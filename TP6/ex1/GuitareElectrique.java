package ex1;

public class GuitareElectrique extends Guitare
{
	public GuitareElectrique(int nombreCorde, String son)
	{
		super(nombreCorde, son.toUpperCase());
	}
	
	public void disto()
	{
		son += "OUUUIIINNGGG";
	}

}
