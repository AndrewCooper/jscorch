package jscorch;

import jscorch.sprite.weapon.*;
import jscorch.sprite.Tank;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;

/**
 * A shield protects a tank from damage by receiving the damage itself.
 */
public class Shield {
	/**
	 * Constant representing no shield
	 */
	public static final int NO_SHIELD = 0;
	/**
	 * Constant representing a mag deflector.
	 */
	public static final int MAG_DEFLECTOR = 1;
	/**
	 * Constant representing a normal shield.
	 */
	public static final int SHIELD = 2;
	/**
	 * Constant representing a force shield.
	 */
	public static final int FORCE_SHIELD = 3;
	/**
	 * Constant representing a heavy shield.
	 */
	public static final int HEAVY_SHIELD = 4;
	/**
	 * Constant representing a super mag deflector.
	 */
	public static final int SUPER_MAG = 5;
	/**
	 * Constant String array holding names of shield types.
	 */
	public static final String[] shieldList = {"No Shield", "Mag Deflector", "Shield", "Force Shield", "Heavy Shield", "Super Mag"};
	/**
	 * Constant holding the total number of shield types.
	 */
	public static final int NUM_TYPES = shieldList.length;

	private BufferedImage image;
	private BufferedImage fimage;
	private RescaleOp filter;
	private float[] rule = {1f, 1f, 1f, 1f};
	private float[] off = {0f, 0f, 0f, 0f};
	private Timer timer;

	private double health;
	private double dFactor;

	private int type;
	private int frame;
	private int numFrames;
	private int width;
	private int height;

	/**
	 * Constructor taking an integer type parameter.
	 * @param t Type of the shield to be created.
	 */
	public Shield(int t) {
		type = t;
		String path = "images/shields/";
		String file = null;
		switch (t) {
			case NO_SHIELD:
				health = 0;
				file = null;
				break;
			case MAG_DEFLECTOR:
				health = 100;
				dFactor = 2;
				file = "mag_deflector_strip.png";
				break;
			case SHIELD:
				health = 100;
				dFactor = 1;
				file = "normal_shield_strip.png";
				break;
			case FORCE_SHIELD:
				health = 100;
				dFactor = .9;
				file = "force_shield_strip.png";
				break;
			case HEAVY_SHIELD:
				health = 100;
				dFactor = .8;
				file = "heavy_shield_strip.png";
				break;
			case SUPER_MAG:
				health = 100;
				dFactor = .6;
				file = "super_mag_strip.png";
				break;
		}

		try {
			if (file != null) {
				image = ImageIO.read(jScorch.class.getResource(path + file));
				width = image.getHeight();
				height = image.getHeight();
				numFrames = image.getWidth() / image.getHeight();
				frame = 0;
				timer = new Timer(1000 / numFrames, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						advanceFrame();
					}
				});
				filter = new RescaleOp(rule, off, null);
				fimage = filter.filter(image, null);
				timer.start();
			} else {
				image = null;
				width = 0;
				height = 0;
				numFrames = 0;
				timer = null;

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void advanceFrame() {
		frame = (frame + 1) % numFrames;
	}

	/**
	 * Method called to damage this shield. Damage is scaled according to the type of shield.
	 * @param d Damage to be applied.
	 */
	public void applyDamage(double d) {
		health -= dFactor * d;
		rule[3] = (float) (health / 100);
		filter = new RescaleOp(rule, off, null);
		fimage = filter.filter(image, null);
	}

	/**
	 * Getter returning the current health of the shield.
	 * @return Health of the shield.
	 */
	public double getHealth() {
		return health;
	}

	/**
	 * Getter returning the height of the shield graphic.
	 * @return Height of the shield.
	 */
	public int getHeight() {
		return width;
	}

	/**
	 * Getter returning the width of the shield graphic.
	 * @return Width of the shield.
	 */
	public int getWidth() {
		return height;
	}

	/**
	 * Paints the shield graphic to the given graphic context.
	 * @param g Context to paint into.
	 */
	public void paint(Graphics g) {
		if (image != null) {
			g.drawImage(image, 0, 0, width, height, frame * width, 0, (frame + 1) * width, height, null);
		}
	}

	/**
	 * Getter returning the type of this shield.
	 * @return Type of this shield.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Getter returning the radius of the shield.
	 * @return Radius of the shield.
	 */
	public int getRadius() {
		return width / 2;
	}

	/**
	 * Returns the name of the shield of the given type.
	 * @param i Type of shield
	 * @return Name of shield
	 */
	public static String stringForType(int i) {
		return shieldList[i];
	}
}
