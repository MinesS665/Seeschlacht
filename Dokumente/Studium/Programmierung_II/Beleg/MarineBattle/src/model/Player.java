package model;

import java.awt.Color;

public class Player {

	private static int playerCount = 0;
	private int ID;
	private String name;
	private Color colour;

	private int xPosHabour;
	private int yPosHabour;
	boolean itsTurn;
	
	public Player(String name, Color colour) {
		playerCount++;
		this.ID = playerCount;
		this.name = name;
		this.colour = colour;
	}
	
	public static void setPlayerCount(int lowerBy) {
		playerCount =- lowerBy;
	}

	public static int getPlayerCount() {
		return playerCount;
	}
}
