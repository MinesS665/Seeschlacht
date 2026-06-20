package model;

import java.awt.Color;

public class Player {

	private static int playerCount = 0;
	private int id;
	private String name;
	private Color colour;

	public int movedSteps;
	public int aShips = 5;
	public Coordinates posHarbour;
	public Ship[] ships = new Ship[aShips];
	public boolean itsTurn;
	private boolean isDefeated = false;
	
	//Konstruktor
	public Player(String name, Color colour) {
		playerCount++;
		this.id = playerCount;
		this.name = name;
		this.colour = colour;
	}
	//Konstruktor fürs Laden von Spielständen
	public Player(int id, String name, Color colour) {
	    this.id = id;          
	    this.name = name;
	    this.colour = colour;
	    ensurePlayerCount(id);
	}
	
	//Ausschieden handlen und falls Spieler "das erste Mal ausscheidet" true zurückgeben
	public boolean playerDefeat() {

		int sunkenShips = 0;
		
		for(Ship s : ships) {
			if (s != null && s.isSunken == true) sunkenShips++;
			
		}
		
		if (sunkenShips == aShips && isDefeated == false) {
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
		playerCount -= lowerBy;
	}
	
	public static void ensurePlayerCount(int id) {
		if (playerCount < id) playerCount = id;
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

	public int getId() {
		return id;
	}

	public void setSteps(int plus) {
		movedSteps += plus;
	}
	
	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + ", colour=" + colour + ", posHarbour=" + posHarbour + ", itsTurn="
				+ itsTurn + "]";
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;
	    Player other = (Player) obj;
	    return this.id == other.id; // Spieler sind gleich, wenn ihre id gleich ist
	}
}
