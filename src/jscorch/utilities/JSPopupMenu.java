package jscorch.utilities;

import jscorch.Player;
import jscorch.ActionArea;
import jscorch.Preferences;
import jscorch.sprite.weapon.Weapon;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: acooper
 * Date: Jul 23, 2003
 * Time: 11:47:06 PM
 * To change this template use Options | File Templates.
 */
public class JSPopupMenu {
	ActionArea container;

	public static final int WEAPONS = 0;
	public static final int SHEILDS = 1;
	public static final int GUIDANCES = 2;
	public static final int MISC = 3;

	JPopupMenu weapMenu;
	JPopupMenu shieldMenu;
	JPopupMenu guidanceMenu;
	JPopupMenu miscMenu;

	Player player;
	Point loc;

	public JSPopupMenu(ActionArea a) {
		container = a;
	}

	public void initData(Player p) {
		player = p;
		Rectangle r = p.getTank().newBounds();
		loc = new Point(r.x+r.width/2,r.y+r.height/2);
		weapMenu = new JPopupMenu("Weapons");
		String[] s = p.getWeaponLocker().checkLocker();
		for (int i = 0; i < s.length; i++) {
			JMenuItem mi = new JMenuItem(s[i]);
			final int j = i;
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					changeWeapon(JSPopupMenu.WEAPONS,j);
				}
			});
			mi.setFont(Preferences.FONT);
			weapMenu.add(mi);
		}
	}

	public void changeWeapon(int m, int i) {
		switch(m) {
			case WEAPONS:
				player.getWeaponLocker().setCurrentOfInventory(i);
				container.getStatusBar().setWeapon(player.getWeaponLocker().toString());
				break;
			case SHEILDS:
				break;
			case GUIDANCES:
				break;
			case MISC:
				break;
			default:
				break;
		}
	}
	public void showMenu(int m) {
		switch(m) {
			case WEAPONS:
				weapMenu.show(container,loc.x,loc.y);
				break;
			case SHEILDS:
				break;
			case GUIDANCES:
				break;
			case MISC:
				break;
			default:
				break;
		}
	}
}
