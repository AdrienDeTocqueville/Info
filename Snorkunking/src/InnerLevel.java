public class InnerLevel extends Level
{
	private int caveId = 0;
	private Chest chest = null;
	
	public InnerLevel(int _caveId, int _depth)
	{
		super(_depth);
		
		caveId = _caveId;
		chest = new Chest(caveId);
	}

	public InnerLevel(int _caveId, int _depth, com.grooptown.snorkunking.service.game.Level level)
	{
		super(_depth);

		caveId = _caveId;

		if (level.getCells().get(0).getChests().size() != 0)
			chest = new Chest(level.getCells().get(0).getChests().get(0));

		else
			chest = null;
	}
	
	public boolean isPersistent()
	{
		return (chest != null);
	}

	public boolean hasChest()
	{
		return (chest != null);
	}

	public String text()
	{
		if (chest == null)
			return "Vide";

		return String.valueOf(chest.nbTreasures);
	}

	public Chest takeChest()
	{
		if (chest == null)
			return null;
		
		Chest copy = chest;
		chest = null;
		
		return copy;
	}
	
	public int getCaveId()
	{
		return caveId;
	}
}
