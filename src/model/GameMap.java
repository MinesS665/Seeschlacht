package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Repräsentiert die Spielkarte als Raster von {@link TileTyp}-Werten.
 *
 * <p>Die Map wird aus einem PNG geladen; die Pixel-Farben werden dabei analysiert
 * und in Wasser- bzw. Land-Typen übersetzt. Außerhalb der Karte wird standardmäßig
 * {@link TileTyp#LAND} zurückgegeben, damit Spielfiguren die Karte nicht verlassen.
 */
public class GameMap {
    
	private TileTyp[][] grid;
	private int width;
	private int height;
    
	/**
	 * Lädt eine Map aus einem Bild (PNG). Die Methode liest jedes Pixel und
	 * entscheidet anhand einer Farb-Toleranz, ob es Wasser oder Land darstellt.
	 *
	 * @param imagePath Pfad zur Bildressource im Klassenpfad (z. B. "/assets/map.png")
	 */
	public GameMap(String imagePath) {
		try {
            
			if (getClass().getResource(imagePath) == null) {
				System.err.println("Map konnte nicht geladen werden! Pfad falsch: " + imagePath);
				return;
			}
            
			//Lade PNG aus dem Assets-Ordner
			BufferedImage mapImg = ImageIO.read(getClass().getResource(imagePath));
			this.width = mapImg.getWidth();
			this.height = mapImg.getHeight();
			this.grid = new TileTyp[width][height];
            
			//Pixelanalyse
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
                    
					int colourRGB = mapImg.getRGB(x,y);
					Color color = new Color(colourRGB);
                    
					//Typ zuordnen
					if (isBlue(color)) {
						grid[x][y] = TileTyp.WATER;
					} else {
						grid[x][y] = TileTyp.LAND;
					}
                    
				}
			}
		} catch (IOException e) {
			//TODO Fenster bauen
			System.err.println("Map konnte nicht geladen werden!");
		}
	}
    
	/**
	 * Bestimmt, ob eine gegebene Farbe als Wasser (Blau) interpretiert werden soll.
	 *
	 * @param color zu prüfende Farbe
	 * @return true, wenn die Farbe innerhalb einer definierten Toleranz dem Zielblau entspricht
	 */
	private boolean isBlue(Color color) {
		if (color == null) return false;

		// Das Ziel-Blau aus deinem Spiel
		int targetR = 127;
		int targetG = 214;
		int targetB = 209;
        
		// Wie stark darf die Farbe maximal abweichen? 
		// Ein Wert zwischen 10 und 30 ist meistens ideal.
		int tolerance = 25; 

		// Die absolute Differenz für jeden Kanal berechnen
		int diffR = Math.abs(color.getRed() - targetR);
		int diffG = Math.abs(color.getGreen() - targetG);
		int diffB = Math.abs(color.getBlue() - targetB);

		// Wenn JEDER Kanal innerhalb der Toleranz liegt, ist es für uns "Blau"
		return diffR <= tolerance && diffG <= tolerance && diffB <= tolerance;
	}
    
	/**
	 * Liefert den {@link TileTyp} an der gegebenen (x,y)-Position. Außerhalb der Map
	 * wird {@link TileTyp#LAND} zurückgegeben.
	 *
	 * @param x X-Koordinate
	 * @param y Y-Koordinate
	 * @return TileTyp an der Position oder LAND, wenn außerhalb
	 */
	public TileTyp getTile (int x, int y) {
        
		if (x >= 0 && x < width && y >= 0 && y < height) {
			return grid[x][y];
		}
        
		// Außerhalb der Map blockieren
		return TileTyp.LAND;
	}
    
	/**
	 * Variante von {@link #getTile(int,int)} für ein {@link Coordinates}-Objekt.
	 *
	 * @param c Koordinate
	 * @return TileTyp an der Position oder LAND, wenn außerhalb
	 */
	public TileTyp getTile(Coordinates c) {
		if (c.getX() >= 0 && c.getX() < width && c.getY() >= 0 && c.getY()< height) {
			return grid[c.getX()][c.getY()];
		}
		// Außerhalb der Map blockieren
		return TileTyp.LAND;
	}

	//Getter
	/**
	 * Liefert die Breite der Karte in Kacheln.
	 * @return Breite (Anzahl Spalten)
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Liefert die Höhe der Karte in Kacheln.
	 * @return Höhe (Anzahl Zeilen)
	 */
	public int getHeight() {
		return height;
	}

    
}
