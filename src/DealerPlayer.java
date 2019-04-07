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
		

		int bet = currentBalance/2;
		currentBalance-=bet;
		return bet;
		
	}
	
	public DealerPlayer(int id, int balance) {
		userID = id;
		currentBalance = balance;
		hand = new Vector <Integer>();
	}

}
