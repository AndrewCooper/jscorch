package jscorch.utilities;

import jscorch.ActionArea;
import jscorch.sprite.Tank;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * User: acooper
 * Date: Jul 6, 2003
 * Time: 10:04:18 PM
 * To change this template use Options | File Templates.
 */
public class JSKeyAdapter extends KeyAdapter {
	private ActionArea actionArea;
	private Timer keyPressTimer;
	private int keyCode;
	private int mult;
	private int dir;

	public JSKeyAdapter(ActionArea a) {
		actionArea = a;
		mult = 1;
		keyPressTimer = new Timer(1000 / 30, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tank t = actionArea.getCurTank();
				switch (keyCode) {
					case KeyEvent.VK_UP:
					case KeyEvent.VK_DOWN:
						t.setPower(dir * mult);
						actionArea.getStatusBar().setPower(t.getPower());
						break;
					case KeyEvent.VK_LEFT:
					case KeyEvent.VK_RIGHT:
						t.setAngle(dir * mult);
						actionArea.getStatusBar().setAngle(t.getAngle());
						break;
				}
			}
		});
	}

	/**
	 * handles the keystroke method called when a KeyEvent is fired
	 * for a key being pressed
	 * @param e corresponds to the KeyEvent for the key being pressed
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
//		System.out.println(key);
		if (key == KeyEvent.VK_SHIFT) {
			mult = 5;
		}
		if (!keyPressTimer.isRunning()) {
			switch (key) {
				case KeyEvent.VK_UP:
					dir = 1;
					keyCode = KeyEvent.VK_UP;
					keyPressTimer.start();
					break;
				case KeyEvent.VK_DOWN:
					dir = -1;
					keyCode = KeyEvent.VK_DOWN;
					keyPressTimer.start();
					break;
				case KeyEvent.VK_LEFT:
					dir = 1;
					keyCode = KeyEvent.VK_LEFT;
					keyPressTimer.start();
					break;
				case KeyEvent.VK_RIGHT:
					dir = -1;
					keyCode = KeyEvent.VK_RIGHT;
					keyPressTimer.start();
					break;
				case KeyEvent.VK_SPACE:
					actionArea.tankFire();
					break;
				default:
			}
			if (key >= KeyEvent.VK_0 && key <= KeyEvent.VK_9) {
				actionArea.getCurTank().getPlayer().getWeaponLocker().setCurrent(key - KeyEvent.VK_0);
				actionArea.getStatusBar().setWeapon(actionArea.getCurTank().getPlayer().getWeaponLocker().toString());
			}
		}

	}

	/**
	 * method called when the KeyEvent for a key being released is fired
	 * @param e the KeyEvent for the key being released
	 */
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
			case KeyEvent.VK_SHIFT:
				mult = 1;
				break;
			case KeyEvent.VK_UP:
				keyPressTimer.stop();
				break;
			case KeyEvent.VK_DOWN:
				keyPressTimer.stop();
				break;
			case KeyEvent.VK_LEFT:
				keyPressTimer.stop();
				break;
			case KeyEvent.VK_RIGHT:
				keyPressTimer.stop();
				break;
			default:
		}
	}

	/**
	 * Invoked when a key has been typed.
	 * This event occurs when a key press is followed by a key release.
	 */
	public void keyTyped(KeyEvent e) {
		char keychar = e.getKeyChar();
		switch (keychar) {
			case 'w':
//				System.out.println("key w pressed");
				actionArea.getMenus().showMenu(JSPopupMenu.WEAPONS);
				break;
		}
	}
}
