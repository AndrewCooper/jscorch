package jscorch;

import jscorch.sprite.Tank;

import java.awt.*;

import java.io.IOException;


/**
 * A jScorch instance connects upper-level objects together so that they may know about each other.
 */
public class jScorch {
	private Player[] players;
	private GameWindow gw;

	/**
	 * Where all the magic starts, this method takes a string array of possible arguments to the program.
	 * @param args Any possible arguments to the game.
	 */
	public static void main(String[] args) {
		new jScorch();
	}

	public jScorch() {
		players = new Player[5];
		players[0] = new Player("Joe", Tank.TYPE_A, Preferences.tankColors[0]);
		players[1] = new Player("Bob", Tank.TYPE_A, Preferences.tankColors[1]);
		players[2] = new Player("Hank", Tank.TYPE_A, Preferences.tankColors[2]);
		players[3] = new Player("George", Tank.TYPE_A, Preferences.tankColors[3]);
		players[4] = new Player("Henry", Tank.TYPE_A, Preferences.tankColors[4]);
		try {
			gw = new GameWindow(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Getter method used to access the global list of players.
	 * @return Array of Player objects
	 */
	public Player[] getPlayers() {
		return players;
	}

	/**
	 * Setter method used to externally set the list of players.
	 * @param players An array of players created somewhere else.
	 */
	public void setPlayers(Player[] players) {
		this.players = players;
	}
}
