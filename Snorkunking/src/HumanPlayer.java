import java.awt.event.KeyEvent;

public class HumanPlayer extends Player
{
    public HumanPlayer(int id, com.grooptown.snorkunking.service.game.Player p)
    {
        super(id, p);
    }

    public boolean play()
    {		
        if (KeyboardManager.isPressed(KeyEvent.VK_UP))
            return up();

        if (KeyboardManager.isPressed(KeyEvent.VK_DOWN))
            return down();

        if (KeyboardManager.isPressed(KeyEvent.VK_ENTER))
            return takeChest();

        return false;
    }

    public boolean down()
    {
        if (super.down())
        {
            Snorkunking.game.c.sendMove("2");
            return true;
        }

        return false;
    }

    public boolean up()
    {
        if (super.up())
        {
            Snorkunking.game.c.sendMove("1");
            return true;
        }

        return false;
    }

    public boolean takeChest()
    {
        if (super.takeChest())
        {
            Snorkunking.game.c.sendMove("3");
            return true;
        }

        return false;
    }
}
