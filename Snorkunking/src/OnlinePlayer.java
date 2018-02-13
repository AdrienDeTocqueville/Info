import com.grooptown.snorkunking.service.game.moves.RecordMove;

import java.util.List;

public class OnlinePlayer extends Player
{
    String name;
    int lastParsed = -1;

    public OnlinePlayer(int id, com.grooptown.snorkunking.service.game.Player p)
    {
        super(id, p);

        name = p.getName();
    }

    public boolean play()
    {
        Snorkunking.game.c.waitOppenentsAndGetTheirMoves();
        List<RecordMove> moves = Snorkunking.game.c.getGame().getMoveList();

        System.out.println(moves.size() + " moves received:");

        while (moves.get(++lastParsed).getIdPlayer() != 0) {}

        RecordMove move = moves.get(lastParsed);

        System.out.println(move.getMove() + " by " + move.getIdPlayer());

        if (move.getMove().equals("1"))
            up();
        else if (move.getMove().equals("2"))
            down();
        else if (move.getMove().equals("3"))
            takeChest();

        return true;
    }
}
