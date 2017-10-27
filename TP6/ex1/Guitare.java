package ex1;

public class Guitare implements Instrument
{
	public int nombreCorde;
	public String son;
	
	public Guitare(int nombreCorde, String son)
	{
		this.nombreCorde = nombreCorde;
		this.son = son;
	}
	
	public void jouer()
	{
		for (int i = 0; i < nombreCorde; i++)
			System.out.println(son);
	}
}
