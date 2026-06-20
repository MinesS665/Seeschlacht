package model;

import java.awt.Color;

/**
 * Repräsentiert einen Spieler im Spiel mit Identität, Farbe, Schiffen und Hafenposition.
 *
 * <p>Die Klasse enthält Hilfsmethoden zum Markieren von ausgeschiedenen Spielern
 * sowie Utility-Methoden zur Verwaltung der globalen Spieleranzahl.
 */
public class Player {

	private static int playerCount = 0;
	private int id;
	private String name;
	private Color colour;

	/** Bereits zurückgelegte Schritte im aktuellen Zug. */
	public int movedSteps;
	/** Anzahl der Schiffe des Spielers (Konstante, standardmäßig 5). */
	public int aShips = 5;
	/** Position des gesetzten Hafens des Spielers. */
	public Coordinates posHarbour;
	/** Array mit den Schiffen des Spielers. */
	public Ship[] ships = new Ship[aShips];
	/** Flag, ob es aktuell der Zug dieses Spielers ist. */
	public boolean itsTurn;
	private boolean isDefeated = false;
    
	/**
	 * Erstellt einen neuen Spieler mit automatisch zugewiesener ID.
	 * @param name Name des Spielers
	 * @param colour Farbe des Spielers
	 */
	public Player(String name, Color colour) {
		playerCount++;
		this.id = playerCount;
		this.name = name;
		this.colour = colour;
	}
	/**
	 * Konstruktor beim Laden eines Spielstands, ID wird übernommen.
	 * @param id gespeicherte ID
	 * @param name Name
	 * @param colour Farbe
	 */
	public Player(int id, String name, Color colour) {
		this.id = id;          
		this.name = name;
		this.colour = colour;
		ensurePlayerCount(id);
	}
    
	/**
	 * Prüft, ob der Spieler ausgeschieden ist (alle Schiffe versenkt) und
	 * markiert ihn als besiegt. Liefert true nur beim erstmaligen Ausscheiden.
	 * @return true, wenn Spieler gerade ausscheidet (erstmalig)
	 */
	public boolean playerDefeat() {

		int sunkenShips = 0;
        
		for(Ship s : ships) {
			if (s != null && s.isSunken == true) sunkenShips++;
            
		}
        
		if (sunkenShips == aShips && isDefeated == false) {
			isDefeated = true;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gibt zurück, ob der Spieler bereits als besiegt markiert ist.
	 * @return true, wenn der Spieler besiegt ist
	 */
	public boolean isDefeated() {
		return isDefeated;
	}
    
	/** Reduziert die globale Spielerzählung (intern verwendet beim Entfernen).
	 * @param lowerBy Anzahl, um die reduziert werden soll
	 */
	public static void setPlayerCount(int lowerBy) {
		playerCount -= lowerBy;
	}
    
	/**
	 * Stellt sicher, dass die globale Zählung mindestens die gegebene ID enthält.
	 * @param id Mindest-ID
	 */
	public static void ensurePlayerCount(int id) {
		if (playerCount < id) playerCount = id;
	}

	/**
	 * Liefert die aktuell vergebene höchste Spieler-ID (Anzahl erzeugter Spieler).
	 * @return Anzahl der Spieler bzw. höchste vergebene ID
	 */
	public static int getPlayerCount() {
		return playerCount;
	}

	/**
	 * Liefert die Farbe des Spielers.
	 * @return Color-Objekt
	 */
	public Color getColour() {
		return colour;
	}

	/**
	 * Liefert den Spielernamen.
	 * @return Name des Spielers
	 */
	public String getName() {
		return name;
	}

	/**
	 * Liefert die ID des Spielers.
	 * @return ID als int
	 */
	public int getId() {
		return id;
	}

	/**
	 * Erhöht die gezählten Schritte dieses Spielers um den angegebenen Wert.
	 * @param plus Anzahl Schritte, die hinzugefügt werden sollen
	 */
	public void setSteps(int plus) {
		movedSteps += plus;
	}
    
	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + ", colour=" + colour + ", posHarbour=" + posHarbour + ", itsTurn="
				+ itsTurn + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Player other = (Player) obj;
		return this.id == other.id; // Spieler sind gleich, wenn ihre id gleich ist
	}
}
