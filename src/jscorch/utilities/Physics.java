package jscorch.utilities;

/**
 * <p>Title: JScorch</p>
 * <p>Description: Scorched Earth for Java</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: HK Creations</p>
 * @author Andrew Cooper
 * @version 1.0
 */

/**
 * Utility class keeping track of variables related to the environment,
 * such as gravity and wind.
 */
public abstract class Physics {
	public static final double gravity = 9.8;
	public static double wind;
	private static final double V_SCALE = 100;

//	private static WindDirLabel windDir = new WindDirLabel();

	/**
	 * returns the wind direction as an integer
	 * @return integer of wind direction
	 */
	public static int getWindDir() {
		if ((int) wind < 0)
			return -1;
		else if ((int) wind > 0)
			return 1;
		else if ((int) wind == 0)
			return 0;
		return 0;
	}

	/**
	 * returns the WindDirLabel object representing the wind of the environment
	 * @return a WindDirLabel for this environment
	 */
//	public static WindDirLabel getWindDirLabel() {
//		return windDir;
//	}

	/**
	 * returns the Velocity Scale for the environment. The Velocity Scale
	 * is what relates the size of the sprites to the size of the environment.
	 * @return the integer velocity scale
	 */
	public static double getVScale() {
		return V_SCALE;
	}

	/**
	 * sets the wind for the environment to a random value between -50 and 50
	 */
	public static void randomWind() {
		wind = Math.random() * 100 - 50;
//		windDir.changeDir();
	}
}
