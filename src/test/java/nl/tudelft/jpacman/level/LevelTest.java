package nl.tudelft.jpacman.level;

import com.google.common.collect.Lists;
import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.NPC;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Tests various aspects of level.
 *
 * @author Jeroen Roosen
 */
public class LevelTest {

    /**
     * An NPC on this level.
     */
    private final NPC ghost = mock(NPC.class);
    /**
     * Starting position 1.
     */
    private final Square square1 = mock(Square.class);
    /**
     * Starting position 2.
     */
    private final Square square2 = mock(Square.class);
    /**
     * Starting position 1 Ghost.
     */
    private final Square square3 = mock(Square.class);
    /**
     * Starting position 2 Ghost.
     */
    private final Square square4 = mock(Square.class);
    /**
     * The board for this level.
     */
    private final Board board = mock(Board.class);
    /**
     * The collision map.
     */
    private final CollisionMap collisions = mock(CollisionMap.class);
    /**
     * The level under test.
     */
    private Level level;

    /**
     * Sets up the level with the default board, a single NPC and a starting
     * square.
     */
    @Before
    public void setUp() {
        final long defaultInterval = 100L;
        level = new Level(board, Lists.newArrayList(ghost), Lists.newArrayList(
                square1, square2), Lists.newArrayList(square3, square4), collisions);
        when(ghost.getInterval()).thenReturn(defaultInterval);
    }

    /**
     * Validates the state of the level when it isn't started yet.
     */
    @Test
    public void noStart() {
        assertFalse(level.isInProgress());
    }

    /**
     * Validates the state of the level when it is stopped without starting.
     */
    @Test
    public void stop() {
        level.stop();
        assertFalse(level.isInProgress());
    }

    /**
     * Validates the state of the level when it is started.
     */
    @Test
    public void start() {
        level.start();
        assertTrue(level.isInProgress());
    }

    /**
     * Validates the state of the level when it is started then stopped.
     */
    @Test
    public void startStop() {
        level.start();
        level.stop();
        assertFalse(level.isInProgress());
    }

    /**
     * Verifies registering a player puts the player on the correct starting
     * square.
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void registerPlayer() {
        PacManPlayer p = mock(PacManPlayer.class);
        level.registerPacManPlayer(p);
        verify(p).occupy(square1);
    }

    /**
     * Verifies registering a player twice does not do anything.
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void registerPlayerTwice() {
        PacManPlayer p = mock(PacManPlayer.class);
        level.registerPacManPlayer(p);
        level.registerPacManPlayer(p);
        verify(p, times(1)).occupy(square1);
    }

    /**
     * Verifies registering a second player puts that player on the correct
     * starting square.
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void registerSecondPlayer() {
        PacManPlayer p1 = mock(PacManPlayer.class);
        PacManPlayer p2 = mock(PacManPlayer.class);
        level.registerPacManPlayer(p1);
        level.registerPacManPlayer(p2);
        verify(p2).occupy(square2);
    }

    /**
     * Verifies registering a third player puts the player on the correct
     * starting square.
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void registerThirdPlayer() {
        PacManPlayer p1 = mock(PacManPlayer.class);
        PacManPlayer p2 = mock(PacManPlayer.class);
        PacManPlayer p3 = mock(PacManPlayer.class);
        level.registerPacManPlayer(p1);
        level.registerPacManPlayer(p2);
        level.registerPacManPlayer(p3);
        verify(p3).occupy(square1);
    }


	/* Lets do some test on the ghost player */

    /**
     * Verifies registering a ghost player puts the player on the correct starting
     * square.
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void registerGhostPlayer() {
        GhostPlayer p = mock(GhostPlayer.class);
        level.registerGhostPlayer(p);
        verify(p).occupy(square3);
    }

    /**
     * Verifies registering a player twice does not do anything.
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void registerGhostPlayerTwice() {
        GhostPlayer p = mock(GhostPlayer.class);
        level.registerGhostPlayer(p);
        level.registerGhostPlayer(p);
        verify(p, times(1)).occupy(square3);
    }

    /**
     * Verifies registering a second player puts that player on the correct
     * starting square.
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void registerSecondGhostPlayer() {
        GhostPlayer p1 = mock(GhostPlayer.class);
        GhostPlayer p2 = mock(GhostPlayer.class);
        level.registerGhostPlayer(p1);
        level.registerGhostPlayer(p2);
        verify(p2).occupy(square4);
    }

    /**
     * Verifies registering a third player puts the player on the correct
     * starting square.
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void registerThirdGhostPlayer() {
        GhostPlayer p1 = mock(GhostPlayer.class);
        GhostPlayer p2 = mock(GhostPlayer.class);
        GhostPlayer p3 = mock(GhostPlayer.class);
        level.registerGhostPlayer(p1);
        level.registerGhostPlayer(p2);
        level.registerGhostPlayer(p3);
        verify(p3).occupy(square3);
    }

}
