package model;

public class Player {

	private static int playerCount = 0;
	private int ID;
	private String name;
	private String colour;
	private int xPosHabour;
	private int yPosHabour;
	boolean itsTurn;
	
	public Player(String name, String colour) {
		playerCount++;
		this.ID = playerCount;
		this.name = name;
		this.colour = colour;
	}
}
