package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.GameController;
import model.Coordinates;
import model.TileTyp;

public class AttackPanel extends JPanel implements MouseListener, MouseMotionListener{

	private static final long serialVersionUID = 1L;
	private GameController controller;
	private MapView mapView;
	private boolean isShooting;
	private final double maxDistance = 25;

	public ArrayList<Point> points = new ArrayList<>();
	
	//Konstruktor
	public AttackPanel(GameController controller, MapView mapView) {
		this.controller = controller;
		this.mapView = mapView;
	    setOpaque(false); //Transparent, damit Map sichtbar ist
	    addMouseListener(this);
	    addMouseMotionListener(this);
	}

	@Override
	//Bewegung der Maus trackn und rehtzeitig beenden
    public void mouseMoved(MouseEvent e) {
		
        if (!isShooting) return;

        Point currentPoint = e.getPoint();

        //Übers Land schießen verhindern
		if (mapView.getMap().getTile(Coordinates.toCoordinates(currentPoint, mapView.getTileSize())) == TileTyp.LAND) {
			repaint();
			fireShot();
			return;
		}
		
		//Schuss beenden, wenn Reichweite ausgeschöpft ist
		if (points.size() >= maxDistance) { 
	        repaint();
	        fireShot();
	        return;
	    }

        points.add(currentPoint);
        repaint();
    }
	
	//Schuss durch Klick auf das Schiff starten
	public void mouseClicked(MouseEvent e) {
        
        if (!isShooting) {
        	
        	Coordinates clickPos = new Coordinates(e.getX()/mapView.getTileSize(), e.getY()/mapView.getTileSize());

        	if (controller.getCurShipPos() == null) {
        		return;
        	}
        	
        	if(!clickPos.isClose(controller.getCurShipPos())) {
        		JOptionPane.showMessageDialog(this, "Klicke auf dein aktuelles Shiff um den Angriff zu starten");
        		return;
        	}
        	
            points.clear();
            points.add(e.getPoint());;
            isShooting = true;
            repaint();
        }
    }
	
	//Schuss anzeigen und gesammelte Punkte an Controller übergeben
	private void fireShot() {
        isShooting = false;
        
        //pausieren umm Schuss anzuzeigen
        try {
            Thread.sleep(1500); // Pausiert für 1 Sekunde
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Status wiederherstellen
        }
        
        //Casting
        ArrayList<Coordinates> cPoints = fillGaps(points);     
        for(Point p : points) {
        	cPoints.add(Coordinates.toCoordinates(p, mapView.getTileSize()));
        }
        
        //Punkte übergeben
        controller.scanDamage(cPoints);

        //Liste leeren damit Linie nach dem Schuss verschwindet
        points.clear();
        repaint();
    }
	
	@Override
	//Punkte verbinden
    public void paintComponent(Graphics g) {
		
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (points.size() > 1) {
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2.5f));

            for (int i = 1; i < points.size(); i++) {
                Point p1 = points.get(i - 1);
                Point p2 = points.get(i);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }
	
	//Lücken füllen
	private ArrayList<Coordinates> fillGaps(ArrayList<Point> points) {
		
	    ArrayList<Coordinates> tilesHit = new ArrayList<>();
	    
	    if (points == null || points.isEmpty()) {
	        return tilesHit;
	    }
	    
	    // Falls nur ein einziger Klick registriert wurde
	    if (points.size() == 1) {
	    	tilesHit.add(Coordinates.toCoordinates(points.get(0), mapView.getTileSize()));
	        return tilesHit;
	    }
	    
	    //Paarweise Betrachtung der Punkte
	    for (int i = 1; i < points.size(); i++) {
	        Point p1 = points.get(i - 1);
	        Point p2 = points.get(i);
	        
	        //Abstand berechnen
	        double dx = p2.x - p1.x;
	        double dy = p2.y - p1.y;
	        double distance = Math.sqrt(dx * dx + dy * dy);
	        
	        //Als Pixel interpretieren
	        int steps = (int) distance;
	        
	        for (int j = 0; j <= steps; j++) {
	        	
	            double progression = (distance == 0) ? 0 : (double) j / distance;
	            
	            //Zwischenpunkte erstellen
	            int intermediateX = (int) (p1.x + dx * progression);
	            int intermediateY = (int) (p1.y + dy * progression);
	            
	            Coordinates c = Coordinates.toCoordinates(new Point(intermediateX, intermediateY), mapView.getTileSize());
	            
	            if (!tilesHit.contains(c)) {
	            	tilesHit.add(c);
	            }
	        }
	    }
	    
	    return tilesHit;
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mouseDragged(MouseEvent e) {}

}
