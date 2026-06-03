package model;

public class Ship {
	
	private Coordinates pos;
	private Coordinates secPos;
	public boolean isSelected;
	public boolean isSunken = false;
	private Player captain;

	public Ship (Player captain, Coordinates pos) {
		this.captain = captain;
		this.pos = pos;
		secPos = pos.addValue(0, 1);
		isSelected = false;
	}

	public Coordinates getPos() {
		return pos;
	}
	public Coordinates getSecPos() {
		return secPos;
	}

	@Override
	public String toString() {
		return "Ship [pos=" + pos + ", isSelected=" + isSelected + ", isSunken=" + isSunken + ", captain=" + captain
				+ "]";
	}

	public void setPos(Coordinates pos) {
		
		this.pos = pos;
	}

	public void setSecPos(Coordinates secPos) {
		this.secPos = secPos;
	}
	
}
