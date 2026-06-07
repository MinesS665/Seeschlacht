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
	private Board board;
	boolean isShooting;
	final double maxDistance = 25;
	double curDistance;

	public ArrayList<Point> points = new ArrayList<>();
	
	public AttackPanel(GameController controller, Board board) {
		this.controller = controller;
		this.board = board;
	    setOpaque(false); // Transparent, damit die Map sichtbar bleibt
	    addMouseListener(this);
	    addMouseMotionListener(this);
	}

	@Override
    public void mouseMoved(MouseEvent e) {
        if (!isShooting) return;

        Point currentPoint = e.getPoint();
        Point lastPoint = points.get(points.size() - 1);

        //Übers Land schießen verhindern
		if (board.getMap().getTile(Coordinates.toCoordinates(currentPoint, board.getTileSize())) == TileTyp.LAND) {
			repaint();
			fireShot();
			return;
		}
		
		if (points.size() >= maxDistance) { 
	        repaint();
	        fireShot(); // Limit erreicht -> Schuss stoppt automatisch
	        return;
	    }

        points.add(currentPoint);
        repaint();
    }
	
	public void mouseClicked(MouseEvent e) {
        // Erster Klick startet den Schuss
        if (!isShooting) {
        	
        	Coordinates clickPos = new Coordinates(e.getX()/board.getTileSize(), e.getY()/board.getTileSize());

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
	
	private void fireShot() {
        isShooting = false;
        
        try {
            Thread.sleep(1500); // Pausiert für 1 Sekunde
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Status wiederherstellen
        }
        
        //Casting
        ArrayList<Coordinates> cPoints = fillGaps(points);     
        for(Point p : points) {
        	cPoints.add(Coordinates.toCoordinates(p, board.getTileSize()));
        }
        
        controller.AttackFinished(cPoints);
        
        System.out.println("Maximale Reichweite erreicht! Schuss abgefeuert mit " + points.size() + " Punkten.");

        // Liste leeren, damit die Linie nach dem Schuss verschwindet
        points.clear();
        repaint();
    }
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (points.size() > 1) {
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(3.0f));

            for (int i = 1; i < points.size(); i++) {
                Point p1 = points.get(i - 1);
                Point p2 = points.get(i);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }
	
	private ArrayList<Coordinates> fillGaps(ArrayList<Point> points) {
	    ArrayList<Coordinates> kacheln = new ArrayList<>();
	    
	    if (points == null || points.isEmpty()) {
	        return kacheln;
	    }
	    
	    // Falls nur ein einziger Klick registriert wurde
	    if (points.size() == 1) {
	        kacheln.add(Coordinates.toCoordinates(points.get(0), board.getTileSize()));
	        return kacheln;
	    }
	    
	    // Lücken zwischen den Punkten interpolieren
	    for (int i = 1; i < points.size(); i++) {
	        Point p1 = points.get(i - 1);
	        Point p2 = points.get(i);
	        
	        double dx = p2.x - p1.x;
	        double dy = p2.y - p1.y;
	        double distance = Math.sqrt(dx * dx + dy * dy);
	        
	        int schritte = (int) distance;
	        for (int j = 0; j <= schritte; j++) {
	            double anteil = (distance == 0) ? 0 : (double) j / distance;
	            int intermediateX = (int) (p1.x + dx * anteil);
	            int intermediateY = (int) (p1.y + dy * anteil);
	            
	            Coordinates c = Coordinates.toCoordinates(new Point(intermediateX, intermediateY), board.getTileSize());
	            
	            if (!kacheln.contains(c)) {
	                kacheln.add(c);
	            }
	        }
	    }
	    
	    return kacheln;
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
