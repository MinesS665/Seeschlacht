package view;

import java.awt.CardLayout;
import javax.swing.*;

import controller.GameController;
import controller.State;
import model.GameMap;
import model.Player;

import java.awt.BorderLayout;

/**
 * Hauptfenster der Anwendung. Kapselt alle UI-Komponenten und bietet Methoden,
 * die der Controller zum Steuern der View aufruft (z. B. nextMove, placeHarbour).
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	/** Layout zum Wechseln zwischen Player-Editor und Spielansicht. */
	private CardLayout cardLayout = new CardLayout();
	/** Hauptpanel, das die Karten enthält. */
	private JPanel mainPanel = new JPanel(cardLayout);
	/** Panel, das die Spieloberfläche (Map + Controls) enthält. */
	private JPanel gamePanel = new JPanel(new BorderLayout());
	/** Panel mit der Karte (MapView). */
	private JPanel mapPanel;
	/** Panel mit Steuerleiste (ControlBar). */
	private JPanel controlPanel;
	/** Overlay für Angriffe (AttackPanel). */
	private JPanel attackPanel;
    
	/** Referenz auf den Controller (wird vom Controller selbst gesetzt). */
	private GameController controller;

	/** Initialisiert das Hauptfenster und zeigt den Spieler-Editor. */
	public MainWindow() {
        
		//Fenster erstellen
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Marine Battle");
		this.getContentPane().add(mainPanel);
        
		//Spieler-Menü erstellen
		EnterPlayers editPlayerPanel = new EnterPlayers(this);
		mainPanel.add(editPlayerPanel, "P-MENU");
        
		//(Leeres) Spielbrett hinzufügen
		mainPanel.add(gamePanel, "GAME");
        
		this.pack();
		this.setLocationRelativeTo(null);
        
		enterPlayerEditor();
	}
    
	public void leavePlayerEditor() {
		cardLayout.show(mainPanel, "GAME");
	}
    
	/**
	 * Zeigt den Spieler-Editor an (wechselt zur entsprechenden Karte).
	 */
	public void enterPlayerEditor() {
		cardLayout.show(mainPanel, "P-MENU");
	}

	/**
	 * Erzeugt die Map-Ansicht und das Attack-Overlay sowie das ControlBar-Panel.
	 * @param map die Spielkarte
	 */
	public void createMap(GameMap map) {

		//Karte besteht aus Map und Attack-Panel
		mapPanel = new MapView(this, map);
		attackPanel = new AttackPanel(controller, (MapView)mapPanel);

		JLayeredPane layerPane = new JLayeredPane();
		java.awt.Dimension mapSize = mapPanel.getPreferredSize();
		layerPane.setPreferredSize(mapSize);
        
		mapPanel.setBounds(0, 0, mapSize.width, mapSize.height);
		attackPanel.setBounds(0, 0, mapSize.width, mapSize.height);
        
		//Layer verwenden um Attack panel mit Map zu synchronisieren und zusammenzufassen
		layerPane.add(mapPanel, JLayeredPane.DEFAULT_LAYER);
		layerPane.add(attackPanel, JLayeredPane.DRAG_LAYER);
		attackPanel.setVisible(false);
        
		gamePanel.add(layerPane, BorderLayout.CENTER);
		controlPanel = new ControlBar(this);
		gamePanel.add(controlPanel, BorderLayout.SOUTH);
        
		gamePanel.revalidate();
		gamePanel.repaint();
        
		this.pack();
		this.setLocationRelativeTo(null);
	}

	public void placeHarbour(Player p) {
        
		((ControlBar) controlPanel).placeHarbour(p);
	}

	public GameController getController() {
		return controller;
	}

	public void setController(GameController controller) {
		this.controller = controller;
	}
    
	/**
	 * Setzt das UI auf den nächsten Zug für den übergebenen Spieler.
	 * @param p der Spieler, dessen Zug beginnt
	 */
	public void nextMove(Player p) {
        
		((ControlBar) controlPanel).nextMove(p);
	}

	public ControlBar getControlPanel() {
		return (ControlBar) controlPanel;
	}
    
	/**
	 * Zeigt eine einfache Fehlermeldung an.
	 * @param text Text der Meldung
	 */
	public void problem (String text) {
		JOptionPane.showMessageDialog(this, text);
	}

	public void attack() {
        
		attackPanel.setVisible(true);
		((ControlBar) controlPanel).setFinishBtnVis(false);
        
	}
    
	public void attackFinished() {
        
		attackPanel.setVisible(false);
		((ControlBar) controlPanel).setAttBtnVis(false);
		((ControlBar) controlPanel).setFinishBtnVis(true);
 
	}

	public JPanel getBoardPanel() {
		return mapPanel;
	}
    
	/**
	 * Zeigt eine einfache Informationsmeldung an.
	 * @param info Text der Information
	 */
	public void infoScreen (String info) {
		JOptionPane.showMessageDialog(this, info);
	}
	/**
	 * Zeigt eine Zustands-basierte Informationsmeldung an (z. B. Sieger).
	 * @param name Name oder Textinhalt, abhängig vom Zustand
	 * @param state Spielzustand, der die Art der Meldung bestimmt
	 */
	public void infoScreen (String name, State state) {
        
		if (state == State.END) {
			JOptionPane.showMessageDialog(    this,
											"🏆 DER SIEGER STEHT FEST! 🏆\n\n" + name + " hat die feindliche Flotte versenkt!", 
																"Marine Battle - Spiel beendet", 
																JOptionPane.INFORMATION_MESSAGE
				);
		} else if (state == State.ATTACK) JOptionPane.showMessageDialog(this, name + " wurde besiegt");
	}
}
