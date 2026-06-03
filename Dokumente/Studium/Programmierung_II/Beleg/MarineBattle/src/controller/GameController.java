package controller;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.Coordinates;
import model.GameMap;
import model.GameModel;
import view.MainWindow;
import model.Player;
import model.Ship;
import model.TileTyp;

public class GameController {
	
	private MainWindow view;
	private GameModel model;
	private GameMap map;
	private int turn = 0;
	private GameState state = new GameState();
	private Player curPlayer;
	private Ship curShip;
	public ArrayList<Player> players;

	
	public GameController (MainWindow view, GameModel model, model.GameMap map) {
		this.view = view;
		this.model = model;
		this.map = map;
		view.CreateMap(map);
		view.setController(this);
	}
	
	public void SavePlayers(ArrayList<Player> players) {
		model.UpdatePlayers(players);
		StartGame();
		
	}
	
	public void StartGame() {
		
		players = model.getPlayers();
		state.setState(State.PLACE_HARBOUR);
		
		curPlayer = players.get(0);
		
		view.PlaceHarbour(curPlayer);

	}
	
	public void NextMove() {
		
		curShip = null;
		
		//Aktuellen Spieler festlegen
		if (curPlayer.getID() == players.size()) {
			turn++;
			
			state.setState(State.SELECT);
			curPlayer = players.get(0);
			
		} else {
			
			curPlayer = players.get(curPlayer.getID());
			
			if (state.getState() != State.PLACE_HARBOUR) {
				state.setState(State.SELECT);
			}
		}
		
		//Hafen platzieren? 
		if (state.getState() == State.PLACE_HARBOUR) view.PlaceHarbour(curPlayer);
		else {
			view.NextMove(curPlayer);
		}
	}

	public void handleMapCLick(Coordinates clickPos) {
		
		if(model.getMap().getTile(clickPos) != TileTyp.WATER && state.getState() != State.MOVE) {
			JOptionPane.showMessageDialog(view, "Pah, Landratte");
			return;
		} 
		
		if (state.getState() == State.PLACE_HARBOUR) placeHarbour(clickPos);
		if (state.getState() == State.SELECT) {
			curShip = SelectShip(clickPos);
			if (curShip != null) {
				state.setState(State.MOVE);
			}
		}
		if (state.getState() == State.MOVE) MoveShip(clickPos, curShip);
		
		view.repaint();
		
	}


	private Ship SelectShip(Coordinates click) {
		
		Ship curShip = null;
		int shipIndex = 0;
		
		for (int i = 0; i<5; i++) {
			if (click.isClose(curPlayer.ships[i].getPos()) || click.isClose(curPlayer.ships[i].getSecPos())) {
				shipIndex = i;
			}
		}
		
		curShip = curPlayer.ships[shipIndex];
		
		if (curShip.isSunken == true) {
			view.problem("Dieses Schiff steht dir nicht mehr zur Verfügung");
			return null;
		}
		
		for(Player p : players) {
			for (int i = 0; i<5; i++) {
				p.ships[i].isSelected = false;
			}
		}
		
		return curShip;
		
	}

	private void MoveShip(Coordinates click, Ship curShip) {
		
		int moveRange = 2;
		int tolerance = 2;
		
		view.NextMove(curPlayer);		
		curShip.isSelected = true;

		System.out.println(curShip);

		//Vektor bestimmen
		int dX = Integer.compare(click.getX(), curShip.getPos().getX());
		int dY = Integer.compare(click.getY(), curShip.getPos().getY());

		if (dY <= tolerance && dX > tolerance) {
	        dY = 0; // Klick war fast horizontal -> erzwinge rein horizontale Bewegung
	    } else if (dX <= tolerance && dY > tolerance) {
	        dX = 0; // Klick war fast vertikal -> erzwinge rein vertikale Bewegung
	    }
		
		
		if (dX == 0 && dY == 0) {
		    return;
		}

		int nextX = curShip.getPos().getX();
		int nextY = curShip.getPos().getY();

		for (int i = 0; i < moveRange; i++) {
		    nextX += dX;
		    nextY += dY;
		    
		    //Außerhalb der Map blockieren
		    if (nextX < 0 || nextX >= map.getWidth() || nextY < 0 || nextY >= map.getHeight()) {
		        break; 
		    }
		    
		    if (map.getTile(nextX, nextY) == TileTyp.WATER) {
		        curShip.setSecPos(curShip.getPos());
		        curShip.setPos(new Coordinates(nextX, nextY));
		    } else {
		        break; 
		    }
		}
		
	}

	public void placeHarbour(Coordinates c) {
		
		int x = c.getX();
		int y = c.getY();
		
		
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

	public GameState getGameState() {
		return state;
	}
	
	
}
