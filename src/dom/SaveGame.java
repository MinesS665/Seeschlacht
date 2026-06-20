package dom;

import java.util.ArrayList;

import model.Player;

public class SaveGame {
	
	public String map;
	public ArrayList<Player> players;
	public int curPlayerId;
	
	public SaveGame (String map, ArrayList<Player> players, int curPlayerId) {
		this.map = map;
		this.players = players;
		this.curPlayerId = curPlayerId;
	}
}
