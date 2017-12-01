public class Main
{

	static String text = "";
	public static void main(String[] args)
	{
		ArbreBinaire e = new ArbreBinaire("e", new ArbreBinaire("g"), new ArbreBinaire("h"));
		ArbreBinaire b = new ArbreBinaire("b", new ArbreBinaire("d"), e);
		
		ArbreBinaire arbre = new ArbreBinaire("a", b, new ArbreBinaire("c", new ArbreBinaire("f"), null));
		
		System.out.println(arbre.getHauteur());
	}
}