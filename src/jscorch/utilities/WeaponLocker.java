package jscorch.utilities;

import jscorch.sprite.Tank;
import jscorch.sprite.weapon.Weapon;

import java.awt.*;

/**
 * A convenience class which holds the weapon inventory.
 */
public class WeaponLocker extends Locker{
	private int current;

	/**
	 * Default constructor creates a WeaponLocker with enough spots to hold every weapon type.
	 * Also holds an infinite number of Baby Missiles.
	 */
	public WeaponLocker() {
		inventory = new int[Weapon.NUM_TYPES];
		inventory[Weapon.BABY_MISSILE] = -1;
		current = Weapon.BABY_MISSILE;
	}

	/**
	 * Returns the current weapon's type.
	 * @return Type of current weapon.
	 */
	public int getCurrent() {
		return current;
	}

	/**
	 * Sets the current weapon's type
	 * @param c Type of new current weapon
	 */
	public boolean setCurrent(int c) {
		if (c < inventory.length) {
			current = c;
			return true;
		}
		current = Weapon.BABY_MISSILE;
		System.out.println("Cannot change weapon to type: " + c);
		return false;
	}

	/**
	 * Returns the current weapon's type, respecting empty spots in the inventory.
	 * @return Type of current weapon.
	 */
	public int getCurrentOfInventory() {
		int num = 0;
		for (int i = 0; i < current; i++) {
			if (inventory[i] != 0) {
				num++;
			}
		}
		return num;
	}

	/**
	 * Sets the current weapons's type, respecting empty spots in the inventory
	 * @param num Type of new current weapon.
	 */
	public void setCurrentOfInventory(int num) {
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] != 0) {
				num--;
			}
			if (num < 0) {
				current = i;
				break;
			}
		}
	}

	public String[] checkLocker() {
		String[] list = new String[inventory.length];
		int n = 0;
		for (int i = 0; i < list.length; i++) {
			if (inventory[i] != 0)
				list[n++] = Weapon.stringForType(i) + "(" + getCount(i) + ")";
		}
		String[] result = new String[n];
		for (int i = 0; i < n; i++) {
			result[i] = list[i];
		}
		return result;
	}

	/**
	 * Returns a string of the current weapon's name, followed by count of that type.
	 * @return String of the current weapon.
	 */
	public String toString() {
		return Weapon.stringForType(current) + "(" + getCount(current) + ")";
	}

	/**
	 * Takes a weapon out of the locker, returning it and decrementing the inventory.
	 * @param type Type of weapon.
	 * @param o Owner of new weapon.
	 * @param loc Starting location of new weapon.
	 * @param ang Incident angle of new weapon.
	 * @param pow Firing power of new weapon.
	 */
	public Weapon use(int type, Tank o, Point loc, int ang, int pow) {
		if (inventory[type] != -1) {
			inventory[type]--;
			if (inventory[type] == 0)
				setCurrent(Weapon.BABY_MISSILE);
		}
		return (Weapon.newWeapon(type, o, loc, ang, pow));
	}
}