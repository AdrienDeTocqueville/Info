import edu.princeton.cs.introcs.StdDraw;
import java.awt.event.KeyEvent;

public class KeyboardManager
{
   private final static int[] keys = {KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_ENTER, KeyEvent.VK_ESCAPE};

    private static int[] pressed;

    public static void init()
    {
        pressed = new int[keys.length];
    }

    public static void update()
    {
        for (int i = 0; i < keys.length; i++)
        {
            if (StdDraw.isKeyPressed(keys[i]))
            {
                if (pressed[i] == 0)
                    pressed[i] = 1;
                else
                    pressed[i] = 2;
            }

            else
                pressed[i] = 0;
        }
    }

    public static boolean isPressed(int key)
    {
        for (int i = 0; i < keys.length; i++)
        {
            if (keys[i] == key)
                return pressed[i] == 1;
        }

        return false;
    }
}
