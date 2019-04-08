import java.util.Random;
import java.util.Vector;

public class SafeAI extends BlackJackAI{
		public void hitorstay(Vector<Integer> deck,Random r) {
			int stopat = 15;
			while(BlackJackHelpers.ScoreCalc(this.hand,0)<stopat)
			{
				this.hand.add(BlackJackHelpers.DealCard(deck,r));
				for(int card : this.hand)
				{
					System.out.print(BlackJackHelpers.IntToCard(card)+" ("+card+") ");
				}
				System.out.println("");
				System.out.println("Total Score: " + BlackJackHelpers.ScoreDisplay(this.hand));
				if(BlackJackHelpers.GetIdealScore(this.hand)>21)
				{
					System.out.println("Safe AI Player has busted.");
				}
			}	
		}
	}