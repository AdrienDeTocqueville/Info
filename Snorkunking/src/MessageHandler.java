import edu.princeton.cs.introcs.StdDraw;

import java.util.LinkedList;

/**
 * utilities class to display messages and events on the screen
 */
public class MessageHandler
{
	/**
	 * messages buffer
	 */
	private static LinkedList<String> messages = new LinkedList<String>();
	
	private static boolean displaying = false;
	private static long startTime = 0;


	public static void add(String message)
	{
		messages.addLast(message);
	}

	public static void clear()
	{
		messages.clear();
		displaying = false;
	}

	public static void draw()
	{
		if (!messages.isEmpty())
		{
			StdDraw.setPenColor(StdDraw.WHITE);
			StdDraw.text(Menu.WIN_WIDTH / 2, 0.09*Menu.WIN_HEIGHT, getCurrent());
		}
	}

	private static String getCurrent()
	{
		if (!displaying)
		{						
			displaying = true;
			startTime = System.currentTimeMillis();

			return messages.peekFirst();
		}

		else if (System.currentTimeMillis() - startTime < 1500)
			return messages.peekFirst();
			
		else
		{
			displaying = false;
			return messages.pollFirst();
		}
	}
}
