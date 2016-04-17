package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.npc.ghost.GhostColor;
import nl.tudelft.jpacman.sprite.PacManSprites;

/**
 * Factory that creates Players.
 * 
 * @author Jeroen Roosen 
 */
public class PlayerFactory {

	/**
	 * The sprite store containing the Pac-Man sprites.
	 */
	private final PacManSprites sprites;

	/**
	 * Creates a new player factory.
	 * 
	 * @param spriteStore
	 *            The sprite store containing the Pac-Man sprites.
	 */
	public PlayerFactory(PacManSprites spriteStore) {
		this.sprites = spriteStore;
	}

	/**
	 * Creates a new player with the classic Pac-Man sprites.
	 * 
	 * @return A new player.
	 */
	public PacManPlayer createPacMan() {
		return new PacManPlayer(sprites.getPacmanSprites(),
				sprites.getPacManDeathAnimation());
	}

	/**
	 * Creates a new player with Ghost sprites.
	 *
	 * @return A new player.
	 */
	public GhostPlayer createGhostPlayer() {
		return new GhostPlayer(sprites.getGhostSprite(GhostColor.CYAN));
	}
}
