package controller;

import model.Player;

public class GameState {

	Player player;
	State state;
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "GameState [player=" + player + ", state=" + state + "]";
	}
	
	
	
}
