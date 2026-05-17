package view;

import java.awt.CardLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.GameMap;
import java.awt.BorderLayout;

public class MainWindow extends JFrame{

	private static final long serialVersionUID = 1L;
	private CardLayout cardLayout = new CardLayout();
	private JPanel mainPanel = new JPanel(cardLayout);
	private JPanel gamePanel = new JPanel(new BorderLayout());
	private JPanel boardPanel;

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
		
		enterPlayerEditor();
		leavePlayerEditor();
	}
	
	public void leavePlayerEditor() {
		cardLayout.show(mainPanel, "GAME");
		this.pack(); 
		this.setLocationRelativeTo(null);
	}
	
	public void enterPlayerEditor() {
		cardLayout.show(mainPanel, "P-MENU");
		this.pack(); 
		this.setLocationRelativeTo(null);
	}

	public void createMap(GameMap map) {
		boardPanel = new Board(this, map);
		gamePanel.add(boardPanel, BorderLayout.CENTER);
		
		this.pack(); 
		this.setLocationRelativeTo(null); // Zentriert das Fenster auf dem Bildschirm
		gamePanel.revalidate();
		gamePanel.repaint();
	}

}
