package jscorch.utilities;

/**
 * Convenience class which holds a number of items for a player.
 */
public abstract class Locker {
	protected int[] inventory;

	/**
	 * Adds the given amount of the given type of items to this locker.
	 * @param type
	 * @param amt
	 */
	public void add(int type, int amt) {
		this.setCount(type, inventory[type] + amt);
	}

	/**
	 * Returns absolute integer array inventory. For inventory with names, {@see checkLocker()}.
	 * @return Array of integer inventory.
	 */
	public int[] getInventory() {
		return inventory;
	}

	/**
	 * returns an array of strings containing data on
	 * the contents of this Locker. Each string has
	 * the item's name followed by the number of items of
	 * that type in parentheses.
	 * @return Array of strings of locker's contents.
	 */
	public abstract String[] checkLocker();

	/**
	 * Setter to replace the inventory of this locker.
	 * @param inventory Replacement array inventory.
	 */
	public void setInventory(int[] inventory) {
		this.inventory = inventory;
	}

	/**
	 * Getter returns the number of shields of the given type in this locker.
	 * @param type Type of shield.
	 * @return Number of shields.
	 */
	public int getCount(int type) {
		return (inventory[type]==-1?99:inventory[type]);
	}

	/**
	 * Setter to set the given number of a given type of shield.
	 * @param type Type of shield.
	 * @param count Number of shields.
	 */
	private void setCount(int type, int count) {
		inventory[type] = count;
	}

}
