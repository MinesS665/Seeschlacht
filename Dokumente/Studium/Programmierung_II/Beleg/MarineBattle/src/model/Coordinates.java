package model;

public class Coordinates {
	int x;
	int y;
	
	public Coordinates (int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public Coordinates LeftOf() {
		return new Coordinates(x-1,y);
	}
	public Coordinates RightOf() {
		return new Coordinates(x+1,y);
	}
	public Coordinates TopOf() {
		return new Coordinates(x,y-1);
	}
	public Coordinates BottomOf() {
		return new Coordinates(x,y+1);
	}

	@Override
	public String toString() {
		return "Coordinates [x=" + x + ", y=" + y + "]";
	}
	
	
	
}
