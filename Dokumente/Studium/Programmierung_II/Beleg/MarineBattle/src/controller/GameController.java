package controller;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.Coordinates;
import model.GameModel;
import view.MainWindow;
import model.Player;
import model.TileTyp;

public class GameController {
	
	private MainWindow view;
	private GameModel model;
	private int turn = 0;
	private GameState state = new GameState();
	private Player curPlayer;

	
	public GameController (MainWindow view, GameModel model, model.GameMap map) {
		this.view = view;
		this.model = model;
		view.CreateMap(map);
		view.setController(this);
	}
	
	public void SavePlayers(ArrayList<Player> players) {
		model.UpdatePlayers(players);
		System.out.println("-2");
		StartGame();
		
	}
	
	public void StartGame() {
		
		ArrayList<Player> players = model.getPlayers();
		
		for (int i = Player.getPlayerCount(); i>0 ; i--) {
			curPlayer = players.get(i-1);
			view.PlaceHarbour(players.get(i-1));
			state .setState(State.PLACE_HARBOUR);;
		}
	}
	
	public void NextMove() {
		
		
		
	}

	public void handleMapCLick(int x, int y) {
		
		if(model.getMap().getTile(x,y) != TileTyp.WATER) {
			JOptionPane.showMessageDialog(view, "Pah, Landratte");
			return;
		} 
		
		
		if (state.getState() == State.PLACE_HARBOUR) {
			
			for (int i = -5; i<=5; i++) {
				for (int j = -2; j<=2; j++) {
					if(model.getMap().getTile(x+i,y+j) != TileTyp.WATER) {
						JOptionPane.showMessageDialog(view, "Nicht genug Platz für deine Flotte. Entferne dich weiter vom Ufer");
						return;
					}
				}
			}
			
			curPlayer.setPosHabour(new Coordinates(x,y));
			view.repaint();
			view.getControlPanel().setPlaced(true);
		}
		
	}

	public Player getCurPlayer() {
		return curPlayer;
	}
	
	
}
