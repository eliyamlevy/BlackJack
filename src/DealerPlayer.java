import java.util.Vector;

public class DealerPlayer extends Player {
	
	public int getMove() {
		
		int handSum = 0;
		
		for (Integer c : hand) {
			handSum+=c;
		}
		
		if (handSum >= 17) return 0;
		else return 1;
		
	}
	
	public int getBet() {
		

		int bet = cur_balance/2;
		cur_balance-=bet;
		return bet;
		
	}
	
	public DealerPlayer(int id, int balance) {
		userID = id;
		cur_balance = balance;
		hand = new Vector <Integer>();
	}

}
