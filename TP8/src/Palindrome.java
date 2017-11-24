import java.util.Scanner;

public class Palindrome
{
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args)
	{
		System.out.print("Entrez une phrase: ");
		String text = sc.nextLine();
		
		text = text.toLowerCase().replaceAll("[^a-z]+", "");

		if (isPalindrome(text))
			System.out.println("C'est un palindrome");
		else
			System.out.println("Ce n'est pas un palindrome");
	}
	
	public static boolean isPalindrome(String text)
	{	
		if (text.length() <= 1)
			return true;
		
		if (text.charAt(0) != text.charAt(text.length()-1))
			return false;
		
		return isPalindrome(text.substring(1, text.length()-1));
	}

}
