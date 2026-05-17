package view;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PlayerRow extends JPanel {

	private static final long serialVersionUID = 1L;

	public static int aPlayers = 0; 

	private JLabel lblSpieler ;
	private JTextField txtEnterName;
	private JComboBox<String> comboPickColor;
	private JButton btnRemovePlayer;
	
	String[] colors = {"Farbe wählen", "Rot", "Orange", "Gelb", "Lila", "Schwarz", "Weiß", "Rosa", "Magenta", "Cyan", "Grau"};
	
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
			case "Rot" -> Color.RED;
			case "Orange" -> Color.ORANGE;
			case "Gelb" -> Color.YELLOW;
			case "Lila" -> new Color(128, 0, 128);
			case "Schwarz" -> Color.BLACK;
			case "Weiß" -> Color.WHITE;
			case "Rosa" -> Color.PINK;
			case "Magenta" -> Color.MAGENTA;
			case "Cyan" -> Color.CYAN;
			case "Grau" -> Color.DARK_GRAY;
			default -> Color.GRAY; 
		};
	}

	public String[] getColors() {
		return colors;
	}

}	


