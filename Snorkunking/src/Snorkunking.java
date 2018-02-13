import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.grooptown.snorkunking.service.game.PlayerInstance;
import edu.princeton.cs.introcs.StdDraw;
import com.grooptown.snorkunking.service.game.Game;
import com.grooptown.snorkunking.service.game.connector.PlayerConnector;

public class Snorkunking
{
	public static Scanner reader = new Scanner(System.in);

	public static Snorkunking game = null;

	public final static int NB_PHASES = 3;
	public final static int OXYGEN_FACTOR = 2;

	public PlayerConnector c;

	public Player[] players;
	public Caves caves;

	private int currentPlayerId = 0;
	private int currentPhase = 0;
	private int oxygenMax = 0;
	private int oxygen = 0;

	public Snorkunking()
	{
		String myName = "JoueurJava";

		c = new PlayerConnector(42, "https://snorkunking.grooptown.com", myName);
		Game gameState = c.getGame();



		game = this;

		caves = new Caves(gameState);

		players = new Player[]{null, null};

		for (com.grooptown.snorkunking.service.game.Player p: gameState.getPlayers())
		{
			if (p.getName().equals(myName))
			{
				//players[1] = new IAPlayer(1, new float[] { 6.0f, 8, 10f }, p);
				players[1] = new HumanPlayer(1, p);
			}

			else
				//players[0] = new IAPlayer(0, new float[] { 6.0f, 8, 10f }, p); // temp
				players[0] = new OnlinePlayer(0, p);
		}

		if (players[0] == null || players[1] == null)
			System.out.println("Error while loading players");

		while (!gameState.isStarted())
		{
			System.out.println("Waiting for game to start...");

			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			gameState = c.getGame();
		}

		updateCPID();


		oxygenMax = OXYGEN_FACTOR * (caves.getLevelCount() - 1); // Surface doesn't count
		oxygen = gameState.getCurrentStage().getOxygen();
	}

	public void loop()
	{
		draw();

		while (true)
		{
			KeyboardManager.update();

			if (KeyboardManager.isPressed(KeyEvent.VK_ESCAPE))
				return;

			Player currentPlayer = getPlayer(currentPlayerId);

			if (currentPlayer.play())
			{
				if (oxygen <= 0)
				{
					if (++currentPhase == NB_PHASES)
					{
						for (int i = 0; i < players.length; i++)
							players[i].reset();

						break;
					} else
						nextPhase();
				}

				else
				{
					if (currentPlayer.getDepth() == 0)
						currentPlayer.openChests();
				}

				updateCPID();
			}

			draw();
		}

		StdDraw.picture(Menu.WIN_WIDTH / 2, Menu.WIN_HEIGHT / 2, "res/background.png");

		for (int i = 0; i < players.length; ++i)
			players[i].drawScore(true);

		StdDraw.setPenColor(StdDraw.WHITE);
		int winner = getWinner();
		if (winner != -1)
			StdDraw.text(Menu.WIN_WIDTH / 2, Menu.WIN_HEIGHT / 2 + 15, "Le gagnant est P" + (getWinner() + 1) + "!");
		else
			StdDraw.text(Menu.WIN_WIDTH / 2, Menu.WIN_HEIGHT / 2 + 15, "Equalité!");

		StdDraw.text(Menu.WIN_WIDTH / 2, Menu.WIN_HEIGHT / 2 - 15, "Appuyez sur [Entrée] pour continuer");
		StdDraw.show();

		while (!KeyboardManager.isPressed(KeyEvent.VK_ENTER))
		{
			KeyboardManager.update();
		}
	}

	public void updateCPID()
	{
		Game gameState = c.getGame();

		currentPlayerId = gameState.getCurrentIdPlayerTurn();
		System.out.println("Current player id: " + currentPlayerId);

		if (currentPlayerId == -1)
		{
			System.out.print("Invalid player id, enter it: ");
			currentPlayerId = reader.nextInt();
		}
	}

	public int getWinner()
	{
		int winner = -1;
		int maxScore = Integer.MIN_VALUE;

		for (int i = 0; i < players.length; i++)
		{
			if (players[i].getScore() == maxScore)
			{
				winner = -1;
			} else if (players[i].getScore() > maxScore)
			{
				winner = players[i].getId();
				maxScore = players[i].getScore();
			}
		}

		return winner;
	}

	public void consumeOxygen(int count)
	{
		oxygen -= count;
	}

	public int getOxygen()
	{
		return oxygen;
	}

	public Level getLevel(int id)
	{
		return caves.getLevel(id);
	}

	public BottomLevel getBottomLevel()
	{
		return (BottomLevel) caves.getLevel(caves.getLevelCount() - 1);
	}

	public int getCurrentPhase()
	{
		return currentPhase;
	}

	public Player getPlayer(int i)
	{
		return players[i];
	}

	private void draw()
	{
		StdDraw.clear(StdDraw.WHITE);
		StdDraw.picture(Menu.WIN_WIDTH / 2, Menu.WIN_HEIGHT / 2, "res/background.png");

		// affiche les caves
		caves.draw();

		// affiche la phase
		StdDraw.text(40, Menu.WIN_HEIGHT - 30, "Phase " + (currentPhase + 1));

		// affiche les joueurs
		for (int i = 0; i < players.length; ++i)
			players[i].draw((players[i].getId() == currentPlayerId));

		// affiche l'oxygene
		int oxyWidth = (int) ((Menu.WIN_WIDTH - 10) * oxygen / oxygenMax);
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.filledRectangle(oxyWidth / 2 + 5, 15, oxyWidth / 2, 10);

		StdDraw.setPenColor(StdDraw.BOOK_BLUE);
		StdDraw.text(Menu.WIN_WIDTH / 2, 13, "OXYGENE");

		// affiche un message
		MessageHandler.draw();

		StdDraw.pause(1000 / Menu.FPS);
		StdDraw.show();
	}

	private void nextPhase()
	{
		caves.removeEmptyLevels();
		oxygen = oxygenMax;

		for (int i = 0; i < players.length; i++)
			players[i].reset();
	}
}
