package jscorch.sprite;

import jscorch.ActionArea;

import java.awt.*;

/**
 * @author acooper
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * Created by IntelliJ IDEA.
 * User: acooper
 * Date: Jul 2, 2003
 * Time: 9:11:40 PM
 * To change this template use Options | File Templates.
 */
public abstract class Sprite {
	protected ActionArea container;

	protected Rectangle newBounds;
	protected Rectangle oldBounds;

	protected int spriteWidth;
	protected int spriteHeight;

	protected boolean needsRepaint;

	protected Sprite(ActionArea a) {
		container = a;
		needsRepaint = true;
	}

	public ActionArea getContainer() {
		return container;
	}

	/**
	 *
	 * @return
	 */
	public Rectangle newBounds() {
		return newBounds;
	}

	/**
	 *
	 * @return
	 */
	public Rectangle oldBounds() {
		return oldBounds;
	}

	public Point getCenter() {
		Rectangle r = this.newBounds();
		int x = r.x + r.width / 2;
		int y = r.y + r.height / 2;
		return new Point(x, y);
	}

	/**
	 *
	 * @param g
	 */
	public abstract void paint(Graphics g);


	/**
	 * @param delay
	 */
	public abstract void update(int delay);

	public boolean needsRepaint() {
		return needsRepaint;
	}

	public void setNeedsRepaint(boolean b) {
		this.needsRepaint = b;
	}

	void repaint() {
		needsRepaint = true;
	}
}
