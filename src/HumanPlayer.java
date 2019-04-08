import java.util.Scanner;
import java.util.Vector;

public class HumanPlayer extends Player {
	
	public boolean busted;
	public boolean blackjack;
	public int cur_bet;
	
	@Override
	public int getMove() {

		return 0;
	}
	//CURRENTLY ADDING SCANNER FUNCTIONALITY BUT IN FUTURE WILL BE TRANSITIONED TO SERVLET OR SOMETHING
	public int getMove(Scanner s) {
		//poll client for input whether to hit or stay, 0 is stay 1 is hit
		
		//Currently just interacts with the terminal
		String input = "";
		System.out.print("Hit (h) or stay (s):");
		input = s.nextLine();
		if(input.equalsIgnoreCase("h"))
		{
			return 1;
		}
		else if(input.equalsIgnoreCase("s"))
		{
			return 0;
		}
		return -1;
	}
	
	@Override
	public int getBet() {
		// TODO Auto-generated method stub
		return 0;
	}

	//CURRENTLY ADDING SCANNER FUNCTIONALITY BUT IN FUTURE WILL BE TRANSITIONED TO SERVLET OR SOMETHING
	public int getBet(Scanner s) {
		//poll client for input on how much they want to bet using AJAX polling
		
		//Currently just interacts with the terminal
		System.out.print("Place Bet: ");
		try {
		int bet = Integer.parseInt(s.nextLine());
		while(this.cur_balance-bet<0 || bet<=0)
		{
			System.out.println("Invalid bet amount.");
			bet = Integer.parseInt(s.nextLine());
		}
		System.out.println("Betting " + bet + " dollars.");
		this.cur_bet = bet;
		this.cur_balance -= bet;
		System.out.println("Remaining balance: " + this.cur_balance);
		return bet;
		}
		catch (NumberFormatException nfe){
			System.out.println("NumberFormatException: " + nfe.getMessage());
		}
		return -1;
	}
	
	public HumanPlayer(int id, int balance) {
		userID = id;
		cur_balance = balance;
		hand = new Vector <Integer>();
		busted = false;
		blackjack = false;
		cur_bet = 0;
	}




}
