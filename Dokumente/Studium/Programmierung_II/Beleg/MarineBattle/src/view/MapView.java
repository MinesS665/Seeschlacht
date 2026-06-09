package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import model.Coordinates;
import model.GameMap;
import model.Ship;
import model.TileTyp;

import java.awt.Color;
import java.awt.Dimension;

public class MapView extends JPanel{
	
	private static final long serialVersionUID = 1L;

	private MainWindow parent;	
	private int tileSize = 4;
	private GameMap map;

	//Konstruktor
	public MapView(MainWindow parent, GameMap map) {
		
		this.parent = parent;
		this.map = map;
        
        this.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mousePressed(MouseEvent e) {
        		int xPixel = e.getX() / tileSize;
        		int yPixel = e.getY() /tileSize;
        		
        		parent.getController().handleMapCLick(new Coordinates(xPixel, yPixel));
        	}
        });
        
	}
	
	@Override
	protected void paintComponent (Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		//Map zeichnen
        DrawMap(g2d);
                
        //Schiffe zeichnen
        DrawShips(g2d);   
   
    }
	
	//Map erstellen
	public void DrawMap(Graphics2D g2d) {
		
		//Pixelweise Map zeichnen
		for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                
            	//Entsprechende Farbe festlegen
            	TileTyp type = map.getTile(x, y);
                
                if (type == TileTyp.WATER) g2d.setColor(new Color(127, 214, 209));
                else g2d.setColor(new Color(120, 191, 105));
	
              //Pixel zeichnen
              g2d.fillRect(x*tileSize, y*tileSize, tileSize, tileSize);
            }
		}
	}
	
	//Schiff über die Karte zeichnen
	public void DrawShips(Graphics2D g2d) {

		ArrayList<model.Player> allPlayers = parent.getController().players;;
			         
		if (allPlayers == null) return;
		
		for (model.Player p : allPlayers) {
			                 
			//Schleife durch alle Schiffe eines Spielers
			for (Ship s : p.ships) {
				if (s != null) {
					
					Coordinates shipPos1 = s.pos;
					Coordinates shipPos2 = s.secPos;
					
					if (shipPos1 != null && shipPos2 != null) {
						
						if (s.isSelected == true) g2d.setColor(Color.WHITE);
						else if (s.isSunken == true) g2d.setColor(p.getColour().darker());
						else g2d.setColor(p.getColour());
						
						//Zeichne ein Quadrat in Spielerfarbe
						g2d.fillRect(shipPos1.getX() * tileSize, shipPos1.getY() * tileSize, tileSize, tileSize);
						g2d.fillRect(shipPos2.getX() * tileSize, shipPos2.getY() * tileSize, tileSize, tileSize);
						
						//schwarzen Rand um das Schiff
						g2d.setColor(Color.BLACK);
						g2d.drawRect(shipPos1.getX() * tileSize, shipPos1.getY() * tileSize, tileSize, tileSize);
						g2d.drawRect(shipPos2.getX() * tileSize, shipPos2.getY() * tileSize, tileSize, tileSize);
						
						//Farbe wieder zurücksetzen
						g2d.setColor(p.getColour()); 
					}
				}
			}
			}
		
	}
	
	@Override
	//Fenstergröße berechnen: PNG-Breite * Skalierung
	public Dimension getPreferredSize() {
		
		//Fenstergröße berechnen: PNG-Breite * Skalierung
        int width = map.getWidth() * tileSize;
        int height = map.getHeight() * tileSize;
		
		return new Dimension(width, height);
	}

	public int getTileSize() {
		return tileSize;
	}

	public GameMap getMap() {
		return map;
	}
	
	
}