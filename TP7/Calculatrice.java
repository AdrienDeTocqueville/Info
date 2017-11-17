import java.util.Scanner;

public class Calculatrice
{	
	public static void main(String[] args)
	{
		Scanner reader = new Scanner(System.in);

		System.out.print("Entrez un expression en NPI: ");		
		String[] command = reader.nextLine().split(" ");
		
		reader.close();
		
		System.out.println("Resultat: " + interpret(command));
	}
	
	static double interpret(String[] expr)
	{
		Pile<Double> operandes = new Pile<Double>();
		
		for (int i = 0; i < expr.length; i++)
		{			
			switch (expr[i])
			{
				case "+":
					operandes.push( operandes.pop() + operandes.pop() );
					
					break;

				case "*":
					operandes.push( operandes.pop() * operandes.pop() );
					
					break;

				case "-":
					operandes.push( -operandes.pop() + operandes.pop() );
					
					break;
					
				default:
					operandes.push( Double.parseDouble(expr[i]) );
			}
		}
		
		return operandes.peek();
	}
}
