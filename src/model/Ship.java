package model;

/**
 * Datenklasse für ein Schiff auf der Karte.
 *
 * <p>Ein Schiff besitzt eine Hauptposition (pos) und eine Sekundärposition (secPos),
 * die die zweite Kachel des Schiffs darstellt. Zusätzlich werden Statusflags für
 * Auswahl und Versenkung gehalten.
 */
public class Ship {
    
	/** Hauptposition (Kopf) des Schiffs. */
	public Coordinates pos;
	/** Sekundärposition des Schiffs (zweite Kachel). */
	public Coordinates secPos;
	/** Markiert, ob das Schiff aktuell ausgewählt ist. */
	public boolean isSelected;
	/** Markiert, ob das Schiff versenkt wurde. */
	public boolean isSunken = false;

	/**
	 * Erstellt ein Schiff mit gegebener Kopfposition; die Sekundärposition wird darunter gesetzt.
	 * @param pos Kopfposition des Schiffs
	 */
	public Ship (Coordinates pos) {

		this.pos = pos;
		secPos = pos.addValue(0, 1);
		isSelected = false;
	}
    
	/**
	 * Überladener Konstruktor mit expliziter Sekundärposition.
	 * @param pos Kopfposition des Schiffs
	 * @param secPos Sekundärposition des Schiffs (zweite Kachel)
	 */
	public Ship (Coordinates pos, Coordinates secPos) {
		this.pos = pos;
		this.secPos = secPos;
		isSelected = false;
        
		if (!this.secPos.isClose(this.pos)) this.secPos = pos.addValue(0, 1);
        
	}

	@Override
	public String toString() {
		return "Ship [pos=" + pos + ", isSelected=" + isSelected + ", isSunken=" + isSunken + "]";
	}
    
}
