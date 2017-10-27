package ex3;

import java.util.Arrays;

public class Groupe
{
	public String nom;
	public String style;
	
	private Musicien[] musiciens;
	
	public Groupe(String nom, String style, Musicien[] musiciens)
	{
		this.nom = nom;
		this.style = style;

		this.musiciens = Arrays.copyOf(musiciens, musiciens.length);
		for (int i = 0; i < musiciens.length; i++)
			musiciens[i].rejoindre(this);
	}
}
