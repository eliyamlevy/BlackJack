package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class PlayerThread extends Thread{
	
	private PrintWriter pw;
	private BufferedReader br;
	private BJServer bjs;
	private Socket s;
	private TableThread table;
	
	private Lock lock;
	private Condition canPlay;
	private boolean isFirst = false;
	
	public PlayerThread(BJServer bjs, Socket s) {
		this.bjs = bjs;
		this.s = s;
		try {
			pw = new PrintWriter(s.getOutputStream());
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			this.start();
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread constructor: " + ioe.getMessage());
		}
	}
	
	public void sendMessage(String message) {
		pw.println(message);
		pw.flush();
	}
	
	public void set(TableThread t, Lock newLock, Condition play, Boolean first) {
		table = t;
		lock = newLock;
		canPlay = play;
		isFirst = first;
	}
	
	public void run() {
		try {
			String line = "";
			
			while (table == null) {
				line = br.readLine();
				if(line.contains("NEWTABLE")) {
					this.sendMessage("Table created!");
					bjs.createTable(this);
					break;
				}	
			}
			
			while (true) {
				this.sendMessage("In table!");
				lock.lock();
				if (!isFirst) canPlay.await();
				else isFirst = false;
				this.sendMessage("It is your turn");
				while (!line.contains("END_OF_MESSAGE")) {
					table.broadcast(line, this);
					line = br.readLine();
				}
				
				lock.unlock();
				table.signalNextMessenger();
				line = "";
			
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
