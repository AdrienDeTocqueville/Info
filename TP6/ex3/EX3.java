package ex3;

import tp6.Guitare;
import tp6.Instrument;

public class EX3
{

	public static void main(String[] args)
	{
		Musicien dave = new Musicien(new Instrument[] {new Guitare(6, "la")});
		
		Groupe nirvana = new Groupe("Nirvana", "Grunge", new Musicien[]{dave});
		Groupe fooFighters = new Groupe("FooFighters", "HardRock", new Musicien[]{dave});
		
		Festival festival = new FestivalGrunge();
		
		System.out.println(festival.inviter(nirvana));
		System.out.println(festival.inviter(fooFighters));
	}

}
