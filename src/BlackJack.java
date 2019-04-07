import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BlackJack {
			
	public static void CheckIfEmpty(Vector<Integer> deck)
	{
		if(deck.isEmpty())
		{
			for(int i = 0; i<52; i++)
			{
				deck.add(i);
			}			
		}
	}
	
	public static int DealCard(Vector<Integer> deck, Random r)
	{
		CheckIfEmpty(deck);
		int randIndex = r.nextInt(deck.size());
		int card = deck.get(randIndex);
		deck.remove(randIndex);
		return card;
	}
	
	public static String IntToCard(int num)
	{
		String result = "";
		if(num<0 || num>52)
		{
			return "N/A";
		}
		int thng = num%13;
		if(thng == 0) {result += "Ace ";}
		else if(thng == 10) {result += "Jack ";}
		else if(thng == 11) {result += "Queen ";}
		else if(thng == 12) {result += "King ";}
		else {result += Integer.toString(thng+1) + " ";}
		
		int suit = num/13;
		if(suit == 0) {result += "of Clubs";}
		else if(suit == 1) {result += "of Diamonds";}
		else if(suit == 2) {result += "of Hearts";}
		else if(suit == 3) {result += "of Spades";}
		
		return result; 
	}
	
	public static int IntToScore(int num, boolean ace1)
	{
		int result = 0;
		if(num<0 || num>52)
		{
			return 0;
		}	
		int thng = num%13;
		if(thng == 0) 
		{
			if(ace1){result = 1;}
			else {result = 11;}
		}
		else if(thng == 10) {result = 10;}
		else if(thng == 11) {result = 10;}
		else if(thng == 12) {result = 10;}
		else {result = thng+1;}
		
		return result;
	}
	
	public static int ScoreCalc(Vector<Integer> hand, int aces)
	{
		int result = 0;
		int acecount = aces;
		for(int card : hand)
		{
			int score = 0;
			if(IntToScore(card,false) == 11 && acecount != 0)
			{
				score = IntToScore(card,true);
				acecount--;
			}
			else
			{
				score = IntToScore(card,false);
			}
			result += score;
		}
		return result;
	}
	
	public static String ScoreDisplay(Vector<Integer> hand)
	{
		String result = "";
		int acecount = 0;

		for(int card : hand)
		{
			if(IntToScore(card,false) == 11)
			{
				acecount++;
			}
		}
		for(int i=0;i<acecount;i++)
		{
			result+=Integer.toString(ScoreCalc(hand,acecount-i))+"/";
		}
		result+=Integer.toString(ScoreCalc(hand,0));
		
		return result;
	}
	
	public static int GetIdealScore(Vector<Integer> hand)
	{
		int result = 0;
		Vector<Integer> scores = new Vector<Integer>();
		String scoredisplayer = ScoreDisplay(hand);	
		Pattern pattern = Pattern.compile("[/]");
		Matcher matcher = pattern.matcher(scoredisplayer);
		int acecount = 0;
		while(matcher.find())
		{acecount++;}
		if(acecount ==  0)
		{
			result = ScoreCalc(hand,0);
			return result;
		}
		

		for(int i=0;i<=acecount;i++)
		{
			int score;
			score = ScoreCalc(hand,acecount-i);
			scores.add(score);
		}
		for(int i = 0; i < scores.size(); i++)
		{
			if(scores.get(i)>result)
			{
				if(scores.get(i)<=21)
				{
					result = scores.get(i);
				}
				else if(i==0)
				{
					result = scores.get(i);
				}
			}
		}
		return result;
	}
	
	
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
		
		DealerPlayer dealer = new DealerPlayer(100,1000000);
		
		//START GAME
		boolean game = true;
		while(game)
		{
			int playernum = 1;
			//LOOP THROUGH ALL PLAYERS
			for(HumanPlayer player : players)
			{
				System.out.println("Player "+playernum+"'s turn.");
				System.out.println("Current balance: " + player.currentBalance);
				//GET BET AND CHECK IF IT'S VALID
				System.out.print("Place Bet: ");
				int bet = Integer.parseInt(scan.nextLine());
				while(player.currentBalance-bet<0)
				{
					System.out.println("Invalid bet amount.");
					bet = Integer.parseInt(scan.nextLine());
				}
				System.out.println("Betting " + bet + " dollars.");
				player.cur_bet = bet;
				player.currentBalance -= bet;
				System.out.println("Remaining balance: " + player.currentBalance);
				//DEAL OUT INITIAL CARDS
				int mCard1 = DealCard(deck,r);
				int mCard2 = DealCard(deck,r);
				player.hand.add(mCard1);
				player.hand.add(mCard2);

				//DISPLAY INITIAL SCORE
				System.out.println("Player "+playernum+"'s Hand: "+IntToCard(mCard1)+" ("+mCard1+") - "+IntToCard(mCard2)+" ("+mCard2+")");
				System.out.println("Total Score: " + ScoreDisplay(player.hand));
				if(GetIdealScore(player.hand)==21)
				{
					System.out.println("Player "+playernum+" got a blackjack!");
					player.blackjack = true;
				}
				
				//HITTING AND STAYING
				String input = "";
				while(!input.equalsIgnoreCase("s") && player.blackjack==false && player.busted==false)
				{
					System.out.print("Hit (h) or stay (s):");
					input = scan.nextLine();
					if(input.equalsIgnoreCase("h"))
					{
						player.hand.add(DealCard(deck,r));
						System.out.print("Player 1 Hand: ");
						for(int card : player.hand)
						{
							System.out.print(IntToCard(card)+" ("+card+") ");
						}
						System.out.println("");
						System.out.println("Total Score: " + ScoreDisplay(player.hand));
						if(GetIdealScore(player.hand)>21)
						{
							System.out.println("Player "+playernum+" has busted.");
							player.busted = true;
						}
					}
					else if(input.equalsIgnoreCase("s"))
					{
						System.out.print("Stay");
						System.out.println("Total Score: " + ScoreDisplay(player.hand));
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
			int mCard1 = DealCard(deck,r);
			int mCard2 = DealCard(deck,r);
			dealer.hand.add(mCard1);
			dealer.hand.add(mCard2);

			//DISPLAY DEALER'S SCORE
			System.out.println("Dealer's Hand: "+IntToCard(mCard1)+" ("+mCard1+") - "+IntToCard(mCard2)+" ("+mCard2+")");
			System.out.println("Total Score: " + ScoreDisplay(dealer.hand));
			
			//DEALER AI
			while(ScoreCalc(dealer.hand,0)<17)
			{
				dealer.hand.add(DealCard(deck,r));
				for(int card : dealer.hand)
				{
					System.out.print(IntToCard(card)+" ("+card+") ");
				}
				System.out.println("");
				System.out.println("Total Score: " + ScoreDisplay(dealer.hand));
				if(GetIdealScore(dealer.hand)>21)
				{
					System.out.println("Dealer has busted.");
				}
			}
			
			System.out.println("Everyone has finished. Calculating results.");
			int dscore = GetIdealScore(dealer.hand);
			for(int i = 0; i<players.size(); i++)
			{
				int pscore = GetIdealScore(players.get(i).hand);
				
				System.out.println("Player "+(i+1)+":");
				if(players.get(i).blackjack == true)
				{
					System.out.println("Player "+(i+1)+" got a blackjack!  Win.");
					players.get(i).currentBalance += players.get(i).cur_bet + players.get(i).cur_bet*1.5;
					System.out.println("Player "+(i+1)+" gained "+players.get(i).cur_bet + players.get(i).cur_bet*1.5+" dollars.");
					System.out.println("Current Balance: "+players.get(i).currentBalance);
				}
				if(pscore>21)
				{
					System.out.println("Player "+(i+1)+" has busted ("+pscore+"). Loss.");
					System.out.println("Player "+(i+1)+" lost "+players.get(i).cur_bet+" dollars.");
					System.out.println("Current Balance: "+players.get(i).currentBalance);
				}
				else if(dscore>21)
				{
					System.out.println("Dealer ("+dscore+") busted. Win.");
					players.get(i).currentBalance += 2*players.get(i).cur_bet;
					System.out.println("Player "+(i+1)+" gained "+2*players.get(i).cur_bet+" dollars.");
					System.out.println("Current Balance: "+players.get(i).currentBalance);
				}
				else if(pscore<dscore)
				{
					System.out.println("Dealer ("+dscore+") beat Player "+(i+1)+" ("+pscore+"). Loss.");
					System.out.println("Player "+(i+1)+" lost "+players.get(i).cur_bet+" dollars.");
					System.out.println("Current Balance: "+players.get(i).currentBalance);
				}
				else if(pscore>dscore)
				{
					System.out.println("Dealer ("+dscore+") lost to Player "+i+1+" ("+pscore+"). Win.");
					players.get(i).currentBalance += 2*players.get(i).cur_bet;
					System.out.println("Player "+(i+1)+" gained "+2*players.get(i).cur_bet+" dollars.");
					System.out.println("Current Balance: "+players.get(i).currentBalance);
				}
				else if (dscore == pscore)
				{
					System.out.println("Dealer ("+dscore+") tied with Player "+i+1+" ("+pscore+"). Push.");
					players.get(i).currentBalance += players.get(i).cur_bet;
					System.out.println("Player "+(i+1)+" didn't lose anything.");
					System.out.println("Current Balance: "+players.get(i).currentBalance);	
				}
				else
				{
					System.out.println("Dealer ("+dscore+") - Player "+i+1+" ("+pscore+"). ???.");
					System.out.println("Player "+(i+1)+" lost "+players.get(i).cur_bet+" dollars.");
					System.out.println("Current Balance: "+players.get(i).currentBalance);	
				}
				
				if(players.get(i).currentBalance==0)
				{
					System.out.println("Player "+i+1+" is out of money. Bailout (b) or quit (q)?");
					String input = "";
					while(!input.equalsIgnoreCase("b") && !input.equalsIgnoreCase("q"))
					{
						input = scan.nextLine();
						if(input.equalsIgnoreCase("b"))
						{
							System.out.println("Player "+i+1+" has gained a bailout token.");
							players.get(i).currentBalance += 500;
							System.out.println("Current Balance: "+players.get(i).currentBalance);	
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
