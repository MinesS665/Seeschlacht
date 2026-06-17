package view;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.*;


public class PlayerRow extends JPanel {

	private static final long serialVersionUID = 1L;

	public static int aPlayers = 0; 

	private JLabel lblSpieler ;
	private JTextField txtEnterName;
	private JComboBox<String> comboPickColor;
	private JButton btnRemovePlayer;
	
	String[] colors = {"Farbe wählen", "Rot", "Orange", "Gelb", "Grün", "Blau", "Lila", "Rosa", "Magenta", "Cyan", "Grau"};
	
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

	public void updateLabel(int index) {
        this.lblSpieler.setText("Spieler " + (index + 1));
    }
	
	public String getTxtEnterName() {
		return txtEnterName.getText();
	}

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

	public String[] getColors() {
		return colors;
	}

}	


