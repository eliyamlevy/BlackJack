package Server;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TableThread extends Thread{
	private PlayerThread owner = null;
	private ArrayList<PlayerThread> players = new ArrayList<PlayerThread>();
	private Vector <Lock> playerLocks;
	private Vector <Condition> playerConditions;
	private int curPlayer;
	
	public TableThread(PlayerThread opt) {
		this.owner = opt;
		players.add(opt);
		this.start();
	}
	
	
	@Override
	public void run() {
		//execute game logic
		playerLocks = new Vector<Lock>();
		playerConditions = new Vector<Condition>();
		curPlayer = 0;
			
		for (int i = 0; i<players.size(); i++) {
			
			Lock newLock = new ReentrantLock();
			Condition canPlay = newLock.newCondition();
			playerLocks.add(newLock);
			playerConditions.add(canPlay);
			
			if (i==0) players.get(i).set(this, newLock, canPlay, true);
			
			else players.get(i).set(this, newLock, canPlay, false);
			
		}
		
		while (true) { //keep table running
			
		}
		
	}
	
	public void broadcast(String message, PlayerThread currPT) {
		if (message != null) {
			System.out.println(message);
			for (PlayerThread st : players) {
				if (st!= currPT) st.sendMessage(message);
			}
		}
	}
	
	public void signalNextMessenger() {
		
		curPlayer++;
		if (curPlayer == playerLocks.size()) {
			curPlayer = 0;
		}
		
		playerLocks.get(curPlayer).lock();
		playerConditions.get(curPlayer).signal();
		playerLocks.get(curPlayer).unlock();
	}
	
}
