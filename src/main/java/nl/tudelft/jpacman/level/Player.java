package nl.tudelft.jpacman.level;

import java.util.Map;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.sprite.AnimatedSprite;
import nl.tudelft.jpacman.sprite.Sprite;

/**
 * A player operated unit in our game.
 * 
 * @author Jeroen Roosen 
 */
public abstract class Player extends Unit {

	/**
	 * The animations for every direction.
	 */
	private Map<Direction, Sprite> sprites;

	/**
	 * Creates a new player with a score of 0 points.
	 * 
	 * @param spriteMap
	 *            A map containing a sprite for this player for every direction.
	 */
	Player(Map<Direction, Sprite> spriteMap) {
		this.sprites = spriteMap;
	}

	@Override
	public Sprite getSprite() {
		return sprites.get(getDirection());
	}
}
