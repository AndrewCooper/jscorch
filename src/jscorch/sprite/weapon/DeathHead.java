package jscorch.sprite.weapon;

/**
 * <p>Title: JScorch</p>
 * <p>Description: Scorched Earth for Java</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: HK Creations</p>
 * @author Andrew Cooper
 * @version 1.0
 */

import jscorch.ActionArea;
import jscorch.sprite.Tank;

import java.awt.*;

/**
 * a Death's Head missile, one that splits into 10 Normal Missiles
 * at its height.
 */
public class DeathHead extends Nuke {
	private int num = 9;

	/**
	 * creates a new Death's Head
	 */
	public DeathHead(Tank o, Point loc, int angle, int power) {
		super(o, loc, angle, power);
	}

	/**
	 * called once per frame, gets the new position of the Missile and
	 * makes sure the position is still valid
	 * @param step the time for one frame to happen
	 */
	public void update(int step) {
		super.update(step);
		if (!path.isRising()) {
			split();
		}
	}

	/**
	 * this method is called when the Mirv reaches its height and must split into 3 missiles
	 */
	private void split() {
		container.removeSpriteFromLayer(this, ActionArea.WEAPON_LAYER);
		int type = Weapon.NORMAL_MISSILE;
		double vx = path.getVelocity().x;
		for (int i = 0; i < num; i++) {
			container.addSpriteToLayer(Weapon.newWeapon(type, owner, new Point(curLoc), 0, (int) ((i + num / 2) * vx / (num - 1))), ActionArea.WEAPON_LAYER);
		}
	}
}