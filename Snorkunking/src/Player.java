import java.util.ArrayList;
import java.awt.Color;
import edu.princeton.cs.introcs.StdDraw;


public abstract class Player
{
	public final static Color[] colors = { StdDraw.CYAN, StdDraw.PINK, StdDraw.RED, StdDraw.MAGENTA };

	public final static int RADIUS = 10;

	protected int id;
	protected int score;
	protected Level level;
	private Color color;

	public ArrayList<Chest> chests;

	public Player(int _id)
	{
		id = _id;
		score = 0;
		color = colors[id];

		level = Snorkunking.game.getLevel(0);

		chests = new ArrayList<Chest>();
	}

	public Player(int _id, com.grooptown.snorkunking.service.game.Player p)
	{
		id = _id;
		score = 0;
		color = colors[id];

		int levelNumber = 0;
		if (p.getCaveIndex() != null)
		{
			for (int i = 0; i < p.getCaveIndex(); i++)
				levelNumber += Snorkunking.game.caves.heights[i];

			levelNumber += p.getLevelIndex() +1;
		}

		level = Snorkunking.game.getLevel(levelNumber);

		chests = new ArrayList<Chest>();
	}

	/**
	 * Action to go down for the player, return true is the action is accomplished, false otherwise
	 * @return Boolean
	 */
	public boolean down()
	{
		Level nextLevel = level.getNext();
		if (nextLevel == null)
		{
			MessageHandler.add("You've hit the bottom!");
			return false;
		}
		
		level = nextLevel;

		Snorkunking.game.consumeOxygen(1 + chests.size());
		return true;
	}

	/**
	 * Action to go up for the player, return true is the action is accomplished, fasle otherwise
	 * @return Boolean
	 */
	public boolean up()
	{
		Level previousLevel = level.getPrevious();
		if (previousLevel == null)
		{
			MessageHandler.add("You are at the surface!");
			return false;
		}
		
		level = previousLevel;

		Snorkunking.game.consumeOxygen(1 + chests.size());		
		return true;
	}

	/**
	 * Action to take the chest for the player, return true is the action is accomplished, fasle otherwise
	 * @return Boolean
	 */
	public boolean takeChest()
	{
		Chest chest = level.takeChest();

		if (chest == null)
		{
			MessageHandler.add("No chest to be found here!");
			return false;
		}

		chests.add(chest);

		MessageHandler.add("Hooray! You found a chest!");
		Snorkunking.game.consumeOxygen(1);

		return true;

	}

	/**
	 * converts player's chest into point (is called when in the safe zone
	 */
	public void openChests()
	{
		if (chests.isEmpty())
			return;

		int newTreasures = 0;
		for (int i = 0; i < chests.size(); i++)
			newTreasures += chests.get(i).nbTreasures;

		chests.clear();

		score += newTreasures;

		MessageHandler.add("P" + (id+1) + " opened " + newTreasures + " treasures");
	}

	/**
	 *
	 * return the depth of the player
	 * @return int
	 */
	public int getDepth()
	{
		return level.depth;
	}

	/**
	 *  return the score of the player
	 * @return int
	 */
	public int getScore()
	{
		return score;
	}

	/**
	 * return the id of the player
	 * @return int
	 */
	public int getId()
	{
		return id;
	}


	/**
	 * return the number of chests the player currently has
	 * @return int
	 */
	public int getChestCount()
	{
		return chests.size();
	}

	/**
	 * resets player, and drops chests to the bottom level
	 */
	public void reset()
	{
		level = Snorkunking.game.getLevel(0);

		if (chests.isEmpty())
			return;

		MessageHandler.add("Player " + (id+1) + " lost " + chests.size() + " chest(s)");

		BottomLevel bottom = Snorkunking.game.getBottomLevel();

		for (int i = 0; i < chests.size(); i++)
			bottom.addChest( chests.get(i) );

		chests.clear();
	}


	/**
	 * draw the player in the context
	 * @param isCurrent true for the player whose turn it is
	 */
	public void draw(boolean isCurrent)
	{
		StdDraw.setPenColor(color);

		// affiche le joueur
		int posy = Menu.WIN_HEIGHT - 50 - Player.RADIUS * (2*getDepth()+1) - 10*level.getCaveId();
		StdDraw.picture(140 + id * 120, posy, "res/mask.png");

		if (!isCurrent)
			StdDraw.setPenColor(StdDraw.WHITE);

		drawScore(false);
	}

	/**
	 * show score in the context
	 * @param useColor
	 */
	public void drawScore(boolean useColor)
	{
		if (useColor)
			StdDraw.setPenColor(color);
		
		StdDraw.text(140 + id * 120, Menu.WIN_HEIGHT - 20, "Score P" + (id+1) + ": " + score);
		StdDraw.text(140 + id * 120, Menu.WIN_HEIGHT - 35, "Chests: " + chests.size());
	}

	public abstract boolean play();
}
