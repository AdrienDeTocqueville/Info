
public class ArbreBinaire
{	
	ArbreBinaire gauche = null;
	ArbreBinaire droit = null;

	String nom;
	
	public ArbreBinaire(String _nom)
	{
		nom = _nom;
	}
	
	public ArbreBinaire(String _nom, ArbreBinaire arbreGauche, ArbreBinaire arbreDroit)
	{
		nom = _nom;
		
		gauche = arbreGauche;
		droit = arbreDroit;
	}
	
	public int getHauteur()
	{
		if (gauche == null && droit == null)
			return 0;

		else if (gauche == null)
			return 1+droit.getHauteur();
		
		else if (droit == null)
			return 1+gauche.getHauteur();

		return 1+Math.max(gauche.getHauteur(), droit.getHauteur());
	}
}
