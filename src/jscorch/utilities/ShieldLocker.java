package jscorch.utilities;

import jscorch.utilities.Locker;
import jscorch.Shield;

/**
 * A convenience class which holds the shield inventory.
 */
public class ShieldLocker extends Locker {

	/**
	 * Default constructor creates a ShieldLocker with enough spots to hold every shield type.
	 */
	public ShieldLocker() {
		inventory = new int[Shield.NUM_TYPES];
	}

	public String[] checkLocker() {
		String[] list = new String[inventory.length];
		int n = 0;
		for (int i = 0; i < list.length; i++) {
			if (inventory[i] != 0)
				list[n++] = Shield.stringForType(i) + "(" + getCount(i) + ")";
		}
		String[] result = new String[n];
		for (int i = 0; i < n; i++) {
			result[i] = list[i];
		}
		return result;
	}

	/**
	 * Returns a shield of the given type, decrementing the inventory.
	 * @param type Type of shield.
	 * @return New shield object.
	 */
	public Shield use(int type) {
		if (type != Shield.NO_SHIELD) {
			inventory[type]--;
		}
		return new Shield(type);
	}
}
