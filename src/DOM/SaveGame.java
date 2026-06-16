package DOM;

import java.util.ArrayList;

import model.Player;

public class SaveGame {
	
	public String map;
	public ArrayList<Player> players;
	public int curPlayerID;
	
	public SaveGame (String map, ArrayList<Player> players, int curPlayerID) {
		this.map = map;
		this.players = players;
		this.curPlayerID = curPlayerID;
	}
}
