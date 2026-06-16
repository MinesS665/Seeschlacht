package controller;


import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import DOM.Manipulator;
import DOM.Reader;
import DOM.SaveGame;
import org.xml.sax.SAXException;
import model.*;
import view.MainWindow;


public class GameController {
	
	private MainWindow view;
	private GameModel model;
	private GameMap map;
	
	private State curState = State.PLACE_HARBOUR;
	private Player curPlayer;
	private Ship curShip;
	public ArrayList<Player> players;

	private int maxSteps = 10;
	
	//Konstruktor
	public GameController (MainWindow view, GameModel model, model.GameMap map) {
		
		this.view = view;
		this.model = model;
		this.map = map;
		
		view.setController(this);
		view.CreateMap(map);
	}
	
	//Spieler speichern, Spiel starten
	public void savePlayers(ArrayList<Player> players) {
		
		model.UpdatePlayers(players);
		startGame();
	}
	
	//Spiel Starten
	private void startGame() {
		
		players = model.getPlayers();
		curState = State.PLACE_HARBOUR;
		
		//Ersten Spieler auswählen
		curPlayer = players.get(0);
		
		view.PlaceHarbour(curPlayer);
	}
	
	//Neuen Zug beginnen
	public void nextMove() {
		
		curPlayer.movedSteps = 0;
		curShip = null; //TODO: Exception
		undoSelect();

		//Aktuellen Spieler festlegen
		if (curPlayer.getID() == players.size()) {
			
			curState  = State.SELECT;
			curPlayer = players.get(0);
			
		} else {
			
			curPlayer = players.get(curPlayer.getID());
			
			if (curState != State.PLACE_HARBOUR) {
				curState = State.SELECT;
			}
		}
		
		//Wenn Spieler bereits besiegt ist diesen überspringen
		if (curPlayer.isDefeated() == true) {
			nextMove();
		}
		
		//Hafen platzieren? Enstsprechnde Anzeige aufrufen
		if (curState == State.PLACE_HARBOUR) view.PlaceHarbour(curPlayer);
		else view.NextMove(curPlayer);
	}

	//Auf Klicks reagieren
	public void handleMapCLick(Coordinates clickPos) {
		
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

	//Schiff auswählen
	private Ship selectShip(Coordinates click) {

		//TODO Exception bauen
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

	//Bewegung des Shiffes steuern
	private void moveShip(Coordinates click, Ship curShip) {
		
		int moveRange = 2;
		int tolerance = 2;
		
		//Richtungsvektor mit Länge 1 bestimmen
		int dX = Integer.compare(click.getX(), curShip.pos.getX());
		int dY = Integer.compare(click.getY(), curShip.pos.getY());
			
		curShip.isSelected = true;
		
		//Entfernung Klick - Schiff bestimmen
		int diffX = Math.abs(click.getX() - curShip.pos.getX());
		int diffY = Math.abs(click.getY() - curShip.pos.getY());

		//Gerade Bewegung erzeingen wenn Klick fast gerade aus war
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
		
		//Schritte aktualisieren
		curPlayer.setSteps(1);
		view.getControlPanel().updateSteps(curPlayer);
	}

	//Startrunde koordinieren
	private void placeHarbour(Coordinates c) {
		
		int x = c.getX();
		int y = c.getY();
		
		//prüfen, ob im Umkreis genaug Wasser ist 
		for (int i = -5; i<=5; i++) {
			for (int j = -2; j<=2; j++) {
				if(model.getMap().getTile(x+i,y+j) != TileTyp.WATER) {
					JOptionPane.showMessageDialog(view, "Nicht genug Platz für deine Flotte. Entferne dich weiter vom Ufer");
					return;
				}
			}
		}
		
		//Hafen setzten und Schiffe generieren
		curPlayer.posHabour = new Coordinates(x,y);
		
		curPlayer.ships[0] = new Ship(curPlayer, new Coordinates(x-4,y));
		curPlayer.ships[1] = new Ship(curPlayer, new Coordinates(x-2,y));
		curPlayer.ships[2] = new Ship(curPlayer, new Coordinates(x,y));
		curPlayer.ships[3] = new Ship(curPlayer, new Coordinates(x+2,y));
		curPlayer.ships[4] = new Ship(curPlayer, new Coordinates(x+4,y));
		
		view.repaint();
		view.getControlPanel().setPlaced(true);
	}
	
	//Angriff einleiten
	public void attackStart () {
		
		if (curShip == null) {
			view.problem("Wähle zuert dein Schiff");
			return;
		}

		view.Attack();
	}
	
	//Ergebnis des Angriffes auswerten
	public void attackFinished(ArrayList<Coordinates> points) {
		
		view.AttackFinished();
		
		//Für jeden Punkt überprüfen, ob ein fremdes Shiff getroffen wurde
		for(Coordinates c : points) {
			for(Player p : players) {
				if(p != curPlayer && p.isDefeated() == false) {
					for(Ship s : p.ships) {
						if((s.pos.equals(c)  || s.secPos.equals(c)) && !s.isSunken) {
							s.isSunken = true;
							if (p.playerDefeat() == true) {
								view.infoScreen(p.getName(), curState);
							}
						}
					}
				}
			}
		}
		
		//Verbleibende Spieler prüfen
		int deafPlayers = 0;
		
		for(Player p : players) {
			if(p.isDefeated()) deafPlayers++;
		}
		
		if (deafPlayers >= players.size()-1) endGame();
		
		saveGame();
		nextMove();
	}
	
	//Alle Schiffe abwählen
	private void undoSelect() {
		
		for(Player p : players) {
			for (int i = 0; i<5; i++) {
				if (p.ships[i] != null) p.ships[i].isSelected = false;
			}
		}
	}

	//Spiel beenden
	private void endGame() {
		
		curState = State.END;
		
		Manipulator man = new Manipulator();
		man.clearSaveGame();
		
		view.infoScreen(curPlayer.getName(), curState);
		System.exit(0);
	}

	public Player getCurPlayer() {
		return curPlayer;
	}

	public State getState() {
		return curState;
	}

	public int getMaxSteps() {
		return maxSteps;
	}

	public Coordinates getCurShipPos() {
		
		if (curShip == null) return null;
		
		return curShip.pos;
	}

	public boolean saveGame() {
		
		SaveGame game = new SaveGame(null, players, curPlayer.getID());
		Manipulator man = new Manipulator();
		man.saveGame(game);
		
		
		return true;
	}
	
	public boolean loadGame(){
		Reader xmlReader = new Reader();
	    
	    try {
	        SaveGame loadedGame = xmlReader.getSaveGame();
	        
	        this.players = loadedGame.players;
	        this.curPlayer = players.get(loadedGame.curPlayerID-1);
	        this.players = loadedGame.players;
	        
	        this.model.UpdatePlayers(this.players);
	        
	        for (Player p : this.players) {
	            if (p.getID() == loadedGame.curPlayerID) {
	                this.curPlayer = p;
	                break;
	            }
	        }
	        
	        curState = State.SELECT;
	        view.NextMove(curPlayer); 
	        
	        
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
	
	
}
