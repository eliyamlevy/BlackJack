package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerThread extends Thread{
	private PrintWriter pw;
	private BufferedReader br;
	BJServer bjs;
	Socket s;
	
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
	
	public void run() {
		try {
			while(true) {
				String line = br.readLine();
				if(line.trim().equals("NEWTABLE")) {
					bjs.createTable(this);
				}
			}
		} catch (IOException ioe) {
			System.out.println("ioe in PlayerThread.run(): " + ioe.getMessage());
		}
	}
}
