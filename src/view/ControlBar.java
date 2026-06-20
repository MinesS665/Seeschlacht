package view;

import java.awt.Color;

import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JButton;

import controller.State;
import model.Player;

/**
 * Steuerleiste (unten im Fenster) mit Informationen zum aktuellen Spieler
 * und Aktionstasten (Angriff, Zug beenden).
 */
public class ControlBar extends JPanel {

	private static final long serialVersionUID = 1L;
    
	/** Referenz zum übergeordneten Fenster zur Kommunikation mit dem Controller. */
	private MainWindow parent;
	/** Aktuelle Hintergrundfarbe der Steuerleiste (Spielerfarbe). */
	private Color colour;
	/** Flag, ob der Hafen bereits platziert wurde (UI-Status). */
	public boolean isPlaced = false;
    
	/** Label mit dem Spielernamen. */
	private JLabel lblPlayerName;
	/** Label, das die Schiffsübersicht anzeigt. */
	private JLabel lblShipsDisplay;
	/** Label, das die verbrauchten Schritte anzeigt. */
	private JLabel lblSteps;
	/** Button zum Starten eines Angriffs. */
	private JButton btnAngriff;
	/** Button zum Beenden des Zugs. */
	private JButton btnBeenden;
	/** Container für Aktions-Buttons. */
	private JPanel actionPanel; 
    
	/**
	 * Erzeugt die Steuerleiste und verbindet die Buttons mit Controller-Aufrufen.
	 */
	public ControlBar(MainWindow parent) {
        
		this.parent = parent;
        
		setLayout(new GridLayout(1, 0, 0, 0));
        
		lblShipsDisplay = new JLabel("Flotte: OOOOO");
		add(lblShipsDisplay);
        
		lblSteps = new JLabel("Ausgefuehrte Züge: 0/10");
		add(lblSteps);
        
		lblPlayerName = new JLabel("");
		add(lblPlayerName);
        
		actionPanel = new JPanel();
		add(actionPanel);
		actionPanel.setLayout(new GridLayout(2, 0, 0, 0));
        
		btnAngriff = new JButton("Angreifen");
		actionPanel.add(btnAngriff);
		btnAngriff.addActionListener(e -> parent.getController().attackStart());
        
		btnBeenden = new JButton("Zug beenden");
		actionPanel.add(btnBeenden);
		btnBeenden.addActionListener(e -> {
			if (parent.getController().getState() != State.PLACE_HARBOUR) parent.getController().saveGame();
			if (parent.getController().getState() == State.PLACE_HARBOUR && isPlaced == false) {
				parent.problem("Klicke auf die Karte um einen Standort festzulegen");
			}
			else {
				parent.getController().nextMove();
			}
		});
        
	}
    
	/** Aktualisiert die Anzeige für den nächsten Spieler. */
	public void nextMove(Player player) {
        
		colour = player.getColour();
		isPlaced = false;
        
		btnAngriff.setVisible(true);
		btnBeenden.setVisible(true);
		this.setBackground(colour);
		btnBeenden.setBackground(colour);
		btnAngriff.setBackground(colour);
		actionPanel.setBackground(colour);
		lblPlayerName.setText(player.getName());
		lblSteps.setVisible(true);
		updateSteps(player);
        
		//Schiffsübersicht erstellen
		String[] shipDisplay = new String[5];
        
		for (int i = 0; i < 5; i++) {
			if (player.ships[i] != null) {
				if (player.ships[i].isSunken == true) shipDisplay[i] = "x";
				else shipDisplay[i] = "o";
			}
		}
        
		lblShipsDisplay.setText("   Verfügbare Schiffe: " + shipDisplay[0] + shipDisplay[1] + shipDisplay[2] + shipDisplay[3] + shipDisplay[4]);
        
		actionPanel.revalidate();
		actionPanel.repaint();
		this.revalidate();
		this.repaint();
	}
    
	/**
	 * Bereitet die Anzeige für die Hafenplatzierung vor.
	 * @param player Spieler, der gerade seinen Hafen setzt
	 */
	//Layout zu Beginn vereinfachen
	public void placeHarbour(Player player) {
        
		nextMove(player);
        
		lblShipsDisplay.setText("Von wo aus soll deine Flotte operieren?");
		lblSteps.setVisible(false);
		btnAngriff.setVisible(false);
        
	}

	/**
	 * Markiert im ControlBar, dass der Hafen gesetzt wurde.
	 * @param isPlaced true, wenn der Hafen bereits platziert wurde
	 */
	public void setPlaced(boolean isPlaced) {
		this.isPlaced = isPlaced;
	}
    
	/**
	 * Aktualisiert das Label mit den bereits genutzten Schritten des Spielers.
	 * @param player Spieler, dessen Schritte angezeigt werden
	 */
	public void updateSteps(Player player) {
		lblSteps.setText("Züge: " + player.movedSteps + "/" + parent.getController().getMaxSteps());
	}
    
	/**
	 * Setzt die Sichtbarkeit des Angriffs-Buttons.
	 * @param vis sichtbar = true
	 */
	public void setAttBtnVis(boolean vis) {
		btnAngriff.setVisible(vis);
	}
	/**
	 * Setzt die Sichtbarkeit des "Zug beenden"-Buttons.
	 * @param vis sichtbar = true
	 */
	public void setFinishBtnVis(boolean vis) {
		btnBeenden.setVisible(vis);
	}
}
