package jscorch.sprite.weapon;

import jscorch.ActionArea;
import jscorch.sprite.Tank;
import jscorch.sprite.explosion.Explosion;
import jscorch.utilities.Physics;
import jscorch.utilities.Trajectory;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;


/**
 * <p>Title: JScorch</p>
 * <p>Description: Scorched Earth for Java</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: HK Creations</p>
 * @author Andrew Cooper
 * @version 1.0
 * superclass for missile weapons; weapons which move along a projectile path.
 */
public abstract class Missile extends Weapon { //@todo fix missile appearance location
	//motion vars
	Trajectory path;
	private boolean clearedShield;
	Point curLoc;
	Point lastLoc;

	//size vars
	int expType = 0;

	//painting vars
	private BufferedImage image;
	Color color = Color.white;
	BasicStroke outline = new BasicStroke(1f);

	/**
	 * initializes Missile global variables.
	 * @param own the owning Tank
	 * @param startLoc starting location of the Missile
	 * @param angle the intial angle of the Missile
	 * @param power the power of the Missile
	 */
	public Missile(Tank own, Point startLoc, int angle, int power) {
		super(own.getContainer(), own);
		spriteWidth = 3;
		spriteHeight = 3;
		startLoc.translate(-spriteWidth, -spriteHeight);

		//create start & velocity vectors
		double velX = power / 100.0 * Physics.getVScale() * Math.cos(Math.toRadians(angle));
		double velY = power / 100.0 * Physics.getVScale() * Math.sin(Math.toRadians(angle));
		Point2D.Double vel = new Point2D.Double(velX, velY);

		//create a new projectile motion path
		path = new Trajectory(vel, startLoc, container.getBounds());
		lastLoc = new Point();
		curLoc = path.getPositionAtTime(0);

		//set up imaging
		image = new BufferedImage(spriteWidth, spriteHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.setPaint(Color.black);
		g2.fillOval(0, 0, spriteWidth, spriteHeight);
		g2.setPaint(color);
		g2.fillOval(1, 1, spriteWidth - 2, spriteHeight - 2);

		//set boundary variables
		newBounds = new Rectangle(curLoc, new Dimension(spriteWidth, spriteHeight));
		oldBounds = new Rectangle(newBounds);
	}

	/**
	 * called once per frame, gets the new position of the Missile and
	 * makes sure the position is still valid
	 * @param step the time for one frame to happen
	 */
	public void update(int step) {
		int inc = 2;
		for (int i = 1; i <= step; i += inc) {
			lastLoc.x = curLoc.x;
			lastLoc.y = curLoc.y;
			curLoc = path.getPositionAtTime(inc);
			if (curLoc != null) {
				if (!getContainer().pointIsValid(curLoc)) {
					if (clearedShield) {
						curLoc = lastLoc;
						explode();
						getContainer().removeSpriteFromLayer(this, ActionArea.WEAPON_LAYER);
						break;
					} else {
						if (owner.getCenter().distance(curLoc) > owner.getShield().getRadius()) {
							clearedShield = true;
						}
					}
				} else {
					if (!clearedShield) {
						if (owner.getCenter().distance(curLoc) > owner.getShield().getRadius()) {
							clearedShield = true;
						}
					}
				}
				if (i + inc > step) {
					curLoc.translate(-spriteWidth / 2, -spriteHeight / 2);
					oldBounds = new Rectangle(newBounds);
					newBounds.setLocation(curLoc);
				}
			} else {
				getContainer().removeSpriteFromLayer(this, ActionArea.WEAPON_LAYER);
				break;
			}
		}
	}

	/**
	 * happens when a Missile reaches an invalid location and thus explodes
	 */
	protected void explode() {
		container.addSpriteToLayer(Explosion.newExplosion(expType, owner, curLoc), ActionArea.EXPLOSION_LAYER);
	}

	/**
	 * overridden method, paints the Missile in the given praphics context
	 * @param g the graphics context
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(image, 0, 0, null);
	}
}

