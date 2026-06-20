package model;

import java.awt.Point;

/**
 * Einfache Datenklasse für 2D-Koordinaten in Karten-Einheiten.
 *
 * <p>Die Klasse kapselt x/y-Koordinaten, bietet Hilfsmethoden für Addition,
 * Näheprüfung und Konvertierung von AWT-Points (Pixel) in Kartenkoordinaten.
 */
public class Coordinates {
    
	int x;
	int y;
    
	/**
	 * Erzeugt eine neue Coordinates-Instanz.
	 *
	 * @param x X-Koordinate in Karten-Einheiten
	 * @param y Y-Koordinate in Karten-Einheiten
	 */
	public Coordinates (int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Liefert die X-Koordinate.
	 * @return X-Wert
	 */
	public int getX() {
		return x;
	}

	/**
	 * Liefert die Y-Koordinate.
	 * @return Y-Wert
	 */
	public int getY() {
		return y;
	}

	/**
	 * Erzeugt eine neue Coordinates-Instanz mit addierten Werten.
	 * @param x1 Delta-X
	 * @param y1 Delta-Y
	 * @return neue Coordinates um die angegebenen Deltas versetzt
	 */
	public Coordinates addValue (int x1, int y1) {
		return new Coordinates(x+x1, y+y1);
	}

	@Override
	/**
	 * Vergleicht zwei Coordinates-Objekte auf Gleichheit (x und y müssen übereinstimmen).
	 */
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
    
	/**
	 * Prüft, ob zwei Koordinaten maximal um 1 Feld in X und Y voneinander abweichen.
	 *
	 * @param obj anderes Coordinates-Objekt
	 * @return true, wenn die Koordinaten nahe beieinander liegen
	 */
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
    
	/**
	 * Wandelt einen AWT-Point (Pixel) in Kartenkoordinaten um, anhand der Kachelgröße.
	 *
	 * @param p AWT Point in Pixeln
	 * @param tileSize Größe einer Kachel in Pixeln
	 * @return abgebildete Coordinates in Karten-Einheiten
	 */
	public static Coordinates toCoordinates(Point p, int tileSize) {
		return new Coordinates(p.x/tileSize, p.y/tileSize);
	}
    
}
