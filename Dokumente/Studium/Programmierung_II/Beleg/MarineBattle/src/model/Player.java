package model;

import java.awt.Color;

public class Player {

	private static int playerCount = 0;
	private int ID;
	private String name;
	private Color colour;

	private Coordinates posHabour;
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

	public Color getColour() {
		return colour;
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return ID;
	}

	public void setPosHabour(Coordinates posHabour) {
		this.posHabour = posHabour;
	}

	public Coordinates getPosHabour() {
		return posHabour;
	}
	
	
	
}
