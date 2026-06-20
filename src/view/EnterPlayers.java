package view;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import model.Player;

public class EnterPlayers extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	//Verbindung zum MainWindow herstellen
	private static MainWindow parent;
	private static JPanel mainPanel;
	
	//globale Variablen
	private static int maxPlayers = 10;
	private static int visiblePlayers = 2;
	private static PlayerRow[] players = new PlayerRow[maxPlayers];

	//Objekte und Layout generieren
	/**
	 * Erzeugt das Panel zur Eingabe und Verwaltung der Spieler.
	 * @param parent referenziertes Hauptfenster zur Kommunikation mit dem Controller
	 */
	public EnterPlayers(MainWindow parent) {
		
		EnterPlayers.parent = parent;
		
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblEditPlayers = new JLabel("Spieler bearbeiten");
		lblEditPlayers.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblEditPlayers, BorderLayout.NORTH);
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
		add(buttonPanel, BorderLayout.SOUTH);
		
		JButton btnNewPlayer = new JButton("Neuer Spieler");
		buttonPanel.add(btnNewPlayer);
		
		JButton btnLoadGame = new JButton("Spielstand laden");
		buttonPanel.add(btnLoadGame);
		
		JButton btnFinish = new JButton("Fertig");
		buttonPanel.add(btnFinish);
		
		mainPanel = new JPanel();
		add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new GridLayout(0, 1));
		
		//Alle Spieler erstellen, nur teilweise sichtbar machen
		for(int i = 0; i<maxPlayers; i++) {
			players[i] = new PlayerRow(i+1);
			mainPanel.add(players[i]);
			
			if (i>=visiblePlayers) players[i].setVisible(false);
		}
		
		btnLoadGame.addActionListener(e -> {

			if (parent.getController().loadGame()) parent.leavePlayerEditor();
			});
		
		//neue Spieler hinzufügen
		btnNewPlayer.addActionListener(e -> {
			if(visiblePlayers < maxPlayers) {
				visiblePlayers++;
				updateLayout();
			}else {
				parent.problem("Maximale Spieleranzahl erreicht");
			}
		});
		
		//Spielerbearbeitung verlassen
		btnFinish.addActionListener(e -> submitPlayers());
		
	}
	
	/**
	 * Liest die eingegebenen Spielerzeilen aus, validiert die Eingaben und übergibt
	 * die finale Spielerliste an den Controller.
	 */
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
				parent.problem("Alle Spieler müssen einen Namen wählen");
				x = -1;
				break;
			}
			if (names.contains(p.getTxtEnterName())) {
				parent.problem( "Alle Spieler sollten verschiedene Namen wählen");
				x = -1;
				break;
				
			} else names.add(p.getTxtEnterName());
			
			//Spieler der Arraylist hinzufügen
			finalPlayers.add(new Player(p.getTxtEnterName(), p.getComboPickColor()));
		}
		
		if (x == 0) {
			parent.getController().savePlayers(finalPlayers);
			parent.leavePlayerEditor();
		}
		
	}
	
	/**
	 * Aktualisiert das Layout des Panels, sodass genau die aktuell sichtbaren
	 * PlayerRow-Elemente in der richtigen Reihenfolge angezeigt werden.
	 */
	public static void updateLayout() {
		
		//Alle Objekte entfernen und gewuenschte wiederherstellen um Reihenfolge beizubehalten
		mainPanel.removeAll();
		for (int i = 0; i < maxPlayers; i++) {
			mainPanel.add(players[i]);
			if (i>=visiblePlayers) players[i].setVisible(false);
			else players[i].setVisible(true);
		}
		
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	/**
	 * Entfernt die angegebene PlayerRow aus der sichtbaren Liste und verschiebt
	 * die nachfolgenden Zeilen nach. Diese Methode ist statisch, da sie von den
	 * PlayerRow-Instanzen (Remove-Button) aufgerufen wird.
	 *
	 * @param rowToRemove die Zeile, die entfernt werden soll
	 */
	public static void removePlayer(PlayerRow rowToRemove) {
		
		if (visiblePlayers <=2) {
			parent.problem("Es braucht mindestens 2 Spieler");
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
