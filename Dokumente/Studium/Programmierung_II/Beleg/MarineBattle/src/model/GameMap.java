package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GameMap {
	
	private TileTyp[][] grid;
	private int width;
	private int height;
	
	//Konstruktor
	public GameMap(String imagePath) {
		try {
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
	
	//Vergleichen ob Pixel blau ist
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
	
	//TileTyp zurückgeben
	public TileTyp getTile (int x, int y) {
		
		if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[x][y];
        }
		
		// Außerhalb der Map blockieren
        return TileTyp.LAND;
	}
	
	//TileTyp zurückgeben
	public TileTyp getTile(Coordinates c) {
		if (c.getX() >= 0 && c.getX() < width && c.getY() >= 0 && c.getY()< height) {
            return grid[c.getX()][c.getY()];
        }
		// Außerhalb der Map blockieren
        return TileTyp.LAND;
	}

	//Getter
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	
}
