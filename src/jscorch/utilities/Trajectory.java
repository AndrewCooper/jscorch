package jscorch.utilities;

import jscorch.Preferences;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * A utility class providing a mechanism for a projectile path influenced by both
 * gravity and wind, as provided by the Physics class. The formulae used by this class
 * may seem strange because of the reversed coordinate system to the typical way
 * of looking at things. In this coordinate system, the vertical coordinate
 * associated with the ground is actually the maxiumum vertical coordinate of the
 * window. Therefore, the formula actually corresponds to a parabola starting at
 * the starting point, going down, and then going back up to the vertical max.
 */
public class Trajectory {
	private Point2D.Double vel;
//	Point2D.Double curDouble;
//	Point2D.Double oldDouble;
//	Point2D.Double deltaDouble;
	private Point2D.Double start;
	private Point2D.Double cur;
	private Point2D.Double old;
	private Rectangle bounds;
	private double xCur;
	private double yCur;
	double deltaX;
	double deltaY;
	private double time = 0;
	boolean rising;

	/**
	 * Creates on instance of a Trajectory using the Double point v as containing
	 * the x and y components of velocity, the point s as a starting point, and
	 * being contained within the given rectangle.
	 * <br><br>
	 * @param v double component representation of velocity
	 * @param s starting location
	 * @param b bounding rectangle for the trajectory.
	 */
	public Trajectory(Point2D.Double v, Point s, Rectangle b) {
		vel = v;
		start = new Point2D.Double(s.x, s.y);
		bounds = b;

		cur = new Point2D.Double();
		old = new Point2D.Double();
		rising = true;
	}

	public boolean isRising() {
		return rising;
	}

	/**
	 * uses Physics mechanics formulas to calculate the position along this trajectory at a given time.
	 * See the note above for more info on these formulas or read the in-line comments.
	 * @param step the time elapsed since the starting point
	 * @return the current Point location
	 */
	public Point getPositionAtTime(double step) {
		time += step / (.2 * 1000.0);
		//calculate the coordinates using d(t) = d(0) + V*t + .5*a*t^2
		//x coord- a = wind
		xCur = (start.x + vel.x * time + .5 * (.075) * Physics.wind * time * time);
		//y coord- a = G
		yCur = (start.y - vel.y * time + .5 * Physics.gravity * time * time);
		old.x = cur.x;
		old.y = cur.y;
		cur.x = xCur;
		cur.y = yCur;
		if (xCur < bounds.x || xCur > (bounds.x + bounds.width))
			doWallCollision();
		if (yCur < bounds.y)
			doCeilingCollision();
		if (old.y < cur.y)
			rising = false;
		else
			rising = true;
		return new Point((int) cur.x, (int) cur.y);
	}

	private void doWallCollision() {
		double vx,vy;
		//v(t) = v(0) + a*t
		vx = vel.x + (.075) * Physics.wind * time;
		vy = vel.y - Physics.gravity * time;
		switch (Preferences.WALL_TYPE) {
			case GameConstants.WALL_OPEN:
				int x = Preferences.PHYSICS_BORDERS_EXTEND;
				if ((cur.x < -1 * x) || (cur.x - bounds.width > x))
					cur = null;
				break;
			case GameConstants.WALL_RUBBER:
				if (cur.x >= (bounds.width))
					cur.x = (bounds.width - 1);
				else
					cur.x = 0;
				start = new Point2D.Double(cur.x, cur.y);
				vel.x = -1 * vx;
				vel.y = vy;
				time = 0;
				break;
			case GameConstants.WALL_WRAPPING:
				if (xCur < 0)
					cur.x = cur.x + bounds.width;
				if (xCur > (bounds.width))
					cur.x = cur.x - bounds.width;
				start = new Point2D.Double(cur.x, cur.y);
				vel.x = vx;
				vel.y = vy;
				time = 0;
				break;
		}
	}

	private void doCeilingCollision() {
		switch (Preferences.WALL_TYPE) {
			case GameConstants.WALL_RUBBER:
				//V^2 = Vo^2 + 2ad
				double vx = vel.x + (.075) * Physics.wind * time;
				double vy = vel.y - Physics.gravity * time;
				cur.y = 0;
				start = new Point2D.Double(cur.x, cur.y);
				vel.x = vx;
				vel.y = -1 * vy;
				time = 0;
				break;
		}
	}

	/**
	 * returns velocity as a Point2D.Double (kinda like a vector)
	 * @return velocity in a vector-like form
	 */
	public Point2D.Double getVelocity() {
		double vx = vel.x + (.075) * Physics.wind * time;
		double vy = vel.y + Physics.gravity * time;
		return new Point2D.Double(vx, vy);
	}
}