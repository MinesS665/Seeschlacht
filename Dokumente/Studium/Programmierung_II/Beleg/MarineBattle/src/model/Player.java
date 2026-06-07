package model;

import java.awt.Color;

public class Player {

	private static int playerCount = 0;
	private int ID;
	private String name;
	private Color colour;

	private Coordinates posHabour;
	boolean itsTurn;
	private boolean isDefeated = false;
	public int movedSteps;
	public int aShips = 5;
	public Ship[] ships = new Ship[aShips];
	
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

	@Override
	public String toString() {
		return "Player [ID=" + ID + ", name=" + name + ", colour=" + colour + ", posHabour=" + posHabour + ", itsTurn="
				+ itsTurn + "]";
	}

	public void setSteps(int plus) {
		
		movedSteps += plus;
		
	}
	
	public void playerDefeat() {
		
		int sunkenShips = 0;
		
		for(Ship s : ships) {
			if (s.isSunken == true) sunkenShips++;
		}
		
		if (sunkenShips == aShips) isDefeated = true;
	}

	public boolean isDefeated() {
		return isDefeated;
	}
	
	
}
