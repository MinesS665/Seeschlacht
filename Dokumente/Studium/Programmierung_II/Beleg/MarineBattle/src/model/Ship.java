package model;

public class Ship {
	
	public Coordinates pos;
	public Coordinates secPos;
	public boolean isSelected;
	public boolean isSunken = false;
	private Player captain;

	public Ship (Player captain, Coordinates pos) {
		this.captain = captain;
		this.pos = pos;
		secPos = pos.addValue(0, 1);
		isSelected = false;
	}
	
	//Überladener Konstruktor
	public Ship (Player captain, Coordinates pos, Coordinates secPos) {
		this.captain = captain;
		this.pos = pos;
		this.secPos = secPos;
		isSelected = false;
		
		if (!this.secPos.isClose(this.pos)) this.secPos = pos.addValue(0, 1);
		
	}

	@Override
	public String toString() {
		return "Ship [pos=" + pos + ", isSelected=" + isSelected + ", isSunken=" + isSunken + ", captain=" + captain
				+ "]";
	}
	
}
