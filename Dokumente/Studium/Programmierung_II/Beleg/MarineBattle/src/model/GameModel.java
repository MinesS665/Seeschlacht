package model;

import java.util.ArrayList;

public class GameModel {

	private ArrayList<Player> players = new ArrayList<>();
	private GameMap map;
	
	//Konstruktor
	public GameModel (GameMap map) {
		this.map = map;
	}
	
	//Spieler speichern
	public void UpdatePlayers(ArrayList<Player> updatedPlayers) {
		
		//prüfen, ob noch alle Spieler aktuell sind
		for (Player p : players) {
			if (!updatedPlayers.contains(p)) players.remove(p);
			Player.setPlayerCount(-1);
		}
		
		//neue Spieler hinzufügen
		for (Player p : updatedPlayers) {
			if(!players.contains(p)) players.add(p);
		}
	}

	//Getter
	public ArrayList<Player> getPlayers() {
		return players;
	}

	public GameMap getMap() {
		return map;
	}
	
}
