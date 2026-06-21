package controller;


import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import dom.Manipulator;
import dom.Reader;
import dom.SaveGame;
import model.*;
import view.MainWindow;


/**
 * Haupt-Controller der Anwendung "MarineBattle".
 *
 * <p>Der Controller koordiniert Spielablauf, Benutzerinteraktionen und Model/View-Komponenten:
 * - Verarbeiten von Klicks auf die Karte
 * - Auswahl, Bewegung und Angriff von Schiffen
 * - Platzierung von Häfen in der Startrunde
 * - Laden/Speichern des Spielstands
 * - Wechseln der Spielzüge und Beenden des Spiels
 *
 *
 */
public class GameController {

	/** Referenz auf das GUI-Hauptfenster (View). */
	private MainWindow view;

	/** Referenz auf das Spielmodell (Model). */
	private GameModel model;

	/** Referenz auf die Spielkarte (Map), wird für Kollisionen/Tile-Abfragen genutzt. */
	private GameMap map;

	/** Aktueller Spielzustand (Platzieren/Selektieren/Bewegen/Angreifen/Ende). */
	private State curState = State.PLACE_HARBOUR;

	/** Aktuell am Zug befindlicher Spieler. */
	private Player curPlayer;

	/** Aktuell ausgewähltes Schiff (kann null sein). */
	private Ship curShip;

	/** Liste aller Spieler im aktuellen Spiel. Öffentlich zugreifbar, da andere Komponenten
	 *  (z. B. View/Model) darauf zugreifen. */
	public ArrayList<Player> players;

	/** Maximale Anzahl Schritte, die ein Spieler pro Zug zurücklegen kann. */
	private int maxSteps = 10;
	
	/**
	 * Erzeugt einen neuen GameController und verbindet Model und View.
	 *
	 * @param view  die Haupt-View, über die Benutzerinteraktionen erfolgen
	 * @param model das Spielmodell, das Spieler- und Spielzustandsdaten enthält
	 * @param map   die Spielkarte (GameMap), die für Kollisionen und Tile-Abfragen verwendet wird
	 */
	public GameController (MainWindow view, GameModel model, model.GameMap map) {
		
		this.view = view;
		this.model = model;
		this.map = map;
		
		view.setController(this);
		view.createMap(map);
	}
	
	/**
	 * Übernimmt die ausgewählten Spieler ins Model und startet das Spiel.
	 *
	 * @param players Liste der Spieler, wie sie im Auswahl-Dialog festgelegt wurde
	 */
	public void savePlayers(ArrayList<Player> players) {
		
		model.updatePlayers(players);
		startGame();
	}
	
	/**
	 * Initialisiert Spielstart: lädt Spieler aus dem Model, setzt den Startzustand
	 * und startet die Platzierungsrunde für den ersten Spieler.
	 */
	private void startGame() {
		
		players = model.getPlayers();
		curState = State.PLACE_HARBOUR;
		
		//Ersten Spieler auswählen
		curPlayer = players.get(0);
		
		view.placeHarbour(curPlayer);
	}
	
	/**
	 * Wechselt zum nächsten Zug.
	 *
	 * Setzt temporäre Zugdaten (z. B. movedSteps) zurück, wählt den nächsten aktiven Spieler
	 * und passt den Spielzustand an (Platzierungsrunde vs. regulärer Spielbetrieb).
	 */
	public void nextMove() {
		
		curPlayer.movedSteps = 0;
		curShip = null;
		undoSelect();
		view.repaint();
		
		if (endRequired(false)) return;
		
		int currentPlayerIndex = players.indexOf(curPlayer);
		boolean harbourRoundFinished = curState == State.PLACE_HARBOUR && currentPlayerIndex == players.size() - 1;
		
		//Aktuellen Spieler festlegen und besiegte Spieler überspringen
		int nextPlayerIndex = currentPlayerIndex;
		
		for (int i = 0; i < players.size(); i++) {
			nextPlayerIndex = (nextPlayerIndex + 1) % players.size();
			curPlayer = players.get(nextPlayerIndex);
			
			if (!curPlayer.isDefeated()) break;
		}
		
		if (harbourRoundFinished || curState != State.PLACE_HARBOUR) {
			curState = State.SELECT;
		}
		
		//Hafen platzieren? Enstsprechnde Anzeige aufrufen
		if (curState == State.PLACE_HARBOUR) view.placeHarbour(curPlayer);
		else view.nextMove(curPlayer);
	}

	/**
	 * Verarbeitet einen Klick auf die Karte.
	 *
	 * Abhängig vom aktuellen Spielzustand wird der Klick als Hafenplatzierung,
	 * Schiffsauswahl oder Bewegungsziel interpretiert. Landklicks sind außerhalb
	 * des MOVE-Zustands nicht erlaubt.
	 *
	 * @param clickPos Position des Klicks in Kartenkoordinaten
	 */
	public void handleMapClick(Coordinates clickPos) {
		
		//Land blockieren
		if(model.getMap().getTile(clickPos) != TileTyp.WATER && curState != State.MOVE) {
			JOptionPane.showMessageDialog(view, "Pah, Landratte");
			return;
		} 
		
		//Klick entsprechend dem Spielstatus interpretieren 
		if (curState == State.PLACE_HARBOUR) {
			placeHarbour(clickPos);
		}
		else if (curState == State.SELECT) {
			
			curShip = selectShip(clickPos);	
			
			if (curShip != null) {
				curShip.isSelected = true;
				curState = State.MOVE;
			}
		} else if (curState == State.MOVE) {
			
			if (curPlayer.movedSteps < maxSteps) {
				moveShip(clickPos, curShip);
			} else {
				view.problem("Du hast dich bereits " + maxSteps + " Einheiten weit bewegt.");
			}
		}
		
		view.repaint();
	}

	/**
	 * Wählt ein Schiff des aktuellen Spielers aus, das sich in der Nähe des Klicks befindet.
	 *
	 * Wenn kein geeignetes Schiff gefunden wird oder das Schiff bereits versenkt ist,
	 * wird null zurückgegeben und eine passende Fehlermeldung angezeigt.
	 *
	 * @param click Koordinate des Benutzerklicks
	 * @return das ausgewählte Ship-Objekt oder null, falls keine Auswahl möglich ist
	 */
	private Ship selectShip(Coordinates click) {

		int shipIndex = -1;
		
		//Nach Schiff in der Nähe suchen
		for (int i = 0; i<5; i++) {
			if (curPlayer.ships[i] != null) { 
		        if ((click.isClose(curPlayer.ships[i].pos) || click.isClose(curPlayer.ships[i].secPos)) && !curPlayer.ships[i].isSunken) {
		            shipIndex = i;
		        }
			}
		}
		
		if (shipIndex == -1) return null;
		curShip = curPlayer.ships[shipIndex];
		
		if (curShip.isSunken == true) {
			view.problem("Dieses Schiff steht dir nicht mehr zur Verfügung");
			return null;
		}
		
		undoSelect();
		
		return curShip;
	}

	/**
	 * Bewegt das übergebene Schiff in Richtung des Klicks.
	 *
	 * Bewegungen werden auf eine maximale Reichweite (moveRange) normiert,
	 * Kollisionen mit Land, Kartenrändern und anderen Regeln werden geprüft.
	 * Nach einer erfolgreichen Bewegung werden Schritte aktualisiert und
	 * umliegende Felder auf Schäden/Angriffe gescannt.
	 *
	 * @param click   Zielkoordinate des Bewegungswunsches
	 * @param curShip das zu bewegende Schiff (muss nicht null sein)
	 */
	private void moveShip(Coordinates click, Ship curShip) {
		
		int moveRange = 2;
		int tolerance = 2;
		
		Coordinates startPos = curShip.pos;
		
		//Richtungsvektor mit Länge 1 bestimmen
		int dX = Integer.compare(click.getX(), curShip.pos.getX());
		int dY = Integer.compare(click.getY(), curShip.pos.getY());
			
		curShip.isSelected = true;
		
		//Entfernung Klick - Schiff bestimmen
		int diffX = Math.abs(click.getX() - curShip.pos.getX());
		int diffY = Math.abs(click.getY() - curShip.pos.getY());

		//Gerade Bewegung erzwingen wenn Klick fast gerade aus war
		if (diffY <= tolerance && diffX > tolerance) {
	        dY = 0;
	    } else if (diffX <= tolerance && diffY > tolerance) {
	        dX = 0;
	    }
		
		//Gültigkeit prüfen
		if (dX == 0 && dY == 0) {
		    return;
		}

		int nextX = curShip.pos.getX();
		int nextY = curShip.pos.getY();

		//moveRange x einen Schritt Richtung Klick machen
		for (int i = 0; i < moveRange; i++) {
		    nextX += dX;
		    nextY += dY;
		    
		    //Außerhalb der Map blockieren
		    if (nextX < 0 || nextX >= map.getWidth() || nextY < 0 || nextY >= map.getHeight()) {
		        break; 
		    }
		    
		    //Auf Land blockieren
		    if (map.getTile(nextX, nextY) == TileTyp.WATER) {
		        curShip.secPos = curShip.pos;
		        curShip.pos = new Coordinates(nextX, nextY);
		    } else {
		        break; 
		    }
		}
		
		if (!startPos.equals(curShip.pos)) {
			//Schritte aktualisieren
			curPlayer.setSteps(1);
			view.getControlPanel().updateSteps(curPlayer);
		}
		
		int[] offset = {0, 1, 0, -1};
		
		ArrayList<Coordinates> surrounding = new ArrayList<>();
		
		for (int x : offset) {
			for (int y : offset) {
				surrounding.add(new Coordinates(curShip.pos.getX() + x, curShip.pos.getY() + y));	
				surrounding.add(new Coordinates(curShip.secPos.getX() + x, curShip.secPos.getY() + y));
			}
		}
		
		view.repaint();
		
		scanDamage(surrounding);
		
	}

	/**
	 * Platziert den Hafen des aktuellen Spielers an der angegebenen Position
	 * und erzeugt die Startschiffe in einer Formation vor dem Hafen.
	 *
	 * Es wird überprüft, ob im Umfeld genügend Wasser vorhanden ist; falls nicht,
	 * wird die Platzierung abgebrochen und eine Meldung angezeigt.
	 *
	 * @param c Koordinate, an der der Hafen gesetzt werden soll
	 */
	private void placeHarbour(Coordinates c) {
		
		int x = c.getX();
		int y = c.getY();
		
		//prüfen, ob im Umkreis genug Wasser ist 
		for (int i = -5; i<=5; i++) {
			for (int j = -2; j<=2; j++) {
				if(model.getMap().getTile(x+i,y+j) != TileTyp.WATER) {
					JOptionPane.showMessageDialog(view, "Nicht genug Platz für deine Flotte. Entferne dich weiter vom Ufer");
					return;
				}
			}
		}
		
		//Hafen setzten und Schiffe generieren
		curPlayer.posHarbour = new Coordinates(x,y);
		
		curPlayer.ships[0] = new Ship(new Coordinates(x-4,y));
		curPlayer.ships[1] = new Ship(new Coordinates(x-2,y));
		curPlayer.ships[2] = new Ship(new Coordinates(x,y));
		curPlayer.ships[3] = new Ship(new Coordinates(x+2,y));
		curPlayer.ships[4] = new Ship(new Coordinates(x+4,y));
		
		view.repaint();
		view.getControlPanel().setPlaced(true);
	}
	
	/**
	 * Leitet den Angriffsmodus für das aktuell ausgewählte Schiff ein.
	 *
	 * Falls kein Schiff ausgewählt wurde, wird eine Fehlermeldung angezeigt
	 * und der Zustand bleibt im SELECT-Modus.
	 */
	public void attackStart () {
		
		curState = State.ATTACK;
		
		if (curShip == null) {
			view.problem("Wähle zuerst dein Schiff");
			curState = State.SELECT;
			return;
		} else view.attack();
	}
	
	/**
	 * Prüft die übergebenen Punkte auf Treffer gegen feindliche Schiffe und
	 * wertet entsprechend die Folgen aus (Versenkung, Spielerbesiegt, etc.).
	 *
	 * Die Methode behandelt sowohl Ramm-Aktionen während der Bewegung (MOVE)
	 * als auch reguläre Angriffe (ATTACK). Bei Spielende-Zuständen wird
	 * gegebenenfalls das Spiel beendet oder der nächste Zug eingeleitet.
	 *
	 * @param points Liste von Koordinaten, die auf Treffer überprüft werden sollen
	 */
	public void scanDamage(ArrayList<Coordinates> points) {
		
		//Für jeden Punkt überprüfen, ob ein fremdes Schiff getroffen wurde
		for(Coordinates c : points) {
			for(Player p : players) {
				if(p != curPlayer && p.isDefeated() == false) {
					for(Ship s : p.ships) {
						if(s != null && (s.pos.equals(c)  || s.secPos.equals(c))) {
							s.isSunken = true;
							
							//Rammen eines Schiffes beim Fahren
							if (curState == State.MOVE) {
								curShip.isSunken = true;
								view.infoScreen("Das schaffen nicht viele... IHR EIGENES SCHIFF ZU VERSENKEN. Halte besser mehr Abstand zu gegenerischen Schiffen");
							
								if (p.playerDefeat()) {
									view.infoScreen(p.getName(), State.ATTACK);
								}
								saveGame();
								if (!endRequired(true)) {
									nextMove();
								}
								return;
							}
							
							//Angriff auf ein Schiff
							if (curState == State.ATTACK) {
								
								if (p.playerDefeat() == true) {
		                            view.infoScreen(p.getName(), curState);
		                        }
		                      
		                    }
						}
					}
				}
			}
		}
		
		saveGame();
		
		
		if (curState == State.ATTACK) {
			
			if (!endRequired(false)) {
				view.attackFinished();
				nextMove();
			}
		}
	}
	
	/**
	 * Überprüft, ob das Spiel beendet werden muss (Sieg / Unentschieden).
	 *
	 * @param tie gibt an, ob ein Unentschieden geprüft werden soll (z. B. nach
	 *            Kollisionen beiderseits)
	 * @return true, wenn das Spiel beendet wurde und kein weiterer Zug nötig ist
	 */
	private boolean endRequired(boolean tie) {
		//Verbleibende Spieler prüfen
		int activePlayers = 0;
		Player winner = null;
				
		for(Player p : players) {
			if(!p.isDefeated()) {
				activePlayers++;
				winner = p;
			}
		}
		
		if (activePlayers == 0) {
			endGame(true);
			return true;
		}
		else if (activePlayers == 1) {
			curPlayer = winner;
			endGame(false);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Hebt die Auswahl aller Schiffe aller Spieler auf (isSelected = false).
	 *
	 * Diese Hilfsmethode wird beim Zugwechsel oder vor einer neuen Auswahl
	 * verwendet, um den Auswahlzustand zu bereinigen.
	 */
	private void undoSelect() {
		
		for(Player p : players) {
			for (int i = 0; i<5; i++) {
				if (p.ships[i] != null) p.ships[i].isSelected = false;
			}
		}
	}

	/**
	 * Führt die Aufräumarbeiten beim Spielende durch, entfernt gespeicherte Daten
	 * und zeigt das Ergebnis an. Anschließend wird die JVM beendet.
	 *
	 * @param tie true = Unentschieden, false = Gewinner vorhanden
	 */
	private void endGame(boolean tie) {
		
		curState = State.END;
		
		Manipulator man = new Manipulator();
		man.clearSaveGame();
		
		if (tie) {
			view.infoScreen("Ganz in Piratenmanier: Es gibt nur Verlierer");
		} else view.infoScreen(curPlayer.getName(), curState);
		
		System.exit(0);
	}

	/**
	 * Liefert den aktuell am Zug befindlichen Spieler.
	 *
	 * @return aktueller Player oder null, falls kein Spieler gesetzt ist
	 */
	public Player getCurPlayer() {
		return curPlayer;
	}

	/**
	 * Liefert den aktuellen Spielzustand.
	 *
	 * @return aktueller State
	 */
	public State getState() {
		return curState;
	}

	/**
	 * Liefert die maximale Anzahl an Schritten, die ein Spieler pro Zug ausführen darf.
	 *
	 * @return maximale Schrittanzahl pro Zug
	 */
	public int getMaxSteps() {
		return maxSteps;
	}

	/**
	 * Liefert die Position des aktuell ausgewählten Schiffs.
	 *
	 * @return Position des Schiffes oder null, wenn kein Schiff ausgewählt ist
	 */
	public Coordinates getCurShipPos() {
        
		if (curShip == null) return null;
        
		return curShip.pos;
	}

	/**
	 * Speichert den aktuellen Spielstand über die DOM-Manipulator-Klasse.
	 *
	 * @return true bei erfolgreichem Speichern, sonst false
	 */
	public boolean saveGame() {
        
		SaveGame game = new SaveGame(null, players, curPlayer.getId());
		Manipulator man = new Manipulator();
		return man.saveGame(game);
	}
	
	/**
	 * Lädt einen gespeicherten Spielstand aus der XML-Datei.
	 *
	 * Die Methode übernimmt die Spieler in das Model, stellt den aktuell gespeicherten
	 * Spieler als aktuellen Spieler ein und wechselt in den SELECT-Zustand. Bei Fehlern
	 * (fehlende oder beschädigte Datei) wird eine Benachrichtigung angezeigt und false
	 * zurückgegeben.
	 *
	 * @return true, wenn der Spielstand erfolgreich geladen und übernommen wurde
	 */
	public boolean loadGame(){
		Reader xmlReader = new Reader();
        
		try {
			SaveGame loadedGame = xmlReader.getSaveGame();
            
			this.players = loadedGame.players;
			this.model.updatePlayers(this.players);
			this.curPlayer = null;
            
			for (Player p : this.players) {
				if (p.getId() == loadedGame.curPlayerId) {
					this.curPlayer = p;
					break;
				}
			}
            
			if (this.curPlayer == null) {
				view.problem("Der gespeicherte aktuelle Spieler wurde nicht gefunden.");
				return false;
			}
            
			curState = State.SELECT;
			view.nextMove(curPlayer); 
            
            
			view.revalidate();
			view.repaint();
            
			return true;

        
		} catch (NullPointerException e) {
			view.problem("Kein alter Spielstand vorhanden");
		} catch (IOException e) {
			view.problem("Speicherdatei konnte nicht gelesen werden oder fehlt!");
		} catch (SAXException | ParserConfigurationException e) {
			view.problem("Der Spielstand ist beschädigt (ungültiges XML)!");
		}
		return false;
	}
	
	/** Hilfsmethoden für Tests: Zustand und aktuelle Objekte setzen/auslesen. */
	/**
	 * Test-Hilfsmethode: setzt einen internen Spielzustand (nur für Tests).
	 * @param state neuer interner Zustand
	 */
	public void setStateTesting(State state) { this.curState = state;}

	/**
	 * Test-Hilfsmethode: setzt den aktuellen Spieler (nur für Tests).
	 * @param p Player-Instanz, die als aktuell gesetzt werden soll
	 */
	public void setCurPlayerTesting(Player p) { this.curPlayer = p;}

	/**
	 * Test-Hilfsmethode: liefert das aktuell ausgewählte Schiff zurück.
	 * @return aktuelles Schiff oder null
	 */
	public Ship getCurShipTesting() {return curShip;}

	/**
	 * Test-Hilfsmethode: setzt das aktuell ausgewählte Schiff.
	 * @param s Schiff, das als aktuell ausgewählt gesetzt werden soll
	 */
	public void setCurShipTesting(Ship s) { this.curShip = s;}
}
