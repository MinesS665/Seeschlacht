package dom;

import java.util.ArrayList;

import model.Player;

/**
 * Repräsentiert einen gespeicherten Spielstand (Einfach-DTO).
 */
public class SaveGame {
    
	/** Kartenpfad oder Identifier (optional). */
	public String map;
	/** Liste der Spieler im gespeicherten Spiel. */
	public ArrayList<Player> players;
	/** ID des Spielers, der zuletzt am Zug war. */
	public int curPlayerId;
    
	/**
	 * Konstruktor.
	 * @param map Karten-String (optional)
	 * @param players Liste der Spieler
	 * @param curPlayerId ID des aktuellen Spielers
	 */
	public SaveGame (String map, ArrayList<Player> players, int curPlayerId) {
		this.map = map;
		this.players = players;
		this.curPlayerId = curPlayerId;
	}
}
