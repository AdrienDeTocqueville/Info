package tp5;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.util.*;

public class RebondMulti
{
	public final static int X_MAX= 64;
	public final static int Y_MAX= 64;
	public final static float WIDTH= 2.0f;
	public final static Color[] colors = {StdDraw.RED, StdDraw.ORANGE, StdDraw.WHITE, StdDraw.BLACK, StdDraw.PINK};
	
	public static void loop()
	{
		Random rand = new Random();
		
		float g = 9.81f;
		
		Balle[] balles = new Balle[5];
		
		for (int i = 0 ; i < balles.length ; ++i)
		{
			balles[i] = new Balle();
			balles[i].setPos(2 + rand.nextInt(60), Y_MAX-2-rand.nextInt(15));
			balles[i].setVel(10.0f*rand.nextInt(11)-6, rand.nextInt(11)-6);
			balles[i].color = colors[rand.nextInt(colors.length)];
		}
			
			
		// Défini l'espace de représentation (visible à l'écran)
		StdDraw.setXscale(-WIDTH, X_MAX+WIDTH);
		StdDraw.setYscale(-WIDTH, Y_MAX+WIDTH);


		long time = System.nanoTime();
		
		while (true)
		{
			float t = (System.nanoTime() - time) * (float)Math.pow(10.0f, -9);
			time = System.nanoTime();

			
			StdDraw.clear(StdDraw.GRAY);

			// Collisions
			float dx, dy;
			for (int i = 0 ; i < balles.length ; ++i)
			{
				for (int j = i+1 ; j < balles.length ; ++j)
				{
					dx = balles[i].px - balles[j].px;
					dy = balles[i].py - balles[j].py;
					
					if (dx*dx + dy*dy < 4.0f*WIDTH*WIDTH)
					{
						balles[i].sym(-dx, -dy);
						balles[j].sym(dx, dy);
					}
						
				}
			}

			// Deplacement
			for (int i = 0 ; i < balles.length ; ++i)
			{
				if (balles[i].py < 0 || balles[i].py > Y_MAX)
				{
					balles[i].py = Math.max(0.0f, Math.min(balles[i].py, Y_MAX));
					balles[i].vy = -balles[i].vy * 0.9f;
				}

				if (balles[i].px < 0 || balles[i].px > X_MAX)
				{
					balles[i].px = Math.max(0.0f, Math.min(balles[i].px, X_MAX));
					balles[i].vx = -balles[i].vx * 0.9f;
				}

				balles[i].vy += g*t;
				balles[i].update(t);

			}

			// Affichage
			for (int i = 0 ; i < balles.length ; ++i)
			{
				// Définit la couleur d'affichage actuelle comme rouge
				StdDraw.setPenColor(balles[i].color);
				StdDraw.filledCircle(balles[i].px, balles[i].py, WIDTH);
			}
			
			// Affiche le dessin et pause pendant 20 ms
			StdDraw.show(20);
		}

	}
}
