package DOM;

import java.util.ArrayList;

import model.Player;
import controller.State;

public class SaveGame {
	
	public String map;
	public ArrayList<Player> players;
	public State curState;
	public int curPlayerID;
	
	public SaveGame (String map, ArrayList<Player> players, State state, int curPlayerID) {
		this.map = map;
		this.players = players;
		this.curState = state;
		this.curPlayerID = curPlayerID;
	}
}
