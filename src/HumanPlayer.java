import java.util.Vector;

public class HumanPlayer extends Player {
	
	public boolean busted;
	public boolean blackjack;
	public int cur_bet;

	public int getMove() {
		//poll client for input whether to hit or stay, 0 is stay 1 is hit
		return 0;
	}
	
	public int getBet() {
		//poll client for input on how much they want to bet using AJAX polling
		int input = 0;
		
		if (currentBalance - input >= 0) return input;
		else return 0;
	}
	
	public HumanPlayer(int id, int balance) {
		userID = id;
		currentBalance = balance;
		hand = new Vector <Integer>();
		busted = false;
		blackjack = false;
		cur_bet = 0;
	}

}
