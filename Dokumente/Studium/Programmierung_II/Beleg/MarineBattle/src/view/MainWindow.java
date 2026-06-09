package view;

import java.awt.CardLayout;
import javax.swing.*;

import controller.GameController;
import controller.State;
import model.GameMap;
import model.Player;

import java.awt.BorderLayout;

public class MainWindow extends JFrame{

	private static final long serialVersionUID = 1L;
	private CardLayout cardLayout = new CardLayout();
	private JPanel mainPanel = new JPanel(cardLayout);
	private JPanel gamePanel = new JPanel(new BorderLayout());
	private JPanel mapPanel;
	private JPanel controlPanel;
	private JPanel attackPanel;
	
	private GameController controller;

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
		
		EnterPlayerEditor();
	}
	
	public void LeavePlayerEditor() {
		cardLayout.show(mainPanel, "GAME");
	}
	
	public void EnterPlayerEditor() {
		cardLayout.show(mainPanel, "P-MENU");
	}

	//Karten-Panel erstellen
	public void CreateMap(GameMap map) {

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

	public void PlaceHarbour(Player p) {
		
		((ControlBar) controlPanel).PlaceHarbour(p);
	}

	public GameController getController() {
		return controller;
	}

	public void setController(GameController controller) {
		this.controller = controller;
	}
	
	public void NextMove(Player p) {
		
		((ControlBar) controlPanel).NextMove(p);
	}

	public ControlBar getControlPanel() {
		return (ControlBar) controlPanel;
	}
	
	public void problem (String text) {
		JOptionPane.showMessageDialog(this, text);
	}

	public void Attack() {
		
		attackPanel.setVisible(true);
		((ControlBar) controlPanel).setFinishBtnVis(false);;
		
	}
	
	public void AttackFinished() {
		
		attackPanel.setVisible(false);
		((ControlBar) controlPanel).setAttBtnVis(false);
		((ControlBar) controlPanel).setFinishBtnVis(true);;

	}

	public JPanel getBoardPanel() {
		return mapPanel;
	}
	
	public void infoScreen (String info) {
		JOptionPane.showMessageDialog(this, info);
	}
	public void infoScreen (String name, State state) {
		
		if (state == State.END) {
			JOptionPane.showMessageDialog(	this,
											"🏆 DER SIEGER STEHT FEST! 🏆\n\n" + name + " hat die feindliche Flotte versenkt!", 
				    						"Marine Battle - Spiel beendet", 
				    						JOptionPane.INFORMATION_MESSAGE
				);
		} else if (state == State.ATTACK) JOptionPane.showMessageDialog(this, name + " wurde besiegt");
	}
}
