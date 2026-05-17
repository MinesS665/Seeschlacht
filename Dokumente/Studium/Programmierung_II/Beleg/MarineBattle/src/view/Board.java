package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import model.GameMap;
import model.TileTyp;

import java.awt.Color;
import java.awt.Dimension;

public class Board extends JPanel{
	
	private static final long serialVersionUID = 1L;

	private MainWindow parent;	
	private int tileSize = 4;
	private GameMap map;

	public Board(MainWindow parent, GameMap map) {
		
		this.parent = parent;
		this.map = map;
		
		// Fenstergröße berechnen: PNG-Breite * Skalierung
        int width = map.getWidth() * tileSize;
        int height = map.getHeight() * tileSize;
        System.out.println(width);
        this.setPreferredSize(new Dimension(width, height));
	}
	
	@Override
	protected void paintComponent (Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		//Map zeichnen
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                
            	TileTyp type = map.getTile(x, y);
                
                if (type == TileTyp.WATER) g2d.setColor(new Color(127, 214, 209));
                else g2d.setColor(new Color(120, 191, 105));
                
                //Pixel zeichnen
                g2d.fillRect(x*tileSize, y*tileSize, tileSize, tileSize);
            }
        }
	}

}
