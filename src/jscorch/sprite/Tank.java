package jscorch.sprite;

import jscorch.ActionArea;
import jscorch.Player;
import jscorch.Shield;
import jscorch.utilities.WeaponLocker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.util.HashMap;

/**
 * <p>Title: JScorch</p>
 * <p>Description: Scorched Earth for Java</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: HK Creations</p>
 * @author Andrew Cooper
 * @version 1.0
 * Top level class for graphic representations of Tanks on the playing field.
 * A Tank keeps track of its health, status, power, and angle. Also, the Tank
 * knows the container is belongs in and the Player which owns it.
 */
public class Tank extends Sprite {
	public static final int TYPE_A = 0;
	private int angle;
	private int power;
	protected int barrelX;
	protected int barrelY;
	private int barrelDiam = 4;
	private BufferedImage barrelImage;

	private int barrelLength = 10;
	private AffineTransform barrelTrans;
	private boolean current;
	private boolean alive;
	private double health;
	private Player owner;
	private Point pivot;

	private HashMap properties;

	private Shield shield;
	private Dimension shieldSize;

	//painting vars
	private BufferedImage tankImage;
	private int tankType = 0;

	/**
	 *
	 * @param a
	 * @param own
	 * @param type
	 */
	public Tank(ActionArea a, Player own, int type) {
		super(a);
		owner = own;

		angle = (int) (Math.random() * 180);
		power = 50;
		health = 100;
		current = false;

		properties = new HashMap();
		properties.put("energy", new Integer(0));
		properties.put("fuel", new Integer(0));
		properties.put("safetyThreshold", new Integer(0));
		properties.put("chutesActive", new Boolean(false));
		properties.put("triggerArmed", new Boolean(false));
		properties.put("guidanceArmed", new Boolean(false));
		setType(type);
		newBounds = new Rectangle(0, 0, spriteWidth, spriteHeight);
		setShield(owner.getShieldLocker().use(Shield.MAG_DEFLECTOR));
		repaint();
	}

	/**
	 *
	 * @param t
	 * @return
	 */
	private Rectangle boundsWithShield(Rectangle t) {
		int w = Math.max(t.width, shieldSize.width);
		int h = Math.max(t.height, shieldSize.height);
		return new Rectangle(
		        t.x + t.width / 2 - w / 2,
		        t.y + t.height / 2 - h / 2,
		        w,
		        h);
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public boolean checkCollision(Point p) {
		if (alive) {
			if (newBounds.contains(p)) {
				int x = p.x - newBounds.x;
				int y = p.y - newBounds.y;
				if (((tankImage.getRGB(x, y) & 0xFF000000) >>> 24) == (0xFF)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * doles out damage from a Damager. if the Tank is destroyed, goes
	 * through the Damager to let the killer know.
	 * @param d the damage
	 * @param dam the Damaager
	 */
	/* @todo implement damage function
		public void damage(double d, Damager dam) {
			if (alive) {
				d = shield.damage(d);
				health -= d;
				if (health <= 0) {
					health = 0;
					dam.getExplosion().getTank().getPlayer().giveKill(this);
					explode();
				}
			}
		}
	*/

	/**
	 * used when a tank has lost all of its health and destruction is imminent.
	 */
	/* @todo implement tank death
		public void explode() {
			int t = getPlayer().getWeaponLocker().getCurrent();
			Point c = this.getCenter();
			alive = false;
			container.removeTank(this);
			container.addExplosion(ExplosionTypes.newExplosion(t, this, c));
		}
	*/

	/**
	 * implements firing for the Tank. finds the point at the end of the Tank's
	 * barrel and adds the appropriate weapon there, using the current angle
	 * and power
	 */
	public void fire() {
		int barrelX = (int) ((barrelLength + 1) * Math.cos(Math.toRadians(angle)));
		int barrelY = (int) ((barrelLength + 1) * Math.sin(Math.toRadians(angle)));
		Point l = newBounds.getLocation();
		l.translate(pivot.x + barrelX, pivot.y - barrelY);
		WeaponLocker wl = owner.getWeaponLocker();
		int type = wl.getCurrent();
		container.addSpriteToLayer(wl.use(type, this, l, angle, power), ActionArea.WEAPON_LAYER);
	}

	/**
	 * returns the angle of the barrel, from 0 to 90. The position(left
	 * or right) isn't important, only the magnitude of the angle.
	 * @return integer angle
	 */
	public int getAngle() {
		if (angle <= 90) {
			return angle;
		} else {
			return (180 - angle);
		}
	}

	/**
	 * returns the center point of the Tank in real-world coordinates
	 * @return center point
	 */
	public Point getCenter() {
		return (
		        new Point(
		                newBounds.x + newBounds.width / 2,
		                newBounds.y + newBounds.height / 2));
	}

	/**
	 * returns the health of the Tank
	 * @return health in double precision
	 */
	public double getHealth() {
		return health;
	}

	/**
	 * returns the Player who owns this Tank
	 * @return owner
	 */
	public Player getPlayer() {
		return owner;
	}

	/**
	 * returns the current power of the Tank
	 * @return integer power
	 */
	public int getPower() {
		return power;
	}

	/**
	 *
	 * @return
	 */
	public Shield getShield() {
		return shield;
	}

	/**
	 * returns the type of the Tank
	 * @return type
	 */
	public int getType() {
		return tankType;
	}

	private void initImage() throws IOException {
		Color tankColor = owner.getColor();
		float[] components = new float[4];
		float o = 0f;
		float[] offsets = {o, o, o, 0f};
		tankColor.getComponents(components);
		for (int i = 0; i < components.length - 1; i++) {
			components[i] *= 1.5;
		}

		RescaleOp filter = new RescaleOp(components, offsets, null);
		BufferedImage t =
		        ImageIO.read(
		                jscorch.jScorch.class.getResource("images/tanks/tank1.png"));
		tankImage = filter.filter(t, null);
		BufferedImage b =
		        ImageIO.read(
		                jscorch.jScorch.class.getResource("images/tanks/barrel1.png"));
		barrelImage = filter.filter(b, null);
		pivot = new Point(15, 7); //@todo move tank pivot into tank type def
		barrelLength = barrelImage.getWidth();
		barrelDiam = barrelImage.getHeight();
		spriteWidth = t.getWidth();
		spriteHeight = t.getHeight();
		barrelTrans = new AffineTransform();
		barrelTrans.translate(pivot.x - 1, pivot.y - 1);
		barrelTrans.rotate(-1 * Math.toRadians(angle), 0, barrelDiam / 2.0);
	}

	/**
	 * returns the status of the Tank(alive or dead)
	 * @return boolean status
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * returns true or false for if the Tank is the current one
	 * @return boolean current
	 */
	public boolean isCurrent() {
		return current;
	}

	/**
	 *
	 * @param d
	 */
	public void move(int d) {
		//does nothing!!
	}

	/**
	 *
	 * @return
	 */
	public Rectangle newBounds() {
		return boundsWithShield(newBounds);
	}

	/**
	 * paints the component, type depending on what the type of the tamk is.
	 * @param g the graphics context
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		shield.paint(g);
		int x2 = 0;
		int y2 = 0;
		Composite comp = g2.getComposite();
		if (shield.getType() != Shield.NO_SHIELD) {
			x2 = (shieldSize.width - spriteWidth) / 2;
			y2 = (shieldSize.height - spriteHeight) / 2;
		}
		g2.translate(x2, y2);
		g2.setComposite(AlphaComposite.SrcOver);
		g2.drawImage(tankImage, 0, 0, null);
		g2.drawImage(barrelImage, barrelTrans, null);
		g2.translate(-x2, -y2);
		g2.setComposite(comp);
	}

	public Rectangle realBounds() {
		return newBounds;
	}

	/**
	 * increments the angle by the delta. also wraps around if the angle
	 * goes below 0 or above 180
	 * @param delta the increment
	 */
	public void setAngle(int delta) {
		//System.out.println("angle change");
		angle += delta;
		barrelTrans.rotate(-1 * Math.toRadians(delta), 0, barrelDiam / 2.0);
		if (angle > 180) {
			barrelTrans.rotate(
			        -1 * Math.toRadians(360 - angle),
			        0,
			        barrelDiam / 2.0);
			angle -= 180;
		}
		if (angle < 0) {
			barrelTrans.rotate(
			        Math.toRadians(180 + angle),
			        0,
			        barrelDiam / 2.0);
			angle += 180;
		}
		//repaint();
	}

	/**
	 * sets the current status(true or false)
	 * @param b boolean current status
	 */
	public void setCurrent(boolean b) {
		current = b;
	}

	/**
	 *
	 * @param x
	 * @param y
	 */
	public void setLocation(int x, int y) {
		oldBounds = boundsWithShield(newBounds);
		newBounds = new Rectangle(x, y, spriteWidth, spriteHeight);
	}

	/**
	 * increments the power of the Tank by delta. sets max at 100 and min at 0
	 * @param delta the increment
	 */
	public void setPower(int delta) {
		power += delta;
		if (power > 100)
			power = 100;
		if (power < 0)
			power = 0;
	}

	/**
	 *
	 * @param s
	 */
	public void setShield(Shield s) {
		shield = s;
		int w = shield.getWidth();
		int h = shield.getHeight();
		shieldSize = new Dimension(w, h);
		oldBounds = boundsWithShield(newBounds);
	}

	/**
	 * sets the type of this tank
	 * @param t the type
	 */
	private void setType(int t) {
		tankType = t;
		try {
			initImage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @return
	 */
	public String toString() {
		return ("Tank for " + owner);
	}

	public void update(int delay) {
	}
}
