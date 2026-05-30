package view;

import java.awt.Color;

import javax.swing.JPanel;

import model.Player;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

public class ControlBar extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private MainWindow parent;
	private Color colour;
	
	private JLabel lblPlayerName;
	private JLabel lblShipsDisplay;
	private JButton btnAngriff;
	JButton btnBeenden;
	JPanel actionPanel; 
	public boolean isPlaced = false;

	public ControlBar(MainWindow parent) {
		
		this.parent = parent;
		
		setLayout(new GridLayout(1, 0, 0, 0));
		
		lblShipsDisplay = new JLabel("Flotte: OOOOO");
		add(lblShipsDisplay);
		
		lblPlayerName = new JLabel("");
		add(lblPlayerName);
		
		actionPanel = new JPanel();
		add(actionPanel);
		actionPanel.setLayout(new GridLayout(2, 0, 0, 0));
		
		btnAngriff = new JButton("Angriff");
		actionPanel.add(btnAngriff);
		
		btnBeenden = new JButton("Beenden");
		actionPanel.add(btnBeenden);
		btnBeenden.addActionListener(e -> {
			System.out.println(isPlaced);
			if (isPlaced == false) JOptionPane.showMessageDialog(parent, "Klicke auf die Karte um einen Standort festzulegen");
			else parent.getController().NextMove();
		});
		
	}
	
	public void NextMove(Player player, int turn) {
		
		colour = player.getColour();
		isPlaced = false;
		
		this.setBackground(colour);
		btnBeenden.setBackground(colour);
		actionPanel.setBackground(colour);
		lblPlayerName.setText(player.getName());
		
		if (turn == 0) {
			PlaceHarbour(player);
		}
		
	}
	
	public void PlaceHarbour(Player player) {		
		
		lblShipsDisplay.setText("Von wo aus soll deine Flotte operieren?");
		btnAngriff.setVisible(false);
		
	}

	public void setPlaced(boolean isPlaced) {
		this.isPlaced = isPlaced;
	}
	
	

}
