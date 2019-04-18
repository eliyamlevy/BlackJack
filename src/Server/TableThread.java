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
	
	public TableThread(PlayerThread opt, int max) {
		owner = opt;
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

		System.out.println("TableThread: Table created, prompting owner");
		
		Boolean oReady = owner.isReady();
		
		while (!oReady) {
			oReady = owner.isReady();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("TableThread: Table is ready.");
			
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
		
		owner.SetReady(false);
		
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
