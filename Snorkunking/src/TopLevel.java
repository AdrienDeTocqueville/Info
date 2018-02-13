/**
 * this Class implement the top level (safe level) from abstract superclass
 */

public class TopLevel extends Level
{
	public TopLevel(int _depth)
	{
		super(_depth);
	}

	public Level getPrevious()
	{
		return null;
	}

	public boolean hasChest()
	{
		return false;
	}
	
	public Chest takeChest()
	{
		return null;
	}
	
	public int getCaveId()
	{
		return 0;
	}

	public String text()
	{
		return "Surface";
	}
}
