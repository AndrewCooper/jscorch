package jscorch.sprite.weapon;

/**
 * <p>Title: JScorch</p>
 * <p>Description: Scorched Earth for Java</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: HK Creations</p>
 * @author Andrew Cooper
 * @version 1.0
 */

import jscorch.sprite.Tank;

import java.awt.*;

/**
 * a Nuke
 */
public class Nuke extends Missile {
	/**
	 * creates a new Nuke
	 */
	public Nuke(Tank owner, Point loc, int angle, int power) {
		super(owner, loc, angle, power);
		expType = Weapon.NUKE;
	}
}