package Game;

import java.util.Random;
import java.util.Vector;

public class DealerAI extends BlackJackAI {
		public void hitorstay(Vector<Integer> deck,Random r) {
			while(BlackJackHelpers.ScoreCalc(this.hand,0)<17)
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
					System.out.println("Dealer has busted.");
				}
			}	
		}
	}