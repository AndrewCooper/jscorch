package jscorch.sprite.weapon;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import jscorch.ActionArea;
import jscorch.sprite.Tank;

import java.awt.*;
import java.awt.geom.Point2D;

public class LeapFrog extends NormalMissile {
	private int num;
	private int power;

	public LeapFrog(Tank owner, Point loc, int angle, int p, int n) {
		super(owner, loc, angle, p);
		num = n;
		power = p;
		expType = Weapon.NORMAL_MISSILE;
	}

	/**
	 * happens when a Missile reaches an invalid location and thus explodes
	 */
	protected void explode() {
		super.explode();
		if (num > 0) {
			Point2D.Double v = path.getVelocity();
			int a = (int) (Math.toDegrees(Math.atan2(v.y, v.x)));
			container.addSpriteToLayer(new LeapFrog(owner, curLoc, Math.abs(a), 3 * power / 4, --num), ActionArea.WEAPON_LAYER);
		}
	}
}