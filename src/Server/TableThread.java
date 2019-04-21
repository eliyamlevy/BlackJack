package Server;
import java.util.Random;
import java.util.Vector;

import Game.BlackJackHelpers;
import Game.DealerAI;

public class TableThread extends Thread{
	public PlayerThread owner = null;
	private Vector<PlayerThread> players = new Vector<PlayerThread>();
	private int maxPlayers;
	private Boolean inRound = false;
	private Vector<Integer> deck;
	private Random r;
	private Vector<Integer> scores;
	private boolean modeJustDealer;
	
	public Boolean getRoundStatus() {
		System.out.println("Getting round status: " + inRound);
		return inRound;
	}
	
	public Vector<PlayerThread> getPlayers() {
		return players;
	}
	
	public boolean hasPlayer(PlayerThread pt) {
		return players.contains(pt);
	}
	
	public TableThread(PlayerThread opt, int max,boolean justDealer) {
		owner = opt;
		owner.SetReady(true); //when owner creates table they are automatically ready
		inRound = false;
		this.maxPlayers = max;
		modeJustDealer  = justDealer;
		players.add(opt);
		r = new Random();
		deck = new Vector<Integer>();
		scores = new Vector<Integer>();
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
			
			System.out.println("TableThread: Checking if owner " + owner.username + " wants to start.");
			
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
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			inRound = true;
			System.out.println("TableThread: Table has started.");
			scores.clear();
			
			DealerAI dealer = new DealerAI();
			dealer.hand = new Vector<Integer>();
			//Check if dealer has Blackjack
			boolean dealerBlackJack = false;
			
			System.out.println("Dealer's cards dealt.");
			
			dealer.hand.add(BlackJackHelpers.DealCard(deck,r));
			dealer.hand.add(BlackJackHelpers.DealCard(deck,r));
			
			if(BlackJackHelpers.GetIdealScore(dealer.hand)==21)
			{
				dealerBlackJack = true;
			}
			
			for (int i = 0; i<players.size(); i++) {
				players.get(i).setTurn(true);
				System.out.println("Checking for input from player: " + i);
//				String input = players.get(i).getAction();
//				System.out.println("Performing input: " + input);
//				players.get(i).setTurn(false);
				
				while (players.get(i).isTurn()) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				scores.add(players.get(i).getScore());
				System.out.println("Player " + i + "'s score is " + players.get(i).getScore());		
				
			}
			
			System.out.println("Dealer's turn.");
			int dscore = 0;
			if(!dealerBlackJack)
			{
				dealer.hitorstay(deck, r);
				dscore = BlackJackHelpers.GetIdealScore(dealer.hand);
				System.out.println("Dealer got "+dscore+".");
				
			}
			else
			{
				dscore = 21;
				System.out.println("Dealer got a blackjack!");
			}
			
			System.out.println("Everyone has finished. Calculating results.");
			if(modeJustDealer)
			{
				System.out.println("Everyone against dealer");
				for(int i=0; i<players.size();i++)
				{
					int curBal = players.get(i).getBalance();
					int curBet = players.get(i).getBet();
					int pscore = scores.get(i);
					System.out.println("Player "+(i+1)+":");
					if(pscore>21)
					{
						System.out.println(players.get(i).username+" has busted ("+pscore+"). Loss.");
					}
					else if (dscore == pscore)
					{
						System.out.println("Dealer ("+dscore+") tied with "+players.get(i).username+" ("+pscore+"). Push.");
						players.get(i).setBalance(curBal+(curBet));
					}
					else if(players.get(i).getBlackjack() == true)
					{
						System.out.println(players.get(i).username+" got a blackjack!  Win.");
						players.get(i).setBalance(curBal+(2*curBet));
					}
					else if(dscore>21)
					{
						System.out.println("Dealer ("+dscore+") busted, so "+players.get(i).username+" Wins.");
						players.get(i).setBalance(curBal+(2*curBet));
					}
					else if(pscore<dscore)
					{
						System.out.println("Dealer ("+dscore+") beat "+players.get(i).username+" ("+pscore+"). Loss.");
					}
					else if(pscore>dscore)
					{
						System.out.println("Dealer ("+dscore+") lost to "+players.get(i).username+" ("+pscore+"). Win.");
						players.get(i).setBalance(curBal+(2*curBet));
					}
				}
			}	
			//Pooled mode - highest player takes all, ties are split between players (including dealer)
			else if(!modeJustDealer)
			{
				System.out.println("Pooled prize.");
				int pooledPrize = 0;
				int numWinners = 0;
				Vector<Integer> winners = new Vector<Integer>();
				for(int i=0; i<players.size();i++)
				{
					pooledPrize += players.get(i).getBet();
					//Check for players with blackjacks and store the IDs.
					if(players.get(i).getBlackjack())
					{
						winners.add(i);
					}
				}
				//If players were found with blackjacks
				if(!winners.isEmpty())
				{
					//All those players are winners
					for(int i : winners)
					{
						System.out.println(players.get(i).username+" wins!");
						numWinners++;
					}
					//If dealer also got a blackjack
					if(dealerBlackJack)
					{
						System.out.println("Dealer wins!");
						numWinners++;
					}
					System.out.println("Prize pool divided amongst winners.");
					for(int i : winners)
					{
						players.get(i).setBalance((int)(pooledPrize/numWinners));
					}
				}
				//Nobody got a blackjack
				else
				{
					//Dealer has a blackjack, only winner
					if(dealerBlackJack)
					{
						System.out.println("Dealer wins!");
					}
					//No blackjacks
					else
					{
						//Calculating highest score
						int highestScore = dscore;
						//Compare player scores to dealer score
						for(int i = 0; i<scores.size();i++)
						{
							//If higher than current highest
							//Set highest to that
							//clear winner vector
							//Add player index to winner vector
							if(scores.get(i)>highestScore)
							{
								highestScore = scores.get(i);
								winners.clear();
								winners.add(i);
							}
							//if tied, just add player index to winner vector
							else if(scores.get(i)==highestScore)
							{
								winners.add(i);
							}
						}
						//if winner vector is empty then dealer is only winner
						if(winners.isEmpty())
						{
							System.out.println("Dealer wins!");
						}
						//If winner vector is not empty then at least one player won
						else if(!winners.isEmpty())
						{
							for(int i : winners)
							{
								System.out.println(players.get(i).username+" wins!");
								numWinners++;
							}
							//if highest score is still equal to dealer score then dealer is also a winner
							if(dscore==highestScore)
							{
								System.out.println("Dealer wins!");
								numWinners++;
							}
							System.out.println("Prize pool divided amongst winners.");
							for(int i : winners)
							{
								players.get(i).setBalance((int)(pooledPrize/numWinners));
							}
							
						}
					}
				}
			
			}	
			
			for(int i = 0; i < players.size();i++)
			{
				if(players.get(i).getBalance()==0)
				{
					System.out.println(players.get(i).username+" is out of money");
					//For now just setting balance to 500
					players.get(i).setBalance(500);
					//Remove them or something idk
				}
			}
			
			System.out.println("TableThread: Round over, resetting.");
			inRound = false;
			setNotReady();
			owner.SetStart(false);
				
		}	

	}
	
	public int dealCard() {
		int card = BlackJackHelpers.DealCard(deck, r);
		return card;
	}
	
	public Boolean allReady() {
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
