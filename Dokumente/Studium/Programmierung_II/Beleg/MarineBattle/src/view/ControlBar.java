package view;

import java.awt.Color;

import javax.swing.JPanel;

import controller.State;
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
	private JLabel lblSteps;

	public ControlBar(MainWindow parent) {
		
		this.parent = parent;
		
		setLayout(new GridLayout(1, 0, 0, 0));
		
		lblShipsDisplay = new JLabel("Flotte: OOOOO");
		add(lblShipsDisplay);
		
		lblSteps = new JLabel("Augeführte Züge: 0/10");
		add(lblSteps);
		
		lblPlayerName = new JLabel("");
		add(lblPlayerName);
		
		actionPanel = new JPanel();
		add(actionPanel);
		actionPanel.setLayout(new GridLayout(2, 0, 0, 0));
		
		btnAngriff = new JButton("Angriff");
		actionPanel.add(btnAngriff);
		btnAngriff.addActionListener(e -> parent.getController().Attack());
		
		btnBeenden = new JButton("Beenden");
		actionPanel.add(btnBeenden);
		btnBeenden.addActionListener(e -> {
			if (parent.getController().getGameState().getState() == State.PLACE_HARBOUR && isPlaced == false) parent.problem("Klicke auf die Karte um einen Standort festzulegen");
			else parent.getController().NextMove();
		});
		
	}
	
	public void NextMove(Player player) {
		
		colour = player.getColour();
		isPlaced = false;
		
		btnAngriff.setVisible(true);
		this.setBackground(colour);
		btnBeenden.setBackground(colour);
		btnAngriff.setBackground(colour);
		actionPanel.setBackground(colour);
		lblPlayerName.setText(player.getName());
		lblSteps.setVisible(true);
		updateSteps(player);
		
		
		String[] shipDisplay = new String[5];
		
		
		for (int i = 0; i < 5; i++) {
			if (player.ships[i] != null) {
				if (player.ships[i].isSunken == true) shipDisplay[i] = "x";
				else shipDisplay[i] = "o";
			}
		}
		
		lblShipsDisplay.setText("   Verfügbare Schiffe: " + shipDisplay[0] + shipDisplay[1] + shipDisplay[2] + shipDisplay[3] + shipDisplay[4]);
		
	}
	
	public void PlaceHarbour(Player player) {
		
		NextMove(player);
		
		lblShipsDisplay.setText("Von wo aus soll deine Flotte operieren?");
		lblSteps.setVisible(false);
		btnAngriff.setVisible(false);
		
	}

	public void setPlaced(boolean isPlaced) {
		this.isPlaced = isPlaced;
	}
	
	public void updateSteps(Player player) {
		lblSteps.setText("Züge: " + player.movedSteps + "/" + parent.getController().getMaxSteps());
	}
	
	public void setAttBtnVis(boolean vis) {
		btnAngriff.setVisible(vis);
	}

}
