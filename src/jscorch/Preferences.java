package jscorch;

import jscorch.utilities.GameConstants;

import java.awt.*;

/**
 * A class that statically keeps track of game preferences. May eventually be capable of writing these to a file
 */
public class Preferences {
	/**
	 * A static instance of a Font, to easily change the default font for the game.
	 */
	public static Font FONT = new Font("monospaced", Font.PLAIN, 10);

	/**
	 * A variable containing info on the type of game flow,
	 * using constants from GameConstants
	 */
	public static int GAME_FLOW = GameConstants.FLOW_SEQUENTIAL;

	/**
	 * A variable containing information on the type of wall, for use in collision detection
	 */
	public static int WALL_TYPE = GameConstants.WALL_RUBBER;

	/**
	 * Tells the trajectory engine how far to look outside the window borders before considering a missile lost.
	 */
	public static int PHYSICS_BORDERS_EXTEND = 0;

	/**
	 * The theoretical (wished for) frame-rate for the game.
	 */
	public static double FPS = 40;

	/**
	 * A set of standard colors for tanks.
	 */
	public static Color[] tankColors = {Color.red, Color.orange, Color.yellow, Color.green, new Color(.3f, .3f, 1f), Color.cyan, Color.magenta, Color.gray};

	/**
	 * A static set of names for the tank colors.
	 */
	public static String[] tankColorNames = {"Red", "Orange", "Yellow", "Green", "Blue", "Cyan", "Magenta", "Gray"};

}