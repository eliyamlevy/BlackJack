import java.util.Random;
import java.util.Scanner;
import java.util.Vector;


public class BlackJack {
			
	public static void main(String[] args) {
		Random r = new Random(); 
		//Initialize Card Deck, and Deck to show whether cards are used
		Vector<Integer> deck = new Vector<Integer>();
		for(int i = 0; i<52; i++)
		{
			deck.add(i);
		}
		//NEXT GET PLAYERS
		Scanner scan = new Scanner(System.in);
		System.out.print("Number of players: ");
		int pnum = Integer.parseInt(scan.nextLine());
		
		//INITIALIZE PLAYERS AND AI
		
		int standard_balance = 100;
		
		Vector<HumanPlayer> players = new Vector<HumanPlayer>();
		for(int i = 0; i < pnum; i++)
		{
			HumanPlayer newplayer = new HumanPlayer(i,standard_balance);
			players.add(newplayer);
		}
		
		DealerAI dealer = new DealerAI();
		
		//START GAME
		boolean game = true;
		while(game)
		{
			int playernum = 1;
			//LOOP THROUGH ALL PLAYERS
			for(HumanPlayer player : players)
			{
				System.out.println("Player "+playernum+"'s turn.");
				System.out.println("Current balance: " + player.cur_balance);
				//GET BET AND CHECK IF IT'S VALID
				int getbet = -1;
				while(getbet == -1)
				{
					getbet = player.getBet(scan);
				}
				//DEAL OUT INITIAL CARDS
				player.hand.add(BlackJackHelpers.DealCard(deck,r));
				player.hand.add(BlackJackHelpers.DealCard(deck,r));
				//DISPLAY INITIAL SCORE
				System.out.print("Player "+playernum+" Hand: ");
				BlackJackHelpers.PrintHand(player.hand);
				System.out.println("Total Score: " + BlackJackHelpers.ScoreDisplay(player.hand));
				if(BlackJackHelpers.GetIdealScore(player.hand)==21)
				{
					System.out.println("Player "+playernum+" got a blackjack!");
					player.blackjack = true;
				}
				
				//HITTING AND STAYING
				boolean hsloop = true;
				while(hsloop && player.blackjack==false && player.busted==false)
				{
					int userinput = player.getMove(scan);
					if(userinput == 1)
					{
						player.hand.add(BlackJackHelpers.DealCard(deck,r));
						System.out.print("Player "+playernum+" Hand: ");
						BlackJackHelpers.PrintHand(player.hand);
						System.out.println("Total Score: " + BlackJackHelpers.ScoreDisplay(player.hand));
						if(BlackJackHelpers.GetIdealScore(player.hand)>21)
						{
							System.out.println("Player "+playernum+" has busted.");
							player.busted = true;
						}
					}
					else if(userinput == 0)
					{
						System.out.print("Stay");
						System.out.println("Total Score: " + BlackJackHelpers.ScoreDisplay(player.hand));
						hsloop = false;
					}
					else
					{
						System.out.println("Invalid input.");
					}
				}
				System.out.println("End of Player "+playernum+"'s turn.");
				playernum++;
			}
			//DEALER TURN
			System.out.println("Dealer's turn.");
			dealer.hand.add(BlackJackHelpers.DealCard(deck,r));
			dealer.hand.add(BlackJackHelpers.DealCard(deck,r));

			//DISPLAY DEALER'S SCORE
			BlackJackHelpers.PrintHand(dealer.hand);
			System.out.println("Total Score: " + BlackJackHelpers.ScoreDisplay(dealer.hand));
			
			//DEALER AI
			dealer.hitorstay(deck, r);
			
			System.out.println("Everyone has finished. Calculating results.");
			int dscore = BlackJackHelpers.GetIdealScore(dealer.hand);
			for(int i = 0; i<players.size(); i++)
			{
				int pscore = BlackJackHelpers.GetIdealScore(players.get(i).hand);
				
				System.out.println("Player "+(i+1)+":");
				if(players.get(i).blackjack == true)
				{
					System.out.println("Player "+(i+1)+" got a blackjack!  Win.");
					players.get(i).cur_balance += players.get(i).cur_bet + players.get(i).cur_bet*1.5;
					System.out.println("Player "+(i+1)+" gained "+players.get(i).cur_bet + players.get(i).cur_bet*1.5+" dollars.");
					System.out.println("Current Balance: "+players.get(i).cur_balance);
				}
				if(pscore>21)
				{
					System.out.println("Player "+(i+1)+" has busted ("+pscore+"). Loss.");
					System.out.println("Player "+(i+1)+" lost "+players.get(i).cur_bet+" dollars.");
					System.out.println("Current Balance: "+players.get(i).cur_balance);
				}
				else if(dscore>21)
				{
					System.out.println("Dealer ("+dscore+") busted. Win.");
					players.get(i).cur_balance += 2*players.get(i).cur_bet;
					System.out.println("Player "+(i+1)+" gained "+2*players.get(i).cur_bet+" dollars.");
					System.out.println("Current Balance: "+players.get(i).cur_balance);
				}
				else if(pscore<dscore)
				{
					System.out.println("Dealer ("+dscore+") beat Player "+(i+1)+" ("+pscore+"). Loss.");
					System.out.println("Player "+(i+1)+" lost "+players.get(i).cur_bet+" dollars.");
					System.out.println("Current Balance: "+players.get(i).cur_balance);
				}
				else if(pscore>dscore)
				{
					System.out.println("Dealer ("+dscore+") lost to Player "+i+1+" ("+pscore+"). Win.");
					players.get(i).cur_balance += 2*players.get(i).cur_bet;
					System.out.println("Player "+(i+1)+" gained "+2*players.get(i).cur_bet+" dollars.");
					System.out.println("Current Balance: "+players.get(i).cur_balance);
				}
				else if (dscore == pscore)
				{
					System.out.println("Dealer ("+dscore+") tied with Player "+i+1+" ("+pscore+"). Push.");
					players.get(i).cur_balance += players.get(i).cur_bet;
					System.out.println("Player "+(i+1)+" didn't lose anything.");
					System.out.println("Current Balance: "+players.get(i).cur_balance);	
				}
				else
				{
					System.out.println("Dealer ("+dscore+") - Player "+i+1+" ("+pscore+"). ???.");
					System.out.println("Player "+(i+1)+" lost "+players.get(i).cur_bet+" dollars.");
					System.out.println("Current Balance: "+players.get(i).cur_balance);	
				}
				
				if(players.get(i).cur_balance==0)
				{
					System.out.println("Player "+i+1+" is out of money. Bailout (b) or quit (q)?");
					String input = "";
					while(!input.equalsIgnoreCase("b") && !input.equalsIgnoreCase("q"))
					{
						input = scan.nextLine();
						if(input.equalsIgnoreCase("b"))
						{
							System.out.println("Player "+i+1+" has gained a bailout token.");
							players.get(i).cur_balance += 500;
							System.out.println("Current Balance: "+players.get(i).cur_balance);	
						}
						else if(input.equalsIgnoreCase("q"))
						{
							System.out.println("Player "+i+1+" has left the game.");
							players.remove(i);
						}
						else
						{
							System.out.println("Invalid input.");
						}
					}
				}
			}	
		}
		scan.close();	
	}
}
