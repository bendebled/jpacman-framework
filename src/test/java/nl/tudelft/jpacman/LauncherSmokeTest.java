package nl.tudelft.jpacman;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.GhostPlayer;
import nl.tudelft.jpacman.level.PacManPlayer;
import nl.tudelft.jpacman.level.Player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Smoke test launching the full game,
 * and attempting to make a number of typical moves.
 *
 * This is <strong>not</strong> a <em>unit</em> test -- it is an end-to-end test
 * trying to execute a large portion of the system's behavior directly from the
 * user interface. It uses the actual sprites and monster AI, and hence
 * has little control over what is happening in the game.
 *
 * Because it is an end-to-end test, it is somewhat longer
 * and has more assert statements than what would be good
 * for a small and focused <em>unit</em> test.
 *
 * @author Arie van Deursen, March 2014.
 */
@SuppressWarnings("magicnumber")
public class LauncherSmokeTest {
	
	private Launcher launcher;
	
	/**
	 * Launch the user interface.
	 */
	@Before
	public void setUpPacman() {
		launcher = new Launcher();
		launcher.launch();
	}
	
	/**
	 * Quit the user interface when we're done.
	 */
	@After
	public void tearDown() {
		launcher.dispose();
	}

    /**
     * Launch the game, and imitate what would happen in a typical game.
     * The test is only a smoke test, and not a focused small test.
     * Therefore it is OK that the method is a bit too long.
     * 
     * @throws InterruptedException Since we're sleeping in this test.
     */
    @SuppressWarnings("methodlength")
    @Test
    public void smokeTest() throws InterruptedException {
        Game game = launcher.getGame();        
        PacManPlayer player = (PacManPlayer)game.getPlayers().get(0);
        GhostPlayer ghostPlayer = (GhostPlayer)game.getPlayers().get(1);

        // start cleanly.
        assertFalse(game.isInProgress());
        game.start();
        assertTrue(game.isInProgress());
        assertEquals(0, player.getScore());

        // get points
        game.move(player, Direction.EAST);
        Thread.sleep(1500);
        assertEquals(60, player.getScore());

        // get pellets on the other side
        game.move(player, Direction.WEST);
        Thread.sleep(3000);
        assertEquals(120, player.getScore());

        // moving back to east won't give us any points
        game.move(player, Direction.EAST);
        Thread.sleep(3000);
        assertEquals(120, player.getScore());

        // ghost player will kill pacman
        game.move(ghostPlayer, Direction.EAST);
        Thread.sleep(1000);
        game.move(ghostPlayer, Direction.SOUTH);
        Thread.sleep(400);
        game.move(ghostPlayer, Direction.EAST);
        Thread.sleep(400);
        game.move(ghostPlayer, Direction.SOUTH);
        Thread.sleep(2000);
        assertFalse(player.isAlive());

        game.stop();
        assertFalse(game.isInProgress());
     }
}
