package controller;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.Coordinates;
import model.GameModel;
import view.MainWindow;
import model.Player;
import model.Ship;
import model.TileTyp;

public class GameController {
	
	private MainWindow view;
	private GameModel model;
	private int turn = 0;
	private GameState state = new GameState();
	private Player curPlayer;
	public ArrayList<Player> players;

	
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
		
		players = model.getPlayers();
		state.setState(State.PLACE_HARBOUR);
		
		curPlayer = players.get(0);
		
		view.PlaceHarbour(curPlayer);

	}
	
	public void NextMove() {
		
		if (curPlayer.getID() == players.size()) NextTurn();
		else {
			System.out.println("else");
			curPlayer = players.get(curPlayer.getID());
		}
		
		if (state.getState() == State.PLACE_HARBOUR) view.PlaceHarbour(curPlayer);
		
		System.out.println(curPlayer);
	}

	private void NextTurn() {
		
		state.setState(State.MOVE);
		
		curPlayer = players.get(0);
		
	}

	public void handleMapCLick(int x, int y) {
		
		if(model.getMap().getTile(x,y) != TileTyp.WATER) {
			JOptionPane.showMessageDialog(view, "Pah, Landratte");
			return;
		} 
		
		if (state.getState() == State.PLACE_HARBOUR) placeHarbour(x,y);
		
		if (state.getState() == State.MOVE) MoveShip();
		
	}
	
	private void MoveShip() {
		
		
		
	}

	public void placeHarbour(int x, int y) {
		
		for (int i = -5; i<=5; i++) {
			for (int j = -2; j<=2; j++) {
				if(model.getMap().getTile(x+i,y+j) != TileTyp.WATER) {
					JOptionPane.showMessageDialog(view, "Nicht genug Platz für deine Flotte. Entferne dich weiter vom Ufer");
					return;
				}
			}
		}
		
		curPlayer.setPosHabour(new Coordinates(x,y));
		
		curPlayer.ships[0] = new Ship(curPlayer, new Coordinates(x-4,y));
		curPlayer.ships[1] = new Ship(curPlayer, new Coordinates(x-2,y));
		curPlayer.ships[2] = new Ship(curPlayer, new Coordinates(x,y));
		curPlayer.ships[3] = new Ship(curPlayer, new Coordinates(x+2,y));
		curPlayer.ships[4] = new Ship(curPlayer, new Coordinates(x+4,y));
		
		view.repaint();
		view.getControlPanel().setPlaced(true);
	}

	public Player getCurPlayer() {
		return curPlayer;
	}
	
	
}
