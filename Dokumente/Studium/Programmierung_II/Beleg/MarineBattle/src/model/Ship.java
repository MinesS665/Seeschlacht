package model;

public class Ship {
	
	private Coordinates pos;
	private boolean isSelected;
	private Player captain;

	public Ship (Player captain, Coordinates pos) {
		this.captain = captain;
		this.pos = pos;
		isSelected = false;
	}
}
