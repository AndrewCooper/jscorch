package jscorch;

import jscorch.sprite.Tank;
import jscorch.sprite.weapon.Weapon;
import jscorch.utilities.ShieldLocker;
import jscorch.utilities.WeaponLocker;

import java.awt.*;
import java.util.HashMap;

/**
 * A class representing a player of a game of jScorch.
 * This class encapsulates all of the details and actions of a
 * jScorch player.
 */
public class Player {
	private String name;
	private Color color;
	private int tankType;
	private Tank tank;
	private WeaponLocker weapons;
//	private GuidanceLocker guidances;
	private ShieldLocker shields;
	private HashMap properties;

	/**
	 * Creates a new Player with the specified name n, using the tank type t.
	 * @param n the name of the new Player
	 * @param t the type of the new Player's tank
	 */
	public Player(String n, int t, Color c) {
		name = n;
		color = c;
		tankType = t;
		properties = new HashMap();
		properties.put("score", new Integer(0));
		properties.put("cash", new Integer(0));
		properties.put("triggers", new Integer(0));
		properties.put("batteries", new Integer(0));
		properties.put("parachutes", new Integer(0));
		properties.put("autoDefense", new Boolean(false));
		weapons = new WeaponLocker();
		for (int i = 1; i < Weapon.NUM_TYPES; i++) {
			weapons.add(i, 10);
		}
//		guidances = new GuidanceLocker();
		shields = new ShieldLocker();
		for (int i = 1; i < Shield.NUM_TYPES; i++) {
			shields.add(i, 10);
		}
	}

	/**
	 * returns the Player's color
	 * @return player's color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * sets the Player's color
	 * @param c the color
	 */
	public void setColor(Color c) {
		color = c;
	}

	/**
	 * returns the Player's GuidanceLocker
	 * @return a GuidanceLocker
	 */
//	public GuidanceLocker getGuidanceLocker() {
//		return guidances;
//	}

	/**
	 * returns Player's name
	 * @return String of the Player's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the Player's name
	 * @param n the new name
	 */
	public void setName(String n) {
		name = n;
	}

	/**
	 * creates and returns a new Tank
	 * @param a ActionArea passed to the constructor
	 * @return newly created tank
	 */
	public Tank getNewTank(ActionArea a) {
		tank = new Tank(a, this, tankType);
		return tank;
	}

	/**
	 * returns the Player's ShieldLocker
	 * @return a ShieldLocker
	 */
	public ShieldLocker getShieldLocker() {
		return shields;
	}

	/**
	 * returns Player's Tank
	 * @return the Player's Tank
	 */
	public Tank getTank() {
		return tank;
	}

	/**
	 * returns Player's WeaponLocker
	 * @return the Player's WeaponLocker
	 */
	public WeaponLocker getWeaponLocker() {
		return weapons;
	}

	/**
	 * Called when this Player destroys a Tank.
	 * If the Player owns this tank, its score is decremented,
	 * otherwise it is incremented.
	 * @param t the tank that was destroyed
	 */
	public void giveKill(Tank t) {
		int s = ((Integer) properties.get("score")).intValue();
		if (t == tank)
			properties.put("score", new Integer(s - 1));
		else
			properties.put("score", new Integer(s + 1));
	}

	/**
	 * Simply prints some information about this Player object
	 * @return A String of information
	 */
	public String toString() {
		return name;
	}
}
