package jscorch.sprite.weapon;

import jscorch.ActionArea;
import jscorch.sprite.Sprite;
import jscorch.sprite.Tank;
import jscorch.utilities.Trajectory;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: acooper
 * Date: Jul 6, 2003
 * Time: 1:16:37 PM
 * To change this template use Options | File Templates.
 */
public abstract class Weapon extends Sprite {
	/** type for Baby Missile */
	public static final int BABY_MISSILE = 0;
	/** type for Normal Missile */
	public static final int NORMAL_MISSILE = 1;
	/** type for Baby Nuke */
	public static final int BABY_NUKE = 2;
	/** type for Nuke */
	public static final int NUKE = 3;
	/** type for LeapFrog */
	public static final int LEAP_FROG = 4;
	/** type for Mirv Missile */
	public static final int MIRV = 5;
	/** type for Funky Bomb */
	public static final int FUNKY_BOMB = 6;
	/** type for Death's Head */
	public static final int DEATH_HEAD = 7;
	/** array of Weapon Names */
	private static final String[] weapList = {"Baby Missile", "Normal Missile", "Baby Nuke", "Nuke", "Leap Frog", "Mirv", "Funky Bomb", "Death's Head"};
	/** total number of weapon types */
	public static final int NUM_TYPES = weapList.length;

	Trajectory path;
	protected Tank owner;

	public Weapon(ActionArea a, Tank own) {
		super(a);
		owner = own;
	}

	public Tank getOwner() {
		return owner;
	}

	public void setOwner(Tank owner) {
		this.owner = owner;
	}

	/**
	 * creates and returns a new instance of a Weapon of the given type.
	 * passes along all other arguments to the constructor of the Weapon.
	 * @param t type
	 * @param o the owning Tank
	 * @param loc the location of the Weapon
	 * @param ang the initial angle
	 * @param pow the power of the Weapon
	 */
	public static Weapon newWeapon(int t, Tank o, Point loc, int ang, int pow) {
		Weapon newWeapon;
		switch (t) {
			case BABY_MISSILE:
				newWeapon = new BabyMissile(o, loc, ang, pow);
				break;
			case NORMAL_MISSILE:
				newWeapon = new NormalMissile(o, loc, ang, pow);
				break;
			case BABY_NUKE:
				newWeapon = new BabyNuke(o, loc, ang, pow);
				break;
			case NUKE:
				newWeapon = new Nuke(o, loc, ang, pow);
				break;
			case LEAP_FROG:
				newWeapon = new LeapFrog(o, loc, ang, pow, 2);
				break;
			case MIRV:
				newWeapon = new Mirv(o, loc, ang, pow);
				break;
			case FUNKY_BOMB:
				newWeapon = new FunkyBomb(o, loc, ang, pow);
				break;
			case DEATH_HEAD:
				newWeapon = new DeathHead(o, loc, ang, pow);
				break;
			default:
				System.out.println("Error in creation of new Weapon: " + t + " is not a type");
				return null;
		}
		return newWeapon;
	}

	public static String stringForType(int i) {
		return weapList[i];
	}
}