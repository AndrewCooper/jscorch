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
 * a Mirv Missile, one that splits into 5 missiles when it reaches
 * its max height
 */
public class Mirv extends BabyNuke {
	private int num = 5;

	/**
	 * creates a new Mirv Missile
	 */
	public Mirv(Tank o, Point loc, int angle, int power) {
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
			container.removeSpriteFromLayer(this, ActionArea.WEAPON_LAYER);
			int type = Weapon.NORMAL_MISSILE;
			double vx = path.getVelocity().x;
			Point loc = new Point(curLoc);
			for (int i = 0; i < num; i++) {
				container.addSpriteToLayer(Weapon.newWeapon(type, owner, new Point(loc), 0, (int) ((i + num / 2) * vx / (num - 1))), ActionArea.WEAPON_LAYER);
			}
		}
	}
}