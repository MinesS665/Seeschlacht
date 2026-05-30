package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import model.Coordinates;
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
        this.setPreferredSize(new Dimension(width, height));
        
        this.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mousePressed(MouseEvent e) {
        		int xPixel = e.getX() / tileSize;
        		int yPixel = e.getY() /tileSize;
        		
        		parent.getController().handleMapCLick(xPixel, yPixel);
        	}
        });
	}
	
	@Override
	protected void paintComponent (Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		//Aktuellen Spieler ermitteln
		model.Player curPlayer = parent.getController().getCurPlayer();		
		Coordinates harbourPos = (curPlayer != null) ? curPlayer.getPosHabour() : null;
		
		//Map zeichnen
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                
            	TileTyp type = map.getTile(x, y);
                
                if (type == TileTyp.WATER) g2d.setColor(new Color(127, 214, 209));
                else g2d.setColor(new Color(120, 191, 105));
                
                //Farbe des aktuell vom Spieler ausgewählten Feld setzen
                if (harbourPos != null && harbourPos.getX() == x && harbourPos.getY() == y) {
                    g2d.setColor(Color.WHITE);
                }
                
                //Pixel zeichnen
                g2d.fillRect(x*tileSize, y*tileSize, tileSize, tileSize);
            }
        }
	}
	
	

}
