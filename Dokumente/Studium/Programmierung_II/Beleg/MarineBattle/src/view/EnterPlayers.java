package view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import model.Player;

import javax.swing.JSplitPane;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.util.ArrayList;


public class EnterPlayers extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	//Verbindung zum MainWindow herstellen
	private static MainWindow parent;
	static JPanel mainPanel;
	
	//globale Variablen
	static int maxPlayers = 10;
	static int visiblePlayers = 2;
	static PlayerRow[] players = new PlayerRow[maxPlayers];

	//Objekte und Layout generieren
	public EnterPlayers(MainWindow parent) {
		
		EnterPlayers.parent = parent;
		
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblEditPlayers = new JLabel("Spieler bearbeiten");
		lblEditPlayers.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblEditPlayers, BorderLayout.NORTH);
		
		JSplitPane optionsPane = new JSplitPane();
		optionsPane.setResizeWeight(0.5);
		add(optionsPane, BorderLayout.SOUTH);
		
		JButton btnNewPlayer = new JButton("Neuer Spieler");
		optionsPane.setLeftComponent(btnNewPlayer);
		
		JButton btnFinish = new JButton("Fertig");
		optionsPane.setRightComponent(btnFinish);
		
		mainPanel = new JPanel();
		add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new GridLayout(0, 1));
		
		//Alle Spieler erstellen, nur teilweise sichtbar machen
		for(int i = 0; i<maxPlayers; i++) {
			players[i] = new PlayerRow(i+1);
			mainPanel.add(players[i]);
			
			if (i>=visiblePlayers) players[i].setVisible(false);
		}
		
		//neue Spieler hinzufügen
		btnNewPlayer.addActionListener(e -> {
			if(visiblePlayers < maxPlayers) {
				visiblePlayers++;
				updateLayout();
			}else {
				JOptionPane.showMessageDialog(parent, "Maximale Spieleranzahl erreicht");
			}
		});
		
		//Spielerbearbeitung verlassen
		btnFinish.addActionListener(e -> submitPlayers());
		
	}
	
	public void submitPlayers() {

		ArrayList<String> names = new ArrayList<>();
		ArrayList<Player> finalPlayers = new ArrayList<>();
		PlayerRow[] visPlayers = new PlayerRow[visiblePlayers];
		int x = 0;
		int j = 0;
		
		//nur sichtbare Spieler auswählen
		for (PlayerRow p : players) {
			if (p.isVisible()) {
				visPlayers[j] = p;
				j++;
			}
		}
		
		//Fehlerbehandlung
		for (PlayerRow p : visPlayers) {
			if (p.getTxtEnterName().isBlank()) {
				JOptionPane.showMessageDialog(parent, "Alle Spieler müssen einen Namen wählen");
				x = -1;
				break;
			}
			if (names.contains(p.getTxtEnterName())) {
				JOptionPane.showMessageDialog(parent,  "Alle Spieler sollten verschiedene Namen wählen");
				x = -1;
				break;
				
			} else names.add(p.getTxtEnterName());
			
			//Spieler der Arraylist hinzufügen
			finalPlayers.add(new Player(p.getTxtEnterName(), p.getComboPickColor()));
		}
		
		if (x == 0) {
			parent.leavePlayerEditor();
		}
		
	}
	
	public static void updateLayout() {
		
		//Alle Objekte entfernen und gewünste wiederhserstellen um Reihenfolge beizubehalten
		mainPanel.removeAll();
		for (int i = 0; i < maxPlayers; i++) {
			mainPanel.add(players[i]);
			if (i>=visiblePlayers) players[i].setVisible(false);
			else players[i].setVisible(true);
		}
		
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	public static void removePlayer(PlayerRow rowToRemove) {
		
		if (visiblePlayers <=2) {
			JOptionPane.showMessageDialog(parent, "Es braucht mindestes 2 Spieler");
			return;
		}
		
		int playerIndex =-1;
		
		//richtige Reihe zum entfernen finden
		for (int i = 0; i < visiblePlayers; i++) {
	        if (players[i] == rowToRemove) {
	            playerIndex = i;
	            break;
	        }
	    }
		
		if (playerIndex == -1) return;
		
		//Spieler nachrücken
		for (int i=playerIndex; i<visiblePlayers-1; i++) {
			players[i] = players[i + 1];
			players[i].updateLabel(i);
		}
		
		players[visiblePlayers - 1] = rowToRemove; 
		
		visiblePlayers--;
		PlayerRow.aPlayers--;
		
		updateLayout();
	}	
	
}
