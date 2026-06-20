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
	public void updatePlayers(ArrayList<Player> updatedPlayers) {
		
		int oldSize = players.size();
		//prüfen, ob noch alle Spieler aktuell sind
		players.removeIf(p -> !updatedPlayers.contains(p));
		Player.setPlayerCount(oldSize -players.size());
		
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
