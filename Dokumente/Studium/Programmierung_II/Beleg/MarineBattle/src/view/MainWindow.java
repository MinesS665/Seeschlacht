package view;

import java.awt.CardLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.GameController;
import model.GameMap;
import model.GameModel;
import model.Player;

import java.awt.BorderLayout;

public class MainWindow extends JFrame{

	private static final long serialVersionUID = 1L;
	private CardLayout cardLayout = new CardLayout();
	private JPanel mainPanel = new JPanel(cardLayout);
	private JPanel gamePanel = new JPanel(new BorderLayout());
	private JPanel boardPanel;
	private JPanel controlPanel;
	private JPanel attackPanel;
	
	private GameController controller;

	public MainWindow() {
		
		//Fenster erstellen
		JFrame mainFrame = new JFrame();
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
		this.pack(); 
		this.setLocationRelativeTo(null);
	}
	
	public void EnterPlayerEditor() {
		cardLayout.show(mainPanel, "P-MENU");
		this.pack(); 
		this.setLocationRelativeTo(null);
	}

	public void CreateMap(GameMap map) {
		
		boardPanel = new Board(this, map);
		attackPanel = new AttackPanel(controller, (Board)boardPanel);
		
		JLayeredPane layerPane = new JLayeredPane();
		
		java.awt.Dimension boardSize = boardPanel.getPreferredSize();
		layerPane.setPreferredSize(boardSize);
		
		boardPanel.setBounds(0, 0, boardSize.width, boardSize.height);
	    attackPanel.setBounds(0, 0, boardSize.width, boardSize.height);
		
	    layerPane.add(boardPanel, JLayeredPane.DEFAULT_LAYER);
	    layerPane.add(attackPanel, JLayeredPane.DRAG_LAYER);
		attackPanel.setVisible(false);
	    
		gamePanel.add(layerPane, BorderLayout.CENTER);
		controlPanel = new ControlBar(this);
		gamePanel.add(controlPanel, BorderLayout.SOUTH);
		
		this.pack(); 
		this.setLocationRelativeTo(null); // Zentriert das Fenster auf dem Bildschirm
		
		gamePanel.revalidate();
		gamePanel.repaint();
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
		
	}
	
	public void AttackFinished() {
		
		attackPanel.setVisible(false);
		((ControlBar) controlPanel).setAttBtnVis(false);

	}

	public JPanel getBoardPanel() {
		return boardPanel;
	}
	
	public void EndScreen (String name) {
		JOptionPane.showMessageDialog(
			    this,
			    "🏆 DER SIEGER STEHT FEST! 🏆\n\n" + name + " hat die feindliche Flotte versenkt!", 
			    "Marine Battle - Spiel beendet", 
			    JOptionPane.INFORMATION_MESSAGE
			);
	}
}
