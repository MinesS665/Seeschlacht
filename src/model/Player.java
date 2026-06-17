package model;

import java.awt.Color;

public class Player {

	private static int playerCount = 0;
	private int ID;
	private String name;
	private Color colour;

	public int movedSteps;
	public int aShips = 5;
	public Coordinates posHabour;
	public Ship[] ships = new Ship[aShips];
	public boolean itsTurn;
	private boolean isDefeated = false;
	
	//Konstruktor
	public Player(String name, Color colour) {
		playerCount++;
		this.ID = playerCount;
		this.name = name;
		this.colour = colour;
	}
	//Konstruktor fürs Laden von Spielständen
	public Player(int id, String name, Color colour) {
	    this.ID = id;          
	    this.name = name;
	    this.colour = colour;
	}
	
	//Ausschieden handlen und falls Spieler "das erste Mal ausscheidet" true zurückgeben
	public boolean playerDefeat() {

		
		int sunkenShips = 0;
		
		for(Ship s : ships) {
			if (s.isSunken == true) sunkenShips++;
		}
		
		if (sunkenShips == aShips-1 && isDefeated == false) {
			isDefeated = true;
			return true;
		} else {
			return false;
		}
	}

	public boolean isDefeated() {
		return isDefeated;
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

	public void setSteps(int plus) {
		movedSteps += plus;
	}
	
	@Override
	public String toString() {
		return "Player [ID=" + ID + ", name=" + name + ", colour=" + colour + ", posHabour=" + posHabour + ", itsTurn="
				+ itsTurn + "]";
	}

}
