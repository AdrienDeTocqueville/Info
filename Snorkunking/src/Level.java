public abstract class Level
{
	public int depth;
	
	public Level(int _depth)
	{
		depth = _depth;
	}

	/**
	 * return the previous Level instance
	 * @return Level
	 */
	public Level getPrevious()
	{
		return Snorkunking.game.getLevel(depth-1);
	}

	/**
	 * return the next Level instance
	 * @return Level
	 */
	public Level getNext()
	{
		return Snorkunking.game.getLevel(depth+1);
	}
	
	public boolean isPersistent()
	{
		return true;
	}
	public String text()
	{
		return "ERROR";
	}
	
	public abstract boolean hasChest();
	public abstract Chest takeChest();
	public abstract int getCaveId();
}
