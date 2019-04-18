package Server;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class PlayerThread extends Thread{
	
	public int sessionIndex;
	private TableThread table;
	private Boolean ready = false;
	public String username;
	private String action = null;
	private Lock lock;
	private Condition canPlay;
	private boolean isFirst = false;
	private boolean isTurn = false;
	private String result = null;
	
	public PlayerThread(int index, String username) {
		this.username = username;
		this.sessionIndex = index;
		this.start();
	}
	
	public void SetReady(Boolean r) {
		ready = r;
		if (ready) {
			System.out.println("PlayerThread: Player " + username + " is ready.");
		}
		
		else {
			System.out.println("PlayerThread: Player " + username + " is not ready.");
		}
	}
	
	public void setAction(String input) {
		action = input;
		System.out.println("PlayerThread: Player Action" + input);
	}
	
	public Boolean isReady() {
		return ready;
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
			while (true) {
				if(this.isTurn) {
					if(this.action != null) {
						//Do action
						System.out.println("PlayerThread: Perfoming action: " + action);
						action = null;
						this.isTurn = false;
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public TableThread getTable() {
		return table;
	}
	
	public boolean isTurn() {
		return this.isTurn;
	}
	
	public void setTurn(boolean turn) {
		this.isTurn = turn;
	}
	
	public String getResult() {
		while(this.result == null) {
		}
		String result = this.result;
		this.result = null;
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
}
