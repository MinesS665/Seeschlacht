package model;

public class Ship {
	
	public Coordinates pos;
	public Coordinates secPos;
	public boolean isSelected;
	public boolean isSunken = false;

	public Ship (Coordinates pos) {

		this.pos = pos;
		secPos = pos.addValue(0, 1);
		isSelected = false;
	}
	
	//Überladener Konstruktor
	public Ship (Coordinates pos, Coordinates secPos) {
		this.pos = pos;
		this.secPos = secPos;
		isSelected = false;
		
		if (!this.secPos.isClose(this.pos)) this.secPos = pos.addValue(0, 1);
		
	}

	@Override
	public String toString() {
		return "Ship [pos=" + pos + ", isSelected=" + isSelected + ", isSunken=" + isSunken + "]";
	}
	
}
