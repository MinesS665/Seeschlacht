package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GameMap {
	
	private TileTyp[][] grid;
	private int width;
	private int height;
	
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
			//TODO Fenster anzeigen
			System.err.println("Map konnte nicht geladen werden!");
		}
	}
	
	private boolean isBlue(Color color) {
		return color.equals(new Color(127, 214, 209));
	}
	
	public TileTyp getTile (int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[x][y];
        }
        return TileTyp.LAND; // Außerhalb der Map blockieren
	}
	
	public TileTyp getTile(Coordinates c) {
		if (c.getX() >= 0 && c.getX() < width && c.getY() >= 0 && c.getY()< height) {
            return grid[c.getX()][c.getY()];
        }
        return TileTyp.LAND; // Außerhalb der Map blockieren
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	
}
