import java.util.ArrayList;
import java.util.List;

import com.grooptown.snorkunking.service.game.Game;
import edu.princeton.cs.introcs.StdDraw;

public class Caves
{
	public final static int NB_CAVES = 3;

	/**
	 * contains the levels of the cave
	 */
	private ArrayList<Level> levels;
	public int[] heights;

	public Caves()
	{
		levels = new ArrayList<Level>();
		heights = new int[NB_CAVES];
		
		levels.add(new TopLevel(0));
		
		for (int i = 0; i < NB_CAVES; i++)
		{
			heights[i] = 11 - 3 * i - Menu.rand.nextInt(3);
			
			for (int j = 0; j < heights[i]; j++)
				levels.add(new InnerLevel(i, levels.size()));
		}
		
		heights[0]++;
		heights[NB_CAVES-1]++;
		levels.add(new BottomLevel(levels.size()));
	}

	public Caves(Game gameState)
	{
		levels = new ArrayList<Level>();
		heights = new int[NB_CAVES];

		levels.add(new TopLevel(0));


		for (int i = 0; i < NB_CAVES; i++)
		{
			heights[i] = gameState.getCaves().get(i).getLevels().size();

			for (int j = 0; j < heights[i]; j++)
				levels.add(new InnerLevel(i, levels.size(), gameState.getCaves().get(i).getLevels().get(j)));
		}

		List<com.grooptown.snorkunking.service.game.Level> lastCaveLevels = gameState.getCaves().get(NB_CAVES-1).getLevels();

		heights[0]++;
		levels.remove(levels.size()-1);
		levels.add(new BottomLevel(levels.size(), lastCaveLevels.get(lastCaveLevels.size()-1)));
	}
	


	/**
	 *  return the Level instance for a given index level
	 * @param level int
	 * @return Level
	 */
	public Level getLevel(int level)
	{
		return levels.get(level);
	}

	/**
	 * returns the number of levels in the cave
	 * @return int
	 */
	public int getLevelCount()
	{
		return levels.size();
	}

	/**
	 * removes the levels that don't contains any chests
	 */
	public void removeEmptyLevels()
	{
		for (int i = 0; i < levels.size(); i++)
		{
			levels.get(i).depth = i;
			
			if (!levels.get(i).isPersistent())
			{
				heights[levels.get(i).getCaveId()]--;
				levels.remove(i);
				i--;
			}
		}
	}

	/**
	 * Draw the cave in the context
	 */
	public void draw()
	{		
		final int levelHeight = 2 * Player.RADIUS;
		final int caveWidth = 300;
		
		int posy = Menu.WIN_HEIGHT - 50;

		StdDraw.setPenColor(32, 178, 170);
		for (int i = 0; i < Caves.NB_CAVES; i++)
		{
			int caveHeight = levelHeight * heights[i];

			StdDraw.filledRectangle(caveWidth/2 + 25, posy - 0.5 * caveHeight, caveWidth/2, 0.5 * caveHeight);

			posy -= caveHeight + 10;
		}
		
		
		StdDraw.setPenColor(StdDraw.WHITE);
		for (int i = 0; i < levels.size(); i++)
		{
			StdDraw.text(40, Menu.WIN_HEIGHT - 50 - i * levelHeight - levels.get(i).getCaveId()*10 - 12, levels.get(i).text());

			//if (levels.get(i).hasChest())
				//StdDraw.picture(60, Menu.WIN_HEIGHT - 50 - i * levelHeight - levels.get(i).getCaveId()*10 - 12, "res/chest.png");
		}
	}
}
