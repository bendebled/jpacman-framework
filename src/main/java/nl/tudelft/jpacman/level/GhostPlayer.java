package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.sprite.Sprite;

import java.util.Map;

/**
 * A player operated unit in our game.
 *
 * @author Jeroen Roosen
 */
public class GhostPlayer extends Player {

    private Map<Direction, Sprite> sprites;

    private boolean won = false;

    GhostPlayer(Map<Direction, Sprite> spriteMap) {
        super();
        this.sprites = spriteMap;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    @Override
    public Sprite getSprite() {
        return sprites.get(getDirection());
    }
}
