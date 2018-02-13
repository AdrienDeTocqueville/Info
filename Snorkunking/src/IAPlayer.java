
public class IAPlayer extends Player
{
	float[] offsets;
	
	public IAPlayer(int id, float[] _offsets)
	{
		super(id);

		offsets = _offsets;
	}

	public IAPlayer(int id, float[] _offsets, com.grooptown.snorkunking.service.game.Player p)
	{
		super(id, p);

		offsets = _offsets;
	}

	public int getOxygenNeeded(Player p)
	{
		return (1+p.getChestCount()) * p.getDepth();
	}

	public boolean play()
	{
		if (getChestCount() == 0 && level.getCaveId() == 0 && Snorkunking.game.getCurrentPhase() == 2)
			return down();

		int oxyNeeded = getOxygenNeeded(Snorkunking.game.getPlayer(0)) + getOxygenNeeded(Snorkunking.game.getPlayer(1));
		
		
		if (Snorkunking.game.getOxygen() <= oxyNeeded + offsets[Snorkunking.game.getCurrentPhase()])
		{
			if ((getChestCount()>0) && up())
				return true;
			else
				return down();
		}

		if (takeChest())
			return true;
		else
			return down();
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
