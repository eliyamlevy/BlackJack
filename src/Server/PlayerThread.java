package Server;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class PlayerThread extends Thread{
	
	public int sessionIndex;
//	private TableThread table;
	private boolean ready = false;
	//variable to represent when owner decides to start the table
	private boolean ownerStart = false;
	public String username;
	private int balance;
	private int bet;
	private String action = null;
	private boolean isTurn = false;
	private boolean blackjack = false;
	private String result = null;
	private Vector<Integer> hand;
	
	public PlayerThread(int index, String username) {
		this.username = username;
		this.sessionIndex = index;
		this.hand = new Vector<Integer>();
		this.start();
	}
	
	public void SetReady(boolean r) {
		ready = r;
		if (ready) {
			System.out.println("PlayerThread: Player " + username + " is ready.");
		}
		
		else {
			System.out.println("PlayerThread: Player " + username + " is not ready.");
		}
	}
	
	public void SetStart(boolean r) {
		ownerStart = r;
		if (ownerStart) {
			System.out.println("PlayerThread: Player " + username + " wants to start table.");
		}
		
		else {
			System.out.println("PlayerThread: Player " + username + " is no longer waiting to start.");
		}
	}
	
	public boolean getStart() {
		return ownerStart;
	}
	
	public void setAction(String input) {
		action = input;
		System.out.println("PlayerThread: Player Action" + input);
	}
	
	public String getAction() {
		while(this.action == null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String action = this.action;
		this.action = null;
		return action;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void sendMessage(String message) {
		//TODO
	}
	
//	public void set(TableThread t, Lock newLock, Condition play, Boolean first) {
//		table = t;
////		lock = newLock;
////		canPlay = play;
////		isFirst = first;
//	}
	
	@Override
	public void run() {
		try {
			while (true) {
				if(this.isTurn) {
					
					//GAME LOGIC BEGINS
					
					//Currently don't have ways out if user chooses to take leave action
					//Midway through game loop
					
					System.out.println(username+"'s turn begins.");
					
					System.out.println("Getting "+username+"'s bet.");
					
					//Bet logic - action must be bet, and bet must be valid
					while(!this.action.contentEquals("Bet"))
					{
						this.action = getAction();
						if(this.action.contentEquals("Bet"))
						{
							//Instead of 0 it should be table.minbet or something
							int minbet = 0;
							while(bet<minbet && bet>balance)
							{
								//bet = GET BET AMOUNT
								System.out.println("Bet recieved.");
								
								if(bet<minbet)
								{
									System.out.println("Bet lower than minimum bet.");
								}
								if(bet>balance)
								{
									System.out.println("Bet higher than current balance.");
								}
							}
						}
					}
					
					//DEAL OUT CARDS TO PLAYER
					//hand.add(table.dealCard());
					//hand.add(table.dealCard());
					
					//Check if player has blackjack
					//If true, notify them and end their turn.
					if(BlackJackHelpers.GetIdealScore(hand)==21)
					{
						blackjack = true;
						System.out.println("User got a blackjack. End Turn.");
						this.isTurn = false;
					}
					//SEND UPDATE TO TABLE FOR BLACKJACK/HAND/SCORE
					//Blackjack = false / hand = current hand / score = BlackJackHelpers.GetIdealScore(hand)
					
					//loop through while loop getting hit/stay commands
					boolean hitstayloop = true;
					while(hitstayloop && !blackjack)
					{
						this.action = getAction();
						if(this.action.equals("Hit"))
						{
							//hand.add(table.dealCard());
							if(BlackJackHelpers.GetIdealScore(hand)>21)
							{
								System.out.println("Player busted. End Turn.");
								hitstayloop = false;
								this.isTurn = false;
								//SEND UPDATE TO TABLE FOR BLACKJACK/HAND/SCORE
								//Blackjack = false / hand = current hand / score = BlackJackHelpers.GetIdealScore(hand)
							}
							else
							{
								System.out.println("Added card. Player still able to play.");
								//SEND UPDATE TO TABLE FOR BLACKJACK/HAND/SCORE
								//Blackjack = false / hand = current hand / score = BlackJackHelpers.GetIdealScore(hand)
							}
						}
						else if(this.action.equals("Stay"))
						{
							System.out.println("Player stayed. End Turn.");
							hitstayloop = false;
							this.isTurn = false;
							//SEND UPDATE TO TABLE FOR BLACKJACK/HAND/SCORE
							//Blackjack = false / hand = current hand / score = BlackJackHelpers.GetIdealScore(hand)
						}
					}
					//Player turn is over
					//With the updates given player hand should be up to date
					
					
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
	
//	public TableThread getTable() {
//		return table;
//	}
//	
//	public void setTable(TableThread t) {
//		table = t;
//	}
	
	public boolean isTurn() {
		return this.isTurn;
	}
	
	public void setTurn(boolean turn) {
		this.isTurn = turn;
	}
	
	public String getResult() {
		while(this.result == null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String result = this.result;
		this.result = null;
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
}
