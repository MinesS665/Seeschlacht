package model;

public class Ship {
	
	private int xPos;
	private int yPos;
	private boolean isSelected;
	private Player captain;

	public Ship (Player captain, int xPos, int yPos) {
		this.captain = captain;
		this.xPos = xPos;
		this.yPos = yPos;
		isSelected = false;
	}
}
