import com.grooptown.snorkunking.service.game.Game;

public class Chest
{
	public int nbTreasures;
	
	public Chest(int caveId)
	{
		int[] nbTreasuresMin = {1, 5, 10};
		int[] nbTreasuresMax = {3, 8, 12};
		
		int delta = nbTreasuresMax[caveId] - nbTreasuresMin[caveId] +1;
		
		nbTreasures = Menu.rand.nextInt(delta) + nbTreasuresMin[caveId];
	}

	public Chest(com.grooptown.snorkunking.service.game.Chest chest)
	{
		nbTreasures = chest.getTreasureCount();
	}
}
