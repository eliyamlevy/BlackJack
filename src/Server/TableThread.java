package Server;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TableThread extends Thread{
	public static PlayerThread owner = null;
	private Vector<PlayerThread> players = new Vector<PlayerThread>();
	private Vector <Lock> playerLocks;
	private Vector <Condition> playerConditions;
	private int maxPlayers;
	private int curPlayer;
	
	public Vector<PlayerThread> getPlayers() {
		return players;
	}
	
	public boolean hasPlayer(PlayerThread pt) {
		return players.contains(pt);
	}
	
	public TableThread(PlayerThread opt, int max) {
		owner = opt;
		owner.SetReady(true); //when owner creates table they are automatically ready
		this.maxPlayers = max;
		players.add(opt);
		this.start();
	}
	
	public void AddPlayer(PlayerThread pt) {
		players.add(pt);
	}
	
	public int GetOpenSpots() {
		return maxPlayers - players.size();
	}
	
	public void RemovePlayer(PlayerThread pt) {
		players.remove(pt); 
	}
		
	@Override
	public void run() {
		//execute game logic
		playerLocks = new Vector<Lock>();
		playerConditions = new Vector<Condition>();
		curPlayer = 0;
		
		System.out.println("TableThread: waiting");

		System.out.println("TableThread: Table created, waiting for all players to be ready");
		
		waitForAllReady();
		
		System.out.println("TableThread: All players are ready.");
		
		System.out.println("TableThread: Checking if owner wants to start.");
		
		Boolean ownerStart = owner.getStart();
		
		int amountOfPlayers = players.size();
		
		while (!ownerStart) {
			//if a new player joins before owner starts go back to waiting for everyone to be ready
			if (players.size() > amountOfPlayers) {
				System.out.println("TableThread: New player has joined, waiting for all ready.");
				waitForAllReady();
				System.out.println("TableThread: Everybody ready, waiting for owner.");
				amountOfPlayers = players.size();
			}
			
			ownerStart = owner.getStart();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		System.out.println("TableThread: Table has started.");
			
//		for (int i = 0; i<players.size(); i++) {
//			
//			Lock newLock = new ReentrantLock();
//			Condition canPlay = newLock.newCondition();
//			playerLocks.add(newLock);
//			playerConditions.add(canPlay);
//			
//			if (i==0) players.get(i).set(this, newLock, canPlay, true);
//			else players.get(i).set(this, newLock, canPlay, false);
//			
//		}
		
//		while (true) { //keep table running
////			bjs.PrintMessage("Table is running");
//		}
		 
//		for (int i = 0; i<players.size(); i++) {
//			String message = "Waiting for player " + i;
//			try {
//				this.wait(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			this.broadcast(message, players.elementAt(i));
//		}
		
		System.out.println("TableThread: Round over, resetting.");
		
		setNotReady();
		owner.SetStart(false);
		
	}
	
	private Boolean allReady() {
		for (PlayerThread pt : players) {
			if (!pt.isReady()) return false;
		}
		return true;
	}
	
	private void waitForAllReady() {
		Boolean allReady = allReady();
		while (!allReady) {
			allReady = allReady();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setNotReady() {
		for (PlayerThread pt : players) {
			if (pt != owner) pt.SetReady(false);
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
