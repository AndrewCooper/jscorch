package jscorch;

import jscorch.sprite.Sprite;
import jscorch.sprite.StatusBar;
import jscorch.sprite.Tank;
import jscorch.utilities.JSPopupMenu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;

/**
 * <p>The ActionArea is where all of the moving action for a game of jScorch is displayed.
 * As such, an actionArea instance keeps track of all the sprites, as well as keeping track of the animation timer.</p>
 *
 */
public class ActionArea extends JPanel {
	/**
	 * The layer which weapon-level sprites are placed in
	 */
	public static final int WEAPON_LAYER = 0;
	/**
	 * The layer which explosion-level sprites are placed in
	 */
	public static final int EXPLOSION_LAYER = 1;
	/**
	 * The layer which tank-level sprites are placed in.
	 */
	public static final int TANK_LAYER = 2;
	/**
	 * The layer which trail-level sprites are placed in.
	 */ 
	public static final int TRAIL_LAYER = 3;

	private jScorch game;

	private Vector weaponLayer; //moving things, like weapons
	private Vector explosionLayer; //nonmoving, but changing, like explosions
	private Vector tankLayer; //nonmoving and nonchanging, like tanks
	private Vector trailLayer;
	private BufferedImage landscape;
	private BufferedImage weaponOffscreen;
	private BufferedImage explosionOffscreen;
	private BufferedImage tankOffscreen;
	private BufferedImage trailOffscreen;
	private Rectangle updateRect;
	private Dimension size;
	private Timer animTimer;
	private final int ANIM_DELAY = 1000 / 20; //(ms/s) / (f/s) = (ms/f)

	private Tank curTank;
	private int curID;
	private boolean dirtyTrails;
	private StatusBar statusBar;
	private JSPopupMenu menus;

	/**
	 * @param g The instance of this game
	 * @throws IOException
	 */
	public ActionArea(jScorch g) throws IOException {
		game = g;

		weaponLayer = new Vector();
		explosionLayer = new Vector();
		tankLayer = new Vector();
		trailLayer = new Vector();

		landscape = ImageIO.read(jscorch.jScorch.class.getResource("images/landscape.png"));
		size = new Dimension(landscape.getWidth(), landscape.getHeight());
		weaponOffscreen = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB_PRE);
		explosionOffscreen = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB_PRE);
		tankOffscreen = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB_PRE);
		trailOffscreen = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB_PRE);

		animTimer = new Timer(ANIM_DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				preparePainting();
			}
		});
		this.setMinimumSize(size);
		this.setMaximumSize(size);
		this.setPreferredSize(size);

//		animTimer.start();
		updateRect = new Rectangle(size);
	}

	/**
	 * Drop tanks and initialize status bar when window initialization is complete.
	 */
	public void initTanks() {
		Player[] p = game.getPlayers();
		Tank t;
		for (int i = 0; i < p.length; i++) {
			int num = (int) (Math.random() * (p.length - i));
			Player temp = p[i];
			p[i] = p[num + i];
			p[num + i] = temp;
			t = p[i].getNewTank(this);
			int xloc = (int) (Math.random() * (size.width / p.length - 3 * t.realBounds().width / 2) + i * size.width / p.length);
			int yloc = 0;
			t.setLocation(xloc, yloc);
			addSpriteToLayer(t, TANK_LAYER);
			dropTank(t);
		}
		curID = 0;
		curTank = (Tank) (tankLayer.elementAt(curID));
		statusBar = new StatusBar(this);
		statusBar.setPower(curTank.getPower());
		statusBar.setAngle(curTank.getAngle());
		statusBar.setWeapon(curTank.getPlayer().getWeaponLocker().toString());
		menus = new JSPopupMenu(this);
		menus.initData(curTank.getPlayer());
	}

	/**
	 *
	 * @param layer Layer to add the sprite to.
	 * @param s Sprite to add
	 */
	public void addSpriteToLayer(Sprite s, int layer) {
		switch (layer) {
			case WEAPON_LAYER:
				weaponLayer.add(s);
				break;
			case EXPLOSION_LAYER:
				explosionLayer.add(s);
				break;
			case TANK_LAYER:
				tankLayer.add(s);
				break;
			case TRAIL_LAYER:
				trailLayer.add(s);
				dirtyTrails = true;
				break;
		}
		if (!animTimer.isRunning())
			animTimer.start();
	}

	private int dropTank(Tank t) {
		Point bl = new Point();
		Point bc = new Point();
		Point br = new Point();
		Rectangle r = t.realBounds();
		bl.y = bc.y = br.y = r.height;
		bl.x = r.x;
		bc.x = r.x + r.width / 2;
		br.x = r.x + r.width;
		int dropheight = 0;
		while ((landPointIsValid(bl) || landPointIsValid(br)) && landPointIsValid(bc)) {
			bl.translate(0, 1);
			br.translate(0, 1);
			bc.translate(0, 1);
			dropheight++;
		}
		t.setLocation(bl.x, bl.y - r.height);
		return dropheight;
	}

	/**
	 * Determines if a point in the world is valid for any object to be in. This takes into account shields, tanks, and land.
	 * @param p Point to check
	 * @return Result of the check
	 */
	public boolean pointIsValid(Point p) {
		Tank tmp;
		if (landPointIsValid(p)) {
			for (int i = 0; i < tankLayer.size(); i++) {
				tmp = (Tank) (tankLayer.elementAt(i));
				if (tmp.getShield().getType() != Shield.NO_SHIELD) {
					if (tmp.getCenter().distance(p) < tmp.getShield().getRadius()) {
						return false;
					}
				} else {
					if (tmp.checkCollision(p)) {
						return false;
					}
				}
			}
		} else {
			return false;
		}
		return true;
	}

	private boolean landPointIsValid(Point p) {
		try {
			int argb = landscape.getRGB(p.x, p.y);
			if (((argb & 0xFF000000)) == (0xFF000000)) {
				return false;
			}
		} catch (Exception e) {
			return true;
		}
		return true;
	}

	/**
	 * Returns the popup menus used for selecting weapons, shields, or accessories.
	 * @return The JSPopupMenu object
	 */
	public JSPopupMenu getMenus() {
		return menus;
	}

	private void preparePainting() {
		//updateRect = new Rectangle(0,0,landscape.getWidth(),landscape.getHeight());
		Sprite tmp;

		Graphics2D g2 = weaponOffscreen.createGraphics();
		g2.setComposite(AlphaComposite.Clear);
		g2.fillRect(0, 0, weaponOffscreen.getWidth(), weaponOffscreen.getHeight());
		g2.setComposite(AlphaComposite.SrcOver);
		for (int i = 0; i < weaponLayer.size(); i++) {
			tmp = (Sprite) (weaponLayer.elementAt(i));
			tmp.update(ANIM_DELAY);
			Rectangle ro = tmp.oldBounds();
			Rectangle rn = tmp.newBounds();
			if (updateRect == null) {
				updateRect = new Rectangle(rn);
				updateRect.add(ro);
			} else {
				updateRect.add(rn);
				updateRect.add(ro);
			}
			g2.translate(rn.x, rn.y);
			g2.setClip(0, 0, rn.width, rn.height);
			tmp.paint(g2);
			g2.setClip(null);
			g2.translate(-rn.x, -rn.y);
		}

		g2 = explosionOffscreen.createGraphics();
		g2.setComposite(AlphaComposite.Clear);
		g2.fillRect(0, 0, explosionOffscreen.getWidth(), explosionOffscreen.getHeight());
		g2.setComposite(AlphaComposite.SrcOver);
		for (int i = 0; i < explosionLayer.size(); i++) {
			tmp = (Sprite) (explosionLayer.elementAt(i));
			tmp.update(ANIM_DELAY);
			Rectangle ro = tmp.oldBounds();
			Rectangle rn = tmp.newBounds();
			if (updateRect == null) {
				updateRect = new Rectangle(rn);
				updateRect.add(ro);
			} else {
				updateRect.add(rn);
				updateRect.add(ro);
			}
			g2.translate(rn.x, rn.y);
			g2.setClip(0, 0, rn.width, rn.height);
			tmp.paint(g2);
			g2.setClip(null);
			g2.translate(-rn.x, -rn.y);
		}

		g2 = tankOffscreen.createGraphics();
		g2.setComposite(AlphaComposite.Src);
		for (int i = 0; i < tankLayer.size(); i++) {
			tmp = (Sprite) (tankLayer.elementAt(i));
			Rectangle rn = tmp.newBounds();
			if (updateRect == null) {
				updateRect = new Rectangle(rn);
			} else {
				updateRect.add(rn);
			}
			g2.translate(rn.x, rn.y);
			g2.setClip(0, 0, rn.width, rn.height);
			tmp.paint(g2);
			g2.setClip(null);
			g2.translate(-rn.x, -rn.y);
		}

		if (dirtyTrails) {
			g2 = trailOffscreen.createGraphics();
			g2.setComposite(AlphaComposite.Clear);
			g2.fillRect(0, 0, trailOffscreen.getWidth(), trailOffscreen.getHeight());
			g2.setComposite(AlphaComposite.SrcOver);
			for (int i = 0; i < trailLayer.size(); i++) {
				tmp = (Sprite) trailLayer.elementAt(i);
				updateRect.add(tmp.newBounds());
				g2.setClip(null);
				tmp.paint(g2);
			}
			dirtyTrails = false;
		}
		if (statusBar != null) {
			if (statusBar.needsRepaint()) {
				if (updateRect == null) {
					updateRect = new Rectangle(statusBar.newBounds());
				} else {
					updateRect.add(statusBar.newBounds());
				}
			} else {
				if (updateRect != null && updateRect.intersects(statusBar.newBounds())) {
					statusBar.setNeedsRepaint(true);
					updateRect.add(statusBar.newBounds());
				}
			}
		}
		repaint(updateRect);
	}

	/**
	 * Overridden repaint() method to increase update rectangle size for system updates.
	 * @param r The area to be updated by this repaint.
	 */
	public void repaint(Rectangle r) {
		updateRect = new Rectangle(this.getSize());
		super.repaint(r);
	}

	/**
	 * Paints this ActionArea in the specified Graphics Context
	 * @param g Context to paint into
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Rectangle r = g.getClipBounds();
		if (updateRect != null) {
			r = updateRect;
		}
		g.setColor(new Color(0, 204, 255));
		g.fillRect(r.x, r.y, r.width, r.height);
		g.drawImage(landscape, r.x, r.y, r.x + r.width, r.y + r.height, r.x, r.y, r.x + r.width, r.y + r.height, this);
		g.drawImage(trailOffscreen, r.x, r.y, r.x + r.width, r.y + r.height, r.x, r.y, r.x + r.width, r.y + r.height, this);
		g.drawImage(tankOffscreen, r.x, r.y, r.x + r.width, r.y + r.height, r.x, r.y, r.x + r.width, r.y + r.height, this);
		g.drawImage(explosionOffscreen, r.x, r.y, r.x + r.width, r.y + r.height, r.x, r.y, r.x + r.width, r.y + r.height, this);
		g.drawImage(weaponOffscreen, r.x, r.y, r.x + r.width, r.y + r.height, r.x, r.y, r.x + r.width, r.y + r.height, this);
		if (statusBar != null && (statusBar.needsRepaint() || updateRect == null)) {
			statusBar.paint(g);
		}
		updateRect = null;
	}

	/**
	 *
	 * @return Current Tank object
	 */
	public Tank getCurTank() {
		return curTank;
	}

	/**
	 *
	 * @return StatusBar object
	 */
	public StatusBar getStatusBar() {
		return statusBar;
	}

	/**
	 * Removes the specified Sprite from the specified layer.
	 * @param s Sprite to add
	 * @param layer Layer to add the Sprite into
	 */
	public void removeSpriteFromLayer(Sprite s, int layer) {
		switch (layer) {
			case WEAPON_LAYER:
				weaponLayer.remove(s);
				break;
			case EXPLOSION_LAYER:
				explosionLayer.remove(s);
				break;
			case TANK_LAYER:
				tankLayer.remove(s);
				break;
			case TRAIL_LAYER:
				trailLayer.remove(s);
				if (updateRect == null)
					updateRect = new Rectangle(s.newBounds());
				else
					updateRect.add(s.newBounds());
				dirtyTrails = true;
				break;
		}
		if ((weaponLayer.size() + explosionLayer.size() + tankLayer.size() + trailLayer.size()) == 0)
			animTimer.stop();
	}

	/**
	 * Method called when a tank fires its weapon.
	 */
	public void tankFire() {
		getCurTank().fire();
		curID = (curID + 1) % tankLayer.size();
		curTank = (Tank) (tankLayer.elementAt(curID));
		statusBar.setAngle(curTank.getAngle());
		statusBar.setPower(curTank.getPower());
		statusBar.setWeapon(curTank.getPlayer().getWeaponLocker().toString());
		menus.initData(curTank.getPlayer());
	}
}
