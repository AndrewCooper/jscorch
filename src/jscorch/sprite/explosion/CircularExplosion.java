package jscorch.sprite.explosion;

import jscorch.ActionArea;
import jscorch.sprite.Tank;
import jscorch.sprite.weapon.Weapon;
import jscorch.utilities.RoundGradientPaint;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: acooper
 * Date: Jul 10, 2003
 * Time: 8:25:41 PM
 * To change this template use Options | File Templates.
 */
public class CircularExplosion extends Explosion {
	protected int MAXRAD = 10;
	protected int MINRAD = 0;
	protected int SPEED = 2;
	protected int rad;
	protected boolean expanding;
	protected int POWER;
	protected Paint gradPaint;

	protected int shockwaveSpeed;
	protected Paint shockPaint;

	public CircularExplosion(Tank own, int t, Point loc) {
		super(own);
		rad = MINRAD;
		expanding = true;
		type = t;
		switch (t) {
			case Weapon.BABY_MISSILE:
				SPEED = 2; //how fast it expands and contracts
				POWER = 50; //how much damage a direct hit will do, %
				MINRAD = 0; //the smallest radius this can have
				MAXRAD = 10; //the biggest radius this can have
				break;
			case Weapon.NORMAL_MISSILE:
			case Weapon.LEAP_FROG:
				SPEED = 2;
				POWER = 75;
				MINRAD = 0;
				MAXRAD = 14;
				break;
			case Weapon.BABY_NUKE:
			case Weapon.MIRV:
			case Weapon.DEATH_HEAD:
			case Weapon.FUNKY_BOMB:
				SPEED = 2;
				POWER = 100;
				MINRAD = 0;
				MAXRAD = 24;
				break;
			case Weapon.NUKE:
				SPEED = 2;
				POWER = 125;
				MINRAD = 0;
				MAXRAD = 30;
				break;
			default:
				SPEED = 0;
				POWER = 0;
				MINRAD = 0;
				MAXRAD = 0;
				break;
		}
		shockwaveSpeed = SPEED * 2;
		newBounds = new Rectangle(loc.x, loc.y, rad * 2, rad * 2);
		oldBounds = new Rectangle(newBounds);
		gradPaint = Color.orange;
		shockPaint = Color.white;
	}

	/**
	 *
	 * @param g
	 */
	public void paint(Graphics g) {
//		g.setColor(Color.ORANGE);
//		g.fillOval(0, 0, rad * 2, rad * 2);
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(gradPaint);
		g2.fillOval(0, 0, rad * 2 - 1, rad * 2 - 1);
//		g2.setPaint(Color.WHITE);
//		g2.drawOval(0, 0, rad * 2 - 1, rad * 2 - 1);
	}

	/**
	 * @param delay
	 */
	public void update(int delay) {
		oldBounds = newBounds;
		if (expanding) {
//			System.out.println("expanding:"+id);
			rad += SPEED;
			if (rad == MAXRAD)
				expanding = false;
			newBounds = new Rectangle(oldBounds.x - SPEED, oldBounds.y - SPEED, rad * 2, rad * 2);
		} else {
//			System.out.println("contracting:"+id);
			rad -= SPEED;
			if (rad <= MINRAD)
				container.removeSpriteFromLayer(this, ActionArea.EXPLOSION_LAYER);
			newBounds = new Rectangle(oldBounds.x + SPEED, oldBounds.y + SPEED, rad * 2, rad * 2);
		}
		gradPaint = new RoundGradientPaint(rad, rad, Color.white, 3 * rad / 4, Color.orange);
	}
}
