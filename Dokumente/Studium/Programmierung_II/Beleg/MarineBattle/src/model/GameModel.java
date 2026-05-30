package model;

import java.util.ArrayList;

public class GameModel {

	private ArrayList<Player> players = new ArrayList<>();
	GameMap map;
	
	public GameModel (GameMap map) {
		this.map = map;
	}
	
	public void UpdatePlayers(ArrayList<Player> updatedPlayers) {
		
		for (Player p : players) {
			if (!updatedPlayers.contains(p)) players.remove(p);
			Player.setPlayerCount(-1);
		}
		for (Player p : updatedPlayers) {
			if(!players.contains(p)) players.add(p);
		}
		
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public GameMap getMap() {
		return map;
	}
	
}
