package model;

import java.util.ArrayList;

public class GameModel {
	
	private String imagePath = "/assets/Map.png";
	ArrayList<Player> players = new ArrayList<>();
	
	public void UpdatePlayers(ArrayList<Player> updatedPlayers) {
		
		for (Player p : players) {
			if (!updatedPlayers.contains(p)) players.remove(p);
			Player.setPlayerCount(-1);
		}
		for (Player p : updatedPlayers) {
			if(!players.contains(p)) players.add(p);
		}
		
	}
	
	public GameMap CreateMap() {
		GameMap map = new GameMap(imagePath);
		return map;
	}

}
