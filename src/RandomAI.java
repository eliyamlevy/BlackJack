import java.util.Random;
import java.util.Vector;

public class RandomAI extends BlackJackAI{
		public void hitorstay(Vector<Integer> deck,Random r) {
			int willhit = r.nextInt(1);
			while(willhit==1)
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
					System.out.println("Random AI Player has busted.");
				}
				willhit = r.nextInt(1);
			}	
		}
	}