import java.util.Random;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlackJackHelpers {

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
	
	public static void PrintHand(Vector<Integer> hand)
	{
		for(int card : hand)
		{
			System.out.print(IntToCard(card)+" ("+card+") ");
		}
		System.out.println("");		
	}
	
}
