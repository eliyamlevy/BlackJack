package Server;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TableThread extends Thread{
	public PlayerThread owner = null;
	private Vector<PlayerThread> players = new Vector<PlayerThread>();
	private Vector <Lock> playerLocks;
	private Vector <Condition> playerConditions;
	private Vector <Integer> deck;
	private int maxPlayers;
	private int curPlayer;
	
	public TableThread(PlayerThread opt, int max) {
		this.owner = opt;
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
		
//		System.out.println("waiting");
//		while (players.size() < max) {
//			bjs.PrintMessage(".");
//		}
		
//		System.out.println("Table created, prompting owner");
//		
//		while (!owner.ready) {
//		}
//		
		System.out.println("Table is ready.");
			
		for (int i = 0; i<players.size(); i++) {
			
			Lock newLock = new ReentrantLock();
			Condition canPlay = newLock.newCondition();
			playerLocks.add(newLock);
			playerConditions.add(canPlay);
			
			if (i==0) players.get(i).set(this, newLock, canPlay, true);
			else players.get(i).set(this, newLock, canPlay, false);
			
		}
		
//		while (true) { //keep table running
////			bjs.PrintMessage("Table is running");
//		}
		
		//Game Logic Starts Here
		deck = new Vector<Integer>();
		BlackJackHelpers.CheckIfEmpty(deck);
		
		//Initialize Dealer AI
		DealerAI dealer = new DealerAI();
		dealer.hand = new Vector<Integer>();
		dealer.hand.add(BlackJackHelpers.DealCard(deck, r));	
		dealer.hand.add(BlackJackHelpers.DealCard(deck, r));
		
		//If other AIs are created, initialize those?
		//Would need to get from table creation
		//How many/what types there would be
		//but the process is identical to Dealer AI
		
		boolean gamerun = true;
		while (gamerun) {
			//Iterate through all players
			for(int i = 0; i<players.size();i++)
			{
				//Start player turn via isturn
			}
			
			//If there are non-dealer AIs, would iterate
			//Through them
			
			//Dealer Takes their turn
			dealer.hitorstay(deck, r);
			//Calc dealer score
			int dscore = BlackJackHelpers.GetIdealScore(dealer.hand);
			
			//Round is over, display results to each player
			
			for(int i = 0; i<players.size();i++)
			{
				int pscore = 0;
				//pscore = players.get(i).FunctionThatReturnsScore;
				
				//Blackjack is bool that is true if player gets blackjack
				
				//if(players.get(i).blackjack == true)
				//{
					//Broadcast to player that they got blackjack
					//Bet increased by 1.5 and added to balance
					//Broadcast winnings
				//}
				//If player busted
				if(pscore>21)
				{
					//Broadcast to player that they lost
					//Bet money is lost
					//Broadcast losings
				}
				//If dealer busted (and player didn't)
				else if(dscore>21)
				{
					//Broadcast to player that dealer busted
					//Bet money is rewarded
					//Broadcast winnings					
				}
				//If dealer has greater hand
				else if(pscore<dscore)
				{
					//Broadcast to player that dealer had better hand
					//Bet money is lost
					//Broadcast losses/balance					
				}
				//If player has better hand
				else if(pscore>dscore)
				{
					//Broadcast to player that they won
					//Bet added to balance
					//Broadcast winnings					
				}
				//If there is a tie
				else if(pscore == dscore)
				{
					//Broadcast to player that something went wrong
					//Bet money is lo
					//Broadcast losses/balance						
				}
				//there shouldnt be any other option but just
				//in case something weird happens player loses
				else
				{
					//Broadcast to player that something went wrong
					//Bet money is subtracted from balance
					//Broadcast losses/balance				
				}
				
			}
     	}
		
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
		
//		owner.ready = false;
		
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
