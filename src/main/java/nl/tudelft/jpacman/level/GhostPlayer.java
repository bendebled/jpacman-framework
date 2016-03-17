package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.sprite.AnimatedSprite;
import nl.tudelft.jpacman.sprite.Sprite;

import java.util.Map;

/**
 * A player operated unit in our game.
 * 
 * @author Jeroen Roosen 
 */
public class GhostPlayer extends Player {

	GhostPlayer(Map<Direction, Sprite> spriteMap) {
		super(spriteMap);
	}
}
