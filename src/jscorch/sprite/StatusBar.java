package jscorch.sprite;

import jscorch.ActionArea;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: acooper
 * Date: Jul 20, 2003
 * Time: 4:16:24 PM
 * To change this template use Options | File Templates.
 */
public class StatusBar extends Sprite {
	private final String powerString = "Power: ";
	private final String angleString = "Angle: ";
	private int power;
	private int angle;
	private String weapon;
    private int repaintsLeft;

	public StatusBar(ActionArea a) {
		super(a);
		needsRepaint = true;
		Dimension d = a.getSize();
		newBounds = new Rectangle(25, d.height - 20, d.width - 50, 20);
		oldBounds = new Rectangle(newBounds);
		power = 500;
		angle = 45;
	}

	/**
	 *
	 * @param g
	 */
	public synchronized void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(new Color(1f, 1f, 1f, .5f));
		g2.fillRect(newBounds.x, newBounds.y, newBounds.width, newBounds.height);
		g2.setPaint(Color.black);
		g2.drawString(powerString + power, newBounds.x + 5, newBounds.y + 15);
		g2.drawString(angleString + angle, newBounds.x + 80, newBounds.y + 15);
		g2.drawString(weapon, newBounds.x + 320, newBounds.y + 15);
		if (repaintsLeft > 0) {
			repaintsLeft--;
        } else {
            needsRepaint = false;
        }
	}

	public void setPower(int p) {
		this.power = p;
		needsRepaint = true;
        repaintsLeft = 1;
	}

	public void setAngle(int a) {
		this.angle = a;
		needsRepaint = true;
        repaintsLeft = 1;
	}

	public void setWeapon(String s) {
		weapon = s;
		needsRepaint = true;
        repaintsLeft = 1;
	}

	/**
	 * @param delay
	 */
	public void update(int delay) {
	}
}
