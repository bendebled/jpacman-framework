package nl.tudelft.jpacman.game;

import com.google.common.collect.ImmutableList;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.GhostPlayer;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.PacManPlayer;
import nl.tudelft.jpacman.level.Player;

import java.util.List;

/**
 * A game with one player and a single level.
 * 
 * @author Jeroen Roosen 
 */
public class TwoPlayersGame extends Game {

	/**
	 * The player of this game.
	 */
	private final PacManPlayer player1;
	private final GhostPlayer player2;

	/**
	 * The level of this game.
	 */
	private final Level level;

	/**
	 * Create a new single player game for the provided level and player.
	 *
	 * @param p1
	 *            The first player.
	 * @param p2
	 *            The second player.
	 * @param l
	 *            The level.
	 */
	protected TwoPlayersGame(PacManPlayer p1, GhostPlayer p2, Level l) {
		assert p1 != null;
		assert p2 != null;
		assert l != null;

		this.player1 = p1;
		this.player2 = p2;
		this.level = l;
		level.registerPacManPlayer(p1);
		level.registerGhostPlayer(p2);
	}

	@Override
	public List<Player> getPlayers() {
		return ImmutableList.of(player1, player2);
	}

	@Override
	public Level getLevel() {
		return level;
	}

	/**
	 * Moves the player one square to the north if possible.
	 */
	public void moveUp() {
		move(player1, Direction.NORTH);
	}

	/**
	 * Moves the player one square to the south if possible.
	 */
	public void moveDown() {
		move(player1, Direction.SOUTH);
	}

	/**
	 * Moves the player one square to the west if possible.
	 */
	public void moveLeft() {
		move(player1, Direction.WEST);
	}

	/**
	 * Moves the player one square to the east if possible.
	 */
	public void moveRight() {
		move(player1, Direction.EAST);
	}

}