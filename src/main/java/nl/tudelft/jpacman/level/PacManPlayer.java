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
public class PacManPlayer extends Player {

    /**
     * The animation that is to be played when Pac-Man dies.
     */
    private final AnimatedSprite deathSprite;
    /**
     * The amount of points accumulated by this player.
     */
    private int score;
    /**
     * <code>true</code> iff this player is alive.
     */
    private boolean alive;

    private Map<Direction, Sprite> sprites;


    /**
     * Creates a new player with a score of 0 points.
     *
     * @param spriteMap      A map containing a sprite for this player for every direction.
     * @param deathAnimation The sprite to be shown when this player dies.
     */
    PacManPlayer(Map<Direction, Sprite> spriteMap, AnimatedSprite deathAnimation) {
        super();
        this.score = 0;
        this.alive = true;
        this.deathSprite = deathAnimation;
        deathSprite.setAnimating(false);
        this.sprites = spriteMap;
    }

    /**
     * Returns whether this player is alive or not.
     *
     * @return <code>true</code> iff the player is alive.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Sets whether this player is alive or not.
     *
     * @param isAlive <code>true</code> iff this player is alive.
     */
    public void setAlive(boolean isAlive) {
        if (isAlive) {
            deathSprite.setAnimating(false);
        }
        if (!isAlive) {
            deathSprite.restart();
        }
        this.alive = isAlive;
    }

    /**
     * Returns the amount of points accumulated by this player.
     *
     * @return The amount of points accumulated by this player.
     */
    public int getScore() {
        return score;
    }

    @Override
    public Sprite getSprite() {
        if (isAlive()) {
            return sprites.get(getDirection());
        }
        return deathSprite;
    }

    /**
     * Adds points to the score of this player.
     *
     * @param points The amount of points to add to the points this player already
     *               has.
     */
    public void addPoints(int points) {
        score += points;
    }

}
