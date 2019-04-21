package Server;
import java.util.Vector;

import Game.BlackJackHelpers;

public class PlayerThread extends Thread{
	
	public int sessionIndex;
	private TableThread table;
	private boolean ready = false;
	//variable to represent when owner decides to start the table
	private boolean ownerStart = false;
	public String username;
	private int balance;
	private int bet;
	private int score;
	private int bailoutTokens;
	private String action = null;
	private boolean isTurn = false;
	private boolean blackjack = false;
	private String result = null;
	private Vector<Integer> hand;
	
	public PlayerThread(int index, String username, TableThread t) {
		this.username = username;
		this.sessionIndex = index;
		this.hand = new Vector<Integer>();
		this.table = t;
		balance = 100;
		score = 0;
		this.start();
	}
	
	public int getBalance() {
		return balance;
	}
	public int getBet() {
		return bet;
	}
	
	public void setScore(int newScore) {
		score = newScore;
	}
	
	public void clearHand() {
		hand.clear();
	}
	
	public boolean getBlackjack() {
		return blackjack;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getBailout() {
 		return bailoutTokens;
 	}
	
	public void setBalance(int newBalance) {
		balance = newBalance;
		return;
	}
	public void setBet(int newBet) {
		bet = newBet;
		return;
	}
	
	public void increaseBailout() {
 		bailoutTokens++;
 	}
	
	
	public Vector<Integer> getHand() {
		return hand;
	}
	
	public void setTable(TableThread t) {
		table = t;
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
		System.out.println("PlayerThread: Player Action: " + input);
	}
	
	public String getAction() {
		return action;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void sendMessage(String message) {
		//TODO
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				if(this.isTurn()) {
					score = 0;
					result = null;
					
					while (action == null) {
						Thread.sleep(10);
					}
					
					System.out.println("Getting "+username+"'s bet.");
					
					String [] details = action.split("\\|");
					
					//Bet logic - action must be bet, and bet must be valid
					while(!details[2].equals("BET"))
					{
						Thread.sleep(10);
						details = action.split("\\|");
					}
					
					bet = Integer.parseInt(details[3]);
					
					//action string is username|act|bet|amount

						//Instead of 0 it should be table.minbet or something
					int minbet = 0;
					while(bet<minbet || bet>balance)
					{
							//bet = GET BET AMOUNT
						System.out.println("Incorrect bet.");
						System.out.println("Balance is " + balance);
							
						if(bet<minbet)
						{
							System.out.println("Bet lower than minimum bet.");
							
						}
						if(bet>balance)
						{
							System.out.println("Bet higher than current balance.");
						}
	
						result = "ERR|PLAYING|INVBET";
						action = null;
						
						while (action == null) {
							Thread.sleep(10);
						}
						
						details = action.split("\\|");
						bet = Integer.parseInt(details[3]);
						
					}
					
					System.out.println("BET done, amount = " + bet);
					result = "SUCCESS";
					action = null;
					setBalance(balance-bet);
					
					//DEAL OUT CARDS TO PLAYER
					hand.add(table.dealCard());
					hand.add(table.dealCard());
					
					//Check if player has blackjack
					//If true, notify them and end their turn.
					if(BlackJackHelpers.GetIdealScore(hand)==21)
					{
						blackjack = true;
						System.out.println("User got a blackjack. End Turn.");
						result = "BLACKJACK";
						setBet((int)(bet*1.5));
						this.setTurn(false);
					}
					//SEND UPDATE TO TABLE FOR BLACKJACK/HAND/SCORE
					//Blackjack = false / hand = current hand / score = BlackJackHelpers.GetIdealScore(hand)
					
					//loop through while loop getting hit/stay commands
					boolean hitstayloop = true;
					while(hitstayloop && !blackjack)
					{
						System.out.println("In hit or stay loop!");
						while (action == null) {
							Thread.sleep(10);
						}
						
						details = action.split("\\|");
						
						if(details[2].equals("HIT"))
						{
							hand.add(table.dealCard());
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
								result = "SUCCESS";
								//SEND UPDATE TO TABLE FOR BLACKJACK/HAND/SCORE
								//Blackjack = false / hand = current hand / score = BlackJackHelpers.GetIdealScore(hand)
							}
							
							action = null;
							
							
						}
						else if(details[2].equals("STAY"))
						{
							System.out.println("Player stayed. End Turn.");
							hitstayloop = false;
							this.isTurn = false;
							action = null;
							//SEND UPDATE TO TABLE FOR BLACKJACK/HAND/SCORE
							//Blackjack = false / hand = current hand / score = BlackJackHelpers.GetIdealScore(hand)
						}
						
					}
					//Player turn is over
					//With the updates given player hand should be up to date
					System.out.println("score is " + BlackJackHelpers.GetIdealScore(hand));
					score = BlackJackHelpers.GetIdealScore(hand);
					BlackJackHelpers.PrintHand(hand);
					result = "SUCCESS";
					action = null;
					bet = -1;
					details = null;
					this.setTurn(false);
					
				}
				Thread.sleep(100);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public boolean isTurn() {
		return this.isTurn;
	}
	
	public void setTurn(boolean turn) {
		this.isTurn = turn;
		if (isTurn) System.out.println("Now turn");
		else System.out.println("No longer turn");
	}
	
	public String getResult() {
		while(this.result == null) {
			try {
				Thread.sleep(10);
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
