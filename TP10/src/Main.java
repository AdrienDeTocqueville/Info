import java.util.Scanner;

public class Main
{
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args)
	{		
		Dictionnaire dico = new Dictionnaire("dico.txt");
		
		String motus = dico.get(), guess;

		System.out.println("Le mot a " + motus.length() + " lettres et commence par la lettre \"" + motus.charAt(0) + "\"");
		
		while (true)
		{
			System.out.print("\nTentez votre chance : ");
			guess = sc.nextLine();
			
			if (guess.length() == 0)
			{
				System.out.println("Dommage ! Le mot était " + motus);
				break;
			}
			
			if (guess.length() != motus.length())
				System.out.println("Mauvaise longueur (" + guess.length() + ")");
			else if (!dico.find(guess))
				System.out.println("Ce mot n'existe pas");
			else
				System.out.println("                      " + evaluer(motus, guess));

			if (motus.equals(guess))
			{
				System.out.println("Bravo !");
				break;
			}
		}

	}

	public static String evaluer(String _word, String _guess)
	{
		StringBuilder res = new StringBuilder();
		
		for (int i = 0; i < _word.length(); i++)
		{
			String regex = "(.*)" + _guess.charAt(i) + "(.*)";
			//System.out.println(regex);
			if (_word.matches(regex))
			{
				if (_guess.charAt(i) == _word.charAt(i))
					res.append("o");
				else
					res.append("-");
			}
			else
				res.append("x");
		}
//			res.charAt(i) = (_word.charAt(i) == _guess.charAt(i)) ? 
		
		return res.toString();
	}
}
