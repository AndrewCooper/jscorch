package jscorch;

import jscorch.utilities.JSKeyAdapter;

import javax.swing.*;
import java.io.IOException;
import java.awt.*;

/**
 * <p>A GameWindow is simply the JFrame subclass containing the ActionArea instance.
 * More may eventually be placed in this layer.</p>
 */
public class GameWindow extends JFrame {
	private jScorch game;
	private ActionArea actionArea;

	/**
	 * This standard constructor takes an instance of the jScorch game to be passed on to its children.
	 * @param g The instance of this game
	 */
	public GameWindow(jScorch g) throws IOException {
		game = g;
		actionArea = new ActionArea(g);
		this.getContentPane().add(actionArea);
		this.pack();
		Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension w = this.getSize();
		this.setLocation((s.width-w.width)/2,(s.height-w.height)/2);
		this.setResizable(false);
		this.setVisible(true);
		actionArea.initTanks();
		this.addKeyListener(new JSKeyAdapter(actionArea));
	}
}