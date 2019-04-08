import java.util.Vector;

public abstract class Player {
	
	int userID;
	int cur_balance;
	Vector <Integer> hand; //stores the numerical values of each card
	
	public abstract int getMove(); //0 signifies stay, 1 signifies to hit
	
	public abstract int getBet(); //bet that user wants to make
	
	public int hit(int card) { //0 if player is still in the game, -1 if player is out, card value is the numerical value of the card
		
		hand.add(card);
		
		int totalScore = 0;
		
		for (Integer c : hand) {
			totalScore+=c;
		}
		
		if (totalScore > 21) return -1;
		
		else return 0;
		
	}
	

}
