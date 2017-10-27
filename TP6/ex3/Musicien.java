package ex3;

import java.util.Arrays;
import tp6.Instrument;

public class Musicien
{
	private static int GROUPES_MAX = 10;
	
	private Instrument[] instruments;
	
	private Groupe[] groupes;
	private int nbGroupes;
	
	public Musicien()
	{
		groupes = new Groupe[GROUPES_MAX];
	}
	
	public Musicien(Instrument[] instruments)
	{
		this.instruments = Arrays.copyOf(instruments, instruments.length);
		
		groupes = new Groupe[GROUPES_MAX];
	}
	
	public void rejoindre(Groupe groupe)
	{
		if (nbGroupes < GROUPES_MAX)
			groupes[nbGroupes++] = groupe;
	}
}
