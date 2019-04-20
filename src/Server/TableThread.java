package Server;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TableThread extends Thread{
	public PlayerThread owner = null;
	private Vector<PlayerThread> players = new Vector<PlayerThread>();
	private int maxPlayers;
	
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
		
		while (true) {
			
			System.out.println("TableThread: waiting for all players to be ready");
			
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
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			
			System.out.println("TableThread: Table has started.");
			
			for (int i = 0; i<players.size(); i++) {
				players.get(i).setTurn(true);
				System.out.println("Checking for input from player: " + i);
				String input = players.get(i).getAction();
				System.out.println("Performing input: " + input);
				players.get(i).setTurn(false);
			}
			
			System.out.println("TableThread: Round over, resetting.");
			
			setNotReady();
			owner.SetStart(false);
				
		}	

		
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
	
}
