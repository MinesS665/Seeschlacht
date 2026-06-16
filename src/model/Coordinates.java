package model;

import java.awt.Point;

public class Coordinates {
	
	int x;
	int y;
	
	//Konstruktor
	public Coordinates (int x, int y) {
		this.x = x;
		this.y = y;
	}

	//Getter
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	//Koordinaten manipulieren
	public Coordinates addValue (int x1, int y1) {
		return new Coordinates(x+x1, y+y1);
	}

	@Override
	//Gleichheit prüfen
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Coordinates other = (Coordinates) obj;
		return x == other.x && y == other.y;
	}
	
	//Prüfen ob zwei Koordinaten nah beieinander sind
	public boolean isClose(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Coordinates other = (Coordinates) obj;
		
		if (Math.abs(x-other.x) <= 1 && Math.abs(y-other.y) <= 1) return true;
		else return false;
	}

	@Override
	public String toString() {
		return "Coordinates [x=" + x + ", y=" + y + "]";
	}
	
	//Umwandlung von Points in Coordinates
	public static Coordinates toCoordinates(Point p, int tileSize) {
		return new Coordinates(p.x/tileSize, p.y/tileSize);
	}
	
}
