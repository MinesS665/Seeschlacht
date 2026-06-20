package model;

import java.util.ArrayList;

/**
 * Einfaches Spielmodell, das die Liste der Spieler und die Referenz auf die Map hält.
 *
 * <p>Der Controller verwendet diese Klasse, um Spieler zu aktualisieren und die
 * aktuelle Karte abzufragen.
 */
public class GameModel {

	private ArrayList<Player> players = new ArrayList<>();
	private GameMap map;
    
	/**
	 * Erzeugt ein GameModel mit einer referenzierten {@link GameMap}.
	 * @param map Spielkarte
	 */
	public GameModel (GameMap map) {
		this.map = map;
	}
    
	/**
	 * Synchronisiert die interne Spielerliste mit einer übergebenen Liste.
	 *
	 * Entfernt nicht mehr vorhandene Spieler und fügt neue Spieler hinzu.
	 * @param updatedPlayers neue Liste der Spieler
	 */
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
	/**
	 * Liefert die interne Liste der Spieler.
	 * @return Liste aller aktuellen Spieler
	 */
	public ArrayList<Player> getPlayers() {
		return players;
	}

	/**
	 * Liefert die referenzierte Spielkarte.
	 * @return GameMap-Instanz
	 */
	public GameMap getMap() {
		return map;
	}
    
}
