package nl.tudelft.jpacman;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.game.GameFactory;
import nl.tudelft.jpacman.level.*;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.ui.Action;
import nl.tudelft.jpacman.ui.PacManUI;
import nl.tudelft.jpacman.ui.PacManUiBuilder;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates and launches the JPacMan UI.
 *
 * @author Jeroen Roosen
 */
public class Launcher {

    private static final PacManSprites SPRITE_STORE = new PacManSprites();

    private PacManUI pacManUI;
    private Game game;

    /**
     * Main execution method for the Launcher.
     *
     * @param args The command line arguments - which are ignored.
     * @throws IOException When a resource could not be read.
     */
    public static void main(String[] args) throws IOException {
        new Launcher().launch();
    }

    /**
     * @return The game object this launcher will start when {@link #launch()}
     * is called.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Creates a new game using the level from {@link #makeLevel()}.
     *
     * @return a new Game.
     */
    public Game makeGame() {
        GameFactory gf = getGameFactory();
        Level level = makeLevel();
        //return gf.createSinglePlayerGame(level);
        return gf.createTwoPlayersGame(level);
    }

    /**
     * Creates a new level. By default this method will use the map parser to
     * parse the default board stored in the <code>board.txt</code> resource.
     *
     * @return A new level.
     */
    public Level makeLevel() {
        MapParser parser = getMapParser();
        try (InputStream boardStream = Launcher.class
                .getResourceAsStream("/board.txt")) {
            return parser.parseMap(boardStream);
        } catch (IOException e) {
            throw new PacmanConfigurationException("Unable to create level.", e);
        }
    }

    /**
     * @return A new map parser object using the factories from
     * {@link #getLevelFactory()} and {@link #getBoardFactory()}.
     */
    protected MapParser getMapParser() {
        return new MapParser(getLevelFactory(), getBoardFactory());
    }

    /**
     * @return A new board factory using the sprite store from
     * {@link #getSpriteStore()}.
     */
    protected BoardFactory getBoardFactory() {
        return new BoardFactory(getSpriteStore());
    }

    /**
     * @return The default {@link PacManSprites}.
     */
    protected PacManSprites getSpriteStore() {
        return SPRITE_STORE;
    }

    /**
     * @return A new factory using the sprites from {@link #getSpriteStore()}
     * and the ghosts from {@link #getGhostFactory()}.
     */
    protected LevelFactory getLevelFactory() {
        return new LevelFactory(getSpriteStore(), getGhostFactory());
    }

    /**
     * @return A new factory using the sprites from {@link #getSpriteStore()}.
     */
    protected GhostFactory getGhostFactory() {
        return new GhostFactory(getSpriteStore());
    }

    /**
     * @return A new factory using the players from {@link #getPlayerFactory()}.
     */
    protected GameFactory getGameFactory() {
        return new GameFactory(getPlayerFactory());
    }

    /**
     * @return A new factory using the sprites from {@link #getSpriteStore()}.
     */
    protected PlayerFactory getPlayerFactory() {
        return new PlayerFactory(getSpriteStore());
    }

    /**
     * Adds key events UP, DOWN, LEFT and RIGHT to a game.
     *
     * @param builder The {@link PacManUiBuilder} that will provide the UI.
     * @param game    The game that will process the events.
     */
    protected void addPlayersKeys(final PacManUiBuilder builder, final Game game) {
        final Player p1 = getPlayers(game).get(0);

        Map<Integer, Direction> bindingPlayer1 = new HashMap<>();
        bindingPlayer1.put(KeyEvent.VK_UP, Direction.NORTH);
        bindingPlayer1.put(KeyEvent.VK_LEFT, Direction.WEST);
        bindingPlayer1.put(KeyEvent.VK_RIGHT, Direction.EAST);
        bindingPlayer1.put(KeyEvent.VK_DOWN, Direction.SOUTH);

        bindKeys(builder, game, p1, bindingPlayer1);

        if (getPlayers(game).size() > 1) {
            Player p2 = getPlayers(game).get(1);

            Map<Integer, Direction> bindingPlayer2 = new HashMap<>();
            bindingPlayer2.put(KeyEvent.VK_Z, Direction.NORTH);
            bindingPlayer2.put(KeyEvent.VK_Q, Direction.WEST);
            bindingPlayer2.put(KeyEvent.VK_D, Direction.EAST);
            bindingPlayer2.put(KeyEvent.VK_S, Direction.SOUTH);

            bindKeys(builder, game, p2, bindingPlayer2);
        }
    }

    private List<Player> getPlayers(final Game game) {
        List<Player> players = game.getPlayers();
        if (players.isEmpty()) {
            throw new IllegalArgumentException("Game has 0 players.");
        }
        final List<Player> p = players;
        return p;
    }

    private void bindKeys(final PacManUiBuilder builder, final Game game, Player p, Map<Integer, Direction> binding) {
        for (Map.Entry<Integer, Direction> entry : binding.entrySet()) {
            builder.addKey(entry.getKey(), new Action() {
                @Override
                public void doAction() {
                    game.move(p, entry.getValue());
                }
            });
        }
    }

    /**
     * Creates and starts a JPac-Man game.
     */
    public void launch() {
        game = makeGame();
        PacManUiBuilder builder = new PacManUiBuilder().withDefaultButtons();
        addPlayersKeys(builder, game);
        pacManUI = builder.build(game);
        pacManUI.start();
    }

    /**
     * Disposes of the UI. For more information see {@link javax.swing.JFrame#dispose()}.
     */
    public void dispose() {
        pacManUI.dispose();
    }
}
