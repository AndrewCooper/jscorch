package jscorch.sprite.explosion;

/**
 * <p>Title: JScorch</p>
 * <p>Description: Scorched Earth for Java</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: HK Creations</p>
 * @author Andrew Cooper
 * @version 1.0
 */

import jscorch.ActionArea;
import jscorch.sprite.Sprite;
import jscorch.sprite.Tank;
import jscorch.sprite.weapon.Weapon;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.QuadCurve2D;

/**
 * an Explosion for a Funky Bomb. Also initializes the smaller explosions
 * connected with the Funky Bomb.
 */
public class FunkyBombExplosion extends CircularExplosion {
	private FunkyBombPanel fbPanel;

	/**
	 * creates a new Explosion, passing along the owner and location for initialization
	 * Also, because a Funky Bomb has 5 other smaller explosions associated with it,
	 * this sets things up to get those created.
	 */
	public FunkyBombExplosion(Tank own, int type, Point loc) {
		super(own, type, loc);

		Rectangle r = container.getBounds();
		fbPanel = new FunkyBombPanel(own, r, loc);
		container.addSpriteToLayer(fbPanel, ActionArea.TRAIL_LAYER);
	}

	public void update(int step) {
		super.update(step);
		if (rad <= MINRAD) {
			container.removeSpriteFromLayer(fbPanel, ActionArea.TRAIL_LAYER);
		}
	}
}

class FunkyBombPanel extends Sprite {
	private int numCurves = 5;
	GeneralPath path = new GeneralPath();
	BasicStroke in = new BasicStroke(1.5f);

	/**
	 * Companion class to the Funky Bomb, this panel provides a canvas for drawing
	 * the projectile paths to the other 5 explosions of the Funky Bomb.
	 * @param owner the owner of the Explosions
	 * @param r the rectangle for the bounds of this canvas
	 * @param loc the starting point for all the paths
	 * creates an instance of this canvas for adding Explosions for owner to c, drawing
	 * with r, and starting at loc
	 */
	public FunkyBombPanel(Tank owner, Rectangle r, Point loc) {
		super(owner.getContainer());
		newBounds = r;

		int expType = Weapon.NORMAL_MISSILE;
		int x0 = loc.x,y0 = loc.y,x1,y1,x2,y2,x3,y3;
		double width, height;
		int j;
		double xt,yt,t;
		Point p;
		QuadCurve2D.Double curve;

		for (int i = 0; i < numCurves; i++) {
			do {
				x3 = (int) (Math.random() * 400 - 200) + x0;
			} while (x3 < 0 || x3 >= r.width);
			j = 0;
			p = new Point(x3, j);
			while (j < r.height && container.pointIsValid(p)) {
				p.y = ++j;
			}
			y3 = j;
			width = (x3 - x0);
			x1 = (int) (width / 3 + x0);
			y1 = (y0 / 3);
			x2 = (int) (2 * width / 3 + x0);
			y2 = (y3 / 3);
			xt = x0;
			yt = y0;
			t = 0;
			p = new Point((int) xt, (int) yt);
			do {
				xt = x0 * Math.pow(1 - t, 3) + 3 * x1 * t * Math.pow(1 - t, 2) + 3 * x2 * Math.pow(t, 2) * (1 - t) + x3 * Math.pow(t, 3);
				yt = y0 * Math.pow(1 - t, 3) + 3 * y1 * t * Math.pow(1 - t, 2) + 3 * y2 * Math.pow(t, 2) * (1 - t) + y3 * Math.pow(t, 3);
				p.x = (int) xt;
				p.y = (int) yt;
				t += .005;
			} while (container.pointIsValid(p) && t <= 1);
			width = (xt - x0);
			height = y0 * (1 - t);
			curve = new QuadCurve2D.Double(x0, y0, width / 2.0 + x0, height, xt, yt);
			path.append(curve, false);
			container.addSpriteToLayer(Explosion.newExplosion(expType, owner, new Point((int) xt, (int) yt)), ActionArea.EXPLOSION_LAYER);
		}
		newBounds = path.getBounds();
		oldBounds = new Rectangle(newBounds);
	}

	/**
	 *
	 * @param g
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(in);
		g2.setPaint(Color.white);
		g2.draw(path);
	}

	/**
	 * @param delay
	 */
	public void update(int delay) {
	}
}