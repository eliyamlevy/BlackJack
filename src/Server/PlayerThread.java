package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class PlayerThread extends Thread{
	
	private PrintWriter pw;
	private BufferedReader br;
	private BJServer bjs;
	private Socket s;
	private TableThread table;
	public Boolean ready = false;
	
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
					this.sendMessage("Please enter max num of players:");
					line = br.readLine();
					int maxNum = Integer.parseInt(line);
					this.sendMessage("Table created!");
					bjs.createTable(this, maxNum);
					
					this.sendMessage("Start Hand?");
					line = br.readLine();
					while (!line.contains("YES")) {}
					ready = true;
					break;
				}
				
				
				else if (line.contains("JOINTABLE")) {
					
					ArrayList<TableThread> tables = bjs.allTables();
					this.sendMessage("Available Tables:");
					for(int i = 0; i < tables.size(); i++) {
						if(tables.get(i).GetOpenSpots() > 0) {
							this.sendMessage("\tTable " + i);
						}
					}
					
					this.sendMessage("Please enter a number:");
					line = br.readLine();
					int tableNum = Integer.parseInt(line);
					
					TableThread t = bjs.getTable(tableNum);
					
					if (t.GetOpenSpots() > 0) {
						this.sendMessage("Joining table: " + tableNum);
						bjs.joinTable(this, tableNum);
						this.sendMessage("Joined table: " + tableNum);
						t.broadcast("Player has joined this table.", this);
						break;
					}

					
					else {
						this.sendMessage("Table is full. Please choose another table or create a new one.");
						
					}

				}
				
			}
			
			while (true) {
				
				this.sendMessage("In table!");
				
				lock.lock();
				if (!isFirst) canPlay.await();
				else isFirst = false;
				this.sendMessage("It is your turn");
				
				line = br.readLine();
				
				if (line.contains("LEAVE")) {
					table.RemovePlayer(this); //check for owner!
					table = null;
					lock = null;
					canPlay = null;
					isFirst = false;
				}
				
//				else if (line.contains("HIT")) {
//					
//				}
//				
//				else if (line.contains("STAY")) {
//					
//				}
//				
//				if (line.contains("BET")) {
//					
//				}
				
				lock.unlock();
				table.signalNextMessenger();
				line = "";
			
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
