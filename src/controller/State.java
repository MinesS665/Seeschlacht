package controller;

/**
 * Mögliche Zustände des Spiels, die vom {@link controller.GameController}
 * ausgewertet werden (z. B. Platzierungsrunde, Auswahl, Bewegung, Angriff, Ende).
 */
public enum State {

	/** Hafenplatzierung in der Startphase. */
	PLACE_HARBOUR,
	/** Schiffsauswahl-Modus. */
	SELECT,
	/** Bewegungsmodus. */
	MOVE,
	/** Angriffmodus (Zielauswahl / Schießen). */
	ATTACK,
	/** Spiel wurde beendet. */
	END
}