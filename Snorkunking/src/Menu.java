import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;
import java.util.Random;


public class Menu
{
    public final static Random rand = new Random();

    public static int WIN_WIDTH = 350;
    public static int WIN_HEIGHT = 650;

    public final static int FPS = 60;

    public static void main(String[] args)
    {
        resizeWindow(350, 650);
        StdDraw.enableDoubleBuffering();

        Font font = new Font("Calibri", Font.BOLD, 14);
        StdDraw.setFont(font);

        KeyboardManager.init();

        boolean replay = true;

        while (replay)
        {
            new Snorkunking();

            Snorkunking.game.loop();
            MessageHandler.clear();
        }
    }

    public static void resizeWindow(int w, int h)
    {
    	WIN_WIDTH = w;
    	WIN_HEIGHT = h;

        StdDraw.setCanvasSize(WIN_WIDTH, WIN_HEIGHT);
        StdDraw.setXscale(0, WIN_WIDTH);
        StdDraw.setYscale(0, WIN_HEIGHT);
    }
}
