package view;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.*;


/**
 * UI-Komponente: Eine Zeile im Spieler-Editor mit Namen, Farbauswahl und Entfernen-Button.
 */
public class PlayerRow extends JPanel {

	private static final long serialVersionUID = 1L;

	/** Anzahl der angelegten PlayerRow-Instanzen (UI-zählend). */
	public static int aPlayers = 0; 

	/** Label mit Spieler-Nummer (z. B. "Spieler 1"). */
	private JLabel lblSpieler ;
	/** Textfeld zur Eingabe des Spielernamens. */
	private JTextField txtEnterName;
	/** ComboBox zur Farbauswahl des Spielers. */
	private JComboBox<String> comboPickColor;
	/** Button, um diese Zeile / den Spieler zu entfernen. */
	private JButton btnRemovePlayer;
    
	/** Interne Farbnamen, angezeigt in der ComboBox. */
	String[] colors = {"Farbe wählen", "Rot", "Orange", "Gelb", "Grün", "Blau", "Lila", "Rosa", "Magenta", "Cyan", "Grau"};
	
	/**
	 * Erstellt eine Zeile im Spieler-Editor mit Textfeld, Farbauswahl und Entfernen-Button.
	 * @param index Anzeigeindex (1-basiert) für die Beschriftung
	 */
	public PlayerRow(int index) {
		
		aPlayers++;
		
		//Komponenten einer Reihe erstellen
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.lblSpieler = new JLabel("Spieler " + index);
		this.txtEnterName = new JTextField(15);
		this.comboPickColor = new JComboBox<String>(colors);
		this.btnRemovePlayer = new JButton("Entfernen");
		
		btnRemovePlayer.addActionListener(e -> EnterPlayers.removePlayer(this));
		
		//Komponenten in Reihe einfügen
		this.add(lblSpieler);
        this.add(txtEnterName);
        this.add(comboPickColor);
        this.add(btnRemovePlayer);
        
		//Layout neuladen
		this.revalidate();
		this.repaint();
	}

	/**
	 * Aktualisiert die Beschriftung der Zeile (Spieler N).
	 * @param index neuer 0-basierter Index
	 */
	public void updateLabel(int index) {
		this.lblSpieler.setText("Spieler " + (index + 1));
	}
	
	/**
	 * Liefert den eingegebenen Spielernamen aus dem Textfeld.
	 * @return eingegebener Name (leer möglich)
	 */
	public String getTxtEnterName() {
		return txtEnterName.getText();
	}

	/**
	 * Liefert die momentan ausgewählte Farbe als {@link Color}.
	 * @return ausgewählte Farbe (Standard: Grau)
	 */
	public Color getComboPickColor() {
		
		String colorName = (String) comboPickColor.getSelectedItem();
				
		return switch (colorName) {
			case "Rot" -> new Color(200, 70, 70);
			case "Orange" -> new Color(200, 130, 70);
			case "Gelb" -> new Color(210, 210, 30);
			case "Grün" -> new Color(100, 200, 70);
			case "Blau" -> new Color(70, 200, 200);
			case "Lila" -> new Color(150, 70, 200);
			case "Rosa" -> new Color(200, 70, 150);
			case "Magenta" -> new Color(200, 70, 115);
			case "Cyan" -> new Color(70, 200, 160);
			case "Grau" -> Color.GRAY;
			default -> Color.GRAY; 
		};
	}

	/**
	 * Liefert die internen Namen der auswählbaren Farben (für UI/Tests).
	 * @return String-Array mit Farbnamen
	 */
	public String[] getColors() {
		return colors;
	}

}	


