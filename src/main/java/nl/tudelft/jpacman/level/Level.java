package nl.tudelft.jpacman.level;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.npc.NPC;

/**
 * A level of Pac-Man. A level consists of the board with the players and the
 * AIs on it.
 * 
 * @author Jeroen Roosen 
 */
public class Level {

	/**
	 * The board of this level.
	 */
	private final Board board;

	/**
	 * The lock that ensures moves are executed sequential.
	 */
	private final Object moveLock = new Object();

	/**
	 * The lock that ensures starting and stopping can't interfere with each
	 * other.
	 */
	private final Object startStopLock = new Object();

	/**
	 * The NPCs of this level and, if they are running, their schedules.
	 */
	private final Map<NPC, ScheduledExecutorService> npcs;

	/**
	 * <code>true</code> iff this level is currently in progress, i.e. players
	 * and NPCs can move.
	 */
	private boolean inProgress;

	/**
	 * The squares from which players can start this game.
	 */
	private final List<Square> startPacManSquares;

	/**
	 * The start current selected starting square.
	 */
	private int startPacManSquareIndex = 0;

	/**
	 * The squares from which players can start this game.
	 */
	private final List<Square> startGhostSquares;

	/**
	 * The start current selected starting square.
	 */
	private int startGhostSquareIndex = 0;


	/**
	 * The players on this level.
	 */
	private final List<Player> players;

	/**
	 * The table of possible collisions between units.
	 */
	private final CollisionMap collisions;

	/**
	 * The objects observing this level.
	 */
	private final List<LevelObserver> observers;

	private Map<Unit, Direction> directionMap = new HashMap<Unit, Direction>();
	private Timer timer = new Timer();

	/**
	 * Creates a new level for the board.
	 * 
	 * @param b
	 *            The board for the level.
	 * @param ghosts
	 *            The ghosts on the board.
	 * @param startPacManPositions
	 *            The squares on which pacman players start on this board.
	 * @param startGhostPositions
	 *            The squares on which ghost players start on this board.
	 * @param collisionMap
	 *            The collection of collisions that should be handled.
	 */
	public Level(Board b, List<NPC> ghosts, List<Square> startPacManPositions, List<Square> startGhostPositions,
			CollisionMap collisionMap) {
		assert b != null;
		assert ghosts != null;
		assert startPacManPositions != null;
		assert startGhostPositions != null;

		this.board = b;
		this.inProgress = false;
		this.npcs = new HashMap<>();
		for (NPC g : ghosts) {
			npcs.put(g, null);
		}
		this.startPacManSquares = startPacManPositions;
		this.startPacManSquareIndex = 0;
		this.startGhostSquares = startGhostPositions;
		this.startGhostSquareIndex = 0;
		this.players = new ArrayList<>();
		this.collisions = collisionMap;
		this.observers = new ArrayList<>();

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				moveTimer();
			}
		}, 200, 200);
	}

	/**
	 * Adds an observer that will be notified when the level is won or lost.
	 * 
	 * @param observer
	 *            The observer that will be notified.
	 */
	public void addObserver(LevelObserver observer) {
		if (observers.contains(observer)) {
			return;
		}
		observers.add(observer);
	}

	/**
	 * Removes an observer if it was listed.
	 * 
	 * @param observer
	 *            The observer to be removed.
	 */
	public void removeObserver(LevelObserver observer) {
		observers.remove(observer);
	}

	/**
	 * Registers a pacman player on this level, assigning him to a starting position. A
	 * player can only be registered once, registering a player again will have
	 * no effect.
	 * 
	 * @param p
	 *            The player to register.
	 */

	public void registerPacManPlayer(Player p) {
		registerPlayer(p, startPacManSquares, startPacManSquareIndex);
		startPacManSquareIndex++;
		startPacManSquareIndex %= startPacManSquares.size();
	}

	/**
	 * Registers a ghost player on this level, assigning him to a starting position. A
	 * player can only be registered once, registering a player again will have
	 * no effect.
	 *
	 * @param p
	 *            The player to register.
	 */
	public void registerGhostPlayer(Player p) {
		registerPlayer(p, startGhostSquares, startGhostSquareIndex);
		startGhostSquareIndex++;
		startGhostSquareIndex %= startGhostSquares.size();
	}

	/**
	 * Registers a player on this level, assigning him to a starting position. A
	 * player can only be registered once, registering a player again will have
	 * no effect.
	 *
	 * @param p
	 *            The player to register.
	 * @param squareList
	 *            the list where the start player goes
	 * @param squareIndex
	 *            the index of the list
	 */
	private void registerPlayer(Player p, List<Square> squareList, Integer squareIndex){
		assert p != null;
		assert !squareList.isEmpty();

		if (players.contains(p)) {
			return;
		}
		players.add(p);
		Square square = squareList.get(squareIndex);
		p.occupy(square);
	}

	/**
	 * Returns the board of this level.
	 * 
	 * @return The board of this level.
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Moves the unit into the given direction if possible and handles all
	 * collisions.
	 */
	public void moveTimer() {
		if(!directionMap.isEmpty() && isInProgress()) {
			for (Map.Entry <Unit, Direction> entry :directionMap.entrySet()) {
				synchronized (moveLock) {
					Square location = entry.getKey().getSquare();
					Square destination = location.getSquareAt(entry.getValue());

					if (destination.isAccessibleTo(entry.getKey())) {
						entry.getKey().setDirection(entry.getValue());
						List<Unit> occupants = destination.getOccupants();
						entry.getKey().occupy(destination);
						for (Unit occupant : occupants) {
							collisions.collide(entry.getKey(), occupant);
						}
						updateObservers();
					}
				}
			}
		}
	}

	public void move(Unit unit, Direction direction) {
		assert unit != null;
		assert direction != null;

		if (!isInProgress()) {
			return;
		}

		synchronized (moveLock) {

			Square location = unit.getSquare();
			Square destination = location.getSquareAt(direction);

			if (destination.isAccessibleTo(unit)) {
				directionMap.put(unit, direction);
			}
		}
	}

	/**
	 * Starts or resumes this level, allowing movement and (re)starting the
	 * NPCs.
	 */
	public void start() {
		synchronized (startStopLock) {
			if (isInProgress()) {
				return;
			}
			startNPCs();
			inProgress = true;
			updateObservers();
		}
	}

	/**
	 * Stops or pauses this level, no longer allowing any movement on the board
	 * and stopping all NPCs.
	 */
	public void stop() {
		synchronized (startStopLock) {
			if (!isInProgress()) {
				return;
			}
			stopNPCs();
			inProgress = false;
		}
	}

	/**
	 * Starts all NPC movement scheduling.
	 */
	private void startNPCs() {
		for (final NPC npc : npcs.keySet()) {
			ScheduledExecutorService service = Executors
					.newSingleThreadScheduledExecutor();
			service.schedule(new NpcMoveTask(service, npc),
					npc.getInterval() / 2, TimeUnit.MILLISECONDS);
			npcs.put(npc, service);
		}
	}

	/**
	 * Stops all NPC movement scheduling and interrupts any movements being
	 * executed.
	 */
	private void stopNPCs() {
		for (Entry<NPC, ScheduledExecutorService> e : npcs.entrySet()) {
			e.getValue().shutdownNow();
		}
	}

	/**
	 * Returns whether this level is in progress, i.e. whether moves can be made
	 * on the board.
	 * 
	 * @return <code>true</code> iff this level is in progress.
	 */
	public boolean isInProgress() {
		return inProgress;
	}

	/**
	 * Updates the observers about the state of this level.
	 */
	private void updateObservers() {
		if (!isAnyPlayerAlive()) {
			for (LevelObserver o : observers) {
				o.levelLost();
			}
		}
		if (remainingPellets() == 0) {
			for (LevelObserver o : observers) {
				o.levelWon();
			}
		}
	}

	/**
	 * Returns <code>true</code> iff at least one of the players in this level
	 * is alive.
	 * 
	 * @return <code>true</code> if at least one of the registered players is
	 *         alive.
	 */
	public boolean isAnyPlayerAlive() {
		for (Player p : players) {
			if(p instanceof PacManPlayer) {
				PacManPlayer p2 = (PacManPlayer)p;
				if (p2.isAlive()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Counts the pellets remaining on the board.
	 * 
	 * @return The amount of pellets remaining on the board.
	 */
	public int remainingPellets() {
		Board b = getBoard();
		int pellets = 0;
		for (int x = 0; x < b.getWidth(); x++) {
			for (int y = 0; y < b.getHeight(); y++) {
				for (Unit u : b.squareAt(x, y).getOccupants()) {
					if (u instanceof Pellet) {
						pellets++;
					}
				}
			}
		}
		return pellets;
	}

	/**
	 * A task that moves an NPC and reschedules itself after it finished.
	 * 
	 * @author Jeroen Roosen 
	 */
	private final class NpcMoveTask implements Runnable {

		/**
		 * The service executing the task.
		 */
		private final ScheduledExecutorService service;

		/**
		 * The NPC to move.
		 */
		private final NPC npc;

		/**
		 * Creates a new task.
		 * 
		 * @param s
		 *            The service that executes the task.
		 * @param n
		 *            The NPC to move.
		 */
		private NpcMoveTask(ScheduledExecutorService s, NPC n) {
			this.service = s;
			this.npc = n;
		}

		@Override
		public void run() {
			Direction nextMove = npc.nextMove();
			if (nextMove != null) {
				move(npc, nextMove);
			}
			long interval = npc.getInterval();
			service.schedule(this, interval, TimeUnit.MILLISECONDS);
		}
	}

	/**
	 * An observer that will be notified when the level is won or lost.
	 * 
	 * @author Jeroen Roosen 
	 */
	public interface LevelObserver {

		/**
		 * The level has been won. Typically the level should be stopped when
		 * this event is received.
		 */
		void levelWon();

		/**
		 * The level has been lost. Typically the level should be stopped when
		 * this event is received.
		 */
		void levelLost();
	}
}
