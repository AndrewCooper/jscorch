package jscorch.sprite.explosion;

import jscorch.sprite.Sprite;
import jscorch.sprite.Tank;
import jscorch.sprite.weapon.Weapon;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: acooper
 * Date: Jul 10, 2003
 * Time: 8:25:24 PM
 * To change this template use Options | File Templates.
 */
public abstract class Explosion extends Sprite {
	private Tank owner;
	int type;

	public Explosion(Tank own) {
		super(own.getContainer());
		owner = own;
	}

	/**
	 * creates and returns a new explosion of the given type t, and passing along
	 * the other given info
	 * @param t the type of Explosion to make
	 * @param o the owner of the new Explosion
	 * @param loc the location of the new Explosion
	 * @return a new Explosion
	 */
	public static Explosion newExplosion(int t, Tank o, Point loc) {
		Explosion newExplosion;
		switch (t) {
			case Weapon.BABY_MISSILE:
			case Weapon.NORMAL_MISSILE:
			case Weapon.BABY_NUKE:
			case Weapon.NUKE:
			case Weapon.MIRV:
			case Weapon.DEATH_HEAD:
			case Weapon.LEAP_FROG:
				newExplosion = new CircularExplosion(o, t, loc);
				break;
			case Weapon.FUNKY_BOMB:
				newExplosion = new FunkyBombExplosion(o, t, loc);
				break;
			default:
				System.out.println("Error creating new explosion: type " + t + " does not exist.");
				return null;
		}
		return newExplosion;
	}

}
