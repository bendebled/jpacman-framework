package nl.tudelft.jpacman.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A key listener based on a set of keyCode-action pairs.
 * 
 * @author Jeroen Roosen 
 */
class PacKeyListener implements KeyListener {

	/**
	 * The mappings of keyCode to action.
	 */
	private final Map<Integer, Action> mappings;

	Timer timer = new Timer();

	Action action;
	/**
	 * Create a new key listener based on a set of keyCode-action pairs.
	 * @param keyMappings The mappings of keyCode to action.
	 */
	PacKeyListener(Map<Integer, Action> keyMappings) {
		assert keyMappings != null;
		this.mappings = keyMappings;

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (action != null) {
					action.doAction();
				}
			}
		}, 200, 200);

	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		assert e != null;
		Action tempAction = mappings.get(e.getKeyCode());
		if (tempAction != null){
			action = tempAction;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// do nothing
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// do nothing
	}


}