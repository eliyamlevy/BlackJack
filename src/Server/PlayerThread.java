package Server;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import javax.websocket.Session;

public class PlayerThread extends Thread{
	
	int sessionIndex;
	private TableThread table;
	public Boolean ready = false;
	private String username;
	
	private Lock lock;
	private Condition canPlay;
	private boolean isFirst = false;
	
	public PlayerThread(int index, String username) {
		this.username = username;
		this.sessionIndex = index;
		this.start();
	}
	
	public void sendMessage(String message) {
		//TODO
	}
	
	public void set(TableThread t, Lock newLock, Condition play, Boolean first) {
		table = t;
		lock = newLock;
		canPlay = play;
		isFirst = first;
	}
	
	@Override
	public void run() {
		try {
			String line = "";
			
			while (true) {
				
				this.sendMessage("In table!");
				
				lock.lock();
				if (!isFirst) canPlay.await();
				else isFirst = false;
				this.sendMessage("It is your turn");
				//TODO
//				line = br.readLine();
				
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
	
	public TableThread getTable() {
		return table;
	}
}
