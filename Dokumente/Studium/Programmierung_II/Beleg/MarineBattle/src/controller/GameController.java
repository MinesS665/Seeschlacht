package controller;

import java.util.ArrayList;

import model.GameModel;
import view.MainWindow;
import model.Player;

public class GameController {
	
	private MainWindow view;
	private GameModel model;

	
	public GameController (MainWindow view, GameModel model, model.GameMap map) {
		this.view = view;
		this.model = model;
		view.createMap(map);
	}
	
	public void SavePlayers(ArrayList<Player> players) {
		model.UpdatePlayers(players);
		
	}
	
}
