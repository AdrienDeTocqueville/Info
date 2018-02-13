import java.util.ArrayList;

public class BottomLevel extends Level

{

	/**
	 * Contains all the chests for the last level level
	 */
	private ArrayList<Chest> chests = new ArrayList<Chest>();

	public BottomLevel(int _depth)
	{
		super(_depth);

		chests.add(new Chest(Caves.NB_CAVES-1));
	}

	public BottomLevel(int _depth, com.grooptown.snorkunking.service.game.Level level)
	{
		super(_depth);

		for (com.grooptown.snorkunking.service.game.Chest ch: level.getCells().get(0).getChests())
		{
			chests.add(new Chest(ch));
		}
	}

	public Level getNext()
	{
		return null;
	}

    /**
     *
     * @return true if there is at least chest
     */
	public boolean hasChest()
	{
		return (chests.size() > 0);
	}

    /**
     * takes a chest from the list, return null if chests is empty
     * @return
     */
	public Chest takeChest()
	{
		if (chests.size() > 0)
			return chests.remove(chests.size()-1);
		
		return null;
	}

	public String text()
	{
		String t = "";

		for (Chest c: chests)
			t += String.valueOf(c.nbTreasures) + " ";

		return t;
	}

    /**
     * Adds chest to chests
     * @param chest
     */
	public void addChest(Chest chest)
	{
		chests.add(chest);
	}

    /**
     * reutn the cave id
     * @return
     */

	public int getCaveId()
	{
		return Caves.NB_CAVES-1;
	}
}
