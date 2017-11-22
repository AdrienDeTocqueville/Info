package tp5;

import edu.princeton.cs.introcs.StdDraw;
import java.util.*;

public class RebondAnim
{
	public final static int X_MAX= 64;
	public final static int Y_MAX= 24;
	public final static float WIDTH= 0.5f;
	
	public static void loop()
	{
		float g = 9.81f;
		
		Balle balle = new Balle();
			balle.setPos(5, Y_MAX-5);
			balle.setVel(10.0f, 0.0f);
			
		// Défini l'espace de représentation (visible à l'écran)
		StdDraw.setXscale(-WIDTH, X_MAX+WIDTH);
		StdDraw.setYscale(-WIDTH, Y_MAX+WIDTH);

		// Définit la couleur d'affichage actuelle comme rouge
		StdDraw.setPenColor(StdDraw.RED);

		long time = System.nanoTime();
		
		while (true)
		{
			float t = (System.nanoTime() - time) * (float)Math.pow(10.0f, -9);
			time = System.nanoTime();

			
			// Efface l'ecran et le colore en gris
			StdDraw.clear(StdDraw.GRAY);
			
			if (balle.py <= 0 || balle.py >= Y_MAX)
				balle.vy = -balle.vy * 0.9f;
			
			if (balle.px <= 0 || balle.px >= X_MAX)
				balle.vx = -balle.vx * 0.9f;

			
			balle.vy += g*t;

			balle.update(t);
			
			// Dessine un disque de rayon WIDTH aux coordonnées x,y indiquées.
			StdDraw.filledCircle(balle.px, balle.py, WIDTH);
			
			// Affiche le dessin et pause pendant 20 ms
			StdDraw.show(20);
		}

	}
}
