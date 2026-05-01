package view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import model.Player;

import javax.swing.JSplitPane;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JFrame;

public class EnterPlayers extends JPanel {
	
	int maxPlayers = 10;
	int visiblePlayers = 2;

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public EnterPlayers() {
	
		ArrayList<JLabel> lblPlayer = new ArrayList<JLabel>();
		ArrayList<JTextField> txtEnterName = new ArrayList<JTextField>();
		ArrayList<JComboBox> comboPickColor = new ArrayList<JComboBox>();
		
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
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 3));
		
		createLayout(lblPlayer, txtEnterName, comboPickColor, panel);
		
		btnNewPlayer.addActionListener(e -> {
			if(visiblePlayers <= maxPlayers) {
				visiblePlayers++;
			}else {
				maxPlayers++;
				visiblePlayers++;
			}
			
		});
		
	}
	
	void createLayout(ArrayList<JLabel> lblPlayer, ArrayList<JTextField> txtEnterName, ArrayList<JComboBox> comboPickColor, JPanel panel) {
		
		String[] colors = {"Farbe wählen", "Rot", "Orange", "Gelb", "Lila", "Schwarz", "Weiß"};
		
		for (int i = 0; i<=maxPlayers; i++) {
			
			lblPlayer.add(new JLabel("Spieler " + (i+1)));
			panel.add(lblPlayer.get(i));
			
			txtEnterName.add(new JTextField());
			panel.add(txtEnterName.get(i));
			
			comboPickColor.add(new JComboBox<String>(colors));
			panel.add(comboPickColor.get(i));
			
			if (i>visiblePlayers-1) {
				lblPlayer.get(i).setVisible(false);
				txtEnterName.get(i).setVisible(false);
				comboPickColor.get(i).setVisible(false);
			} 
		}
		//Layout neuladen
		panel.revalidate();
		panel.repaint();
	}
}
