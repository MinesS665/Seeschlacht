package testController;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.*;
import view.*;
import model.*;

public class GameControllerTest {
	
	private GameMap map;
	private MainWindow view;
	private GameModel model;
	private GameController controller;
	
	@BeforeEach
	public void setUp() {
		
		Player.setPlayerCount(Player.getPlayerCount());
		
		map = new GameMap("/assets/Map4.png");
		view = new MainWindow();
		model = new GameModel(map);
		controller = new GameController(view, model, map);
		
		
		ArrayList<Player> testPlayers = new ArrayList<>();
		testPlayers.add(new Player("Spieler 1", new Color(200, 70, 70)));
		testPlayers.add(new Player("Spieler 2", new Color(200, 130, 70)));
		testPlayers.add(new Player("Spieler 3", new Color(210, 210, 30)));
		
		controller.savePlayers(testPlayers);
	}
	
	@Test
	public void constructorConnection() {
		
		GameMap dummyMap = new GameMap("/assets/Map4.png");
		MainWindow dummyView = new MainWindow();
		GameModel dummyModel = new GameModel(dummyMap);
		
		GameController controller = new GameController(dummyView, dummyModel, dummyMap);
		
		assertNotNull(controller);
		assertEquals(controller, dummyView.getController());
	}
	
	@Test
	public void nextMoveRegular() {
		
		//State manipulieren
		controller.setStateTesting(State.MOVE);
		controller.players.get(0).movedSteps = 6;
		
		controller.nextMove();
		
		assertEquals(0, controller.players.get(0).movedSteps);
		assertEquals(controller.getCurPlayer(), controller.players.get(1));
		assertEquals(controller.getState(), State.SELECT);
		
	}
	
	@Test
	public void nextMoveLastPlayer() {
		
		controller.setCurPlayerTesting(controller.players.get(2));
		
		controller.nextMove();
		
		assertEquals(controller.players.get(0), controller.getCurPlayer());
		assertEquals(controller.getState(), State.SELECT);
	}
	
	@Test
	public void nextMovePlayerDefeated() {
		
		Player p2 = controller.players.get(1);
		
		p2.ships[0] = new Ship(new Coordinates(0,0));
		p2.ships[1] = new Ship(new Coordinates(0,0));
		p2.ships[2] = new Ship(new Coordinates(0,0));
		p2.ships[3] = new Ship(new Coordinates(0,0));
		p2.ships[4] = new Ship(new Coordinates(0,0));
		
		
		for (int i = 0; i < 5; i++) {
	        p2.ships[i].isSunken = true;
	    }
		
		p2.playerDefeat();
		
		controller.nextMove();
		
		assertEquals(controller.players.get(2), controller.getCurPlayer());
	}
	
	@Test
	public void handleMapClick_inPlaceHarbour() {
		
	    controller.setStateTesting(State.PLACE_HARBOUR);
	    Coordinates waterPos = new Coordinates(40, 20);
	    
	    controller.handleMapCLick(waterPos);

	    assertNotNull(controller.getCurPlayer().posHabour);
	}
	
	@Test
	public void handleMapClick_selectShipSuccess() {
	    controller.setStateTesting(State.SELECT);
	    Player p1 = controller.getCurPlayer();
	    
	    Coordinates shipPos = new Coordinates(40, 20);
	    p1.ships[0] = new Ship(shipPos);
	    p1.ships[0].isSunken = false;
	    
	    controller.handleMapCLick(shipPos);
	    
	    assertTrue(p1.ships[0].isSelected);
	    assertEquals(State.MOVE, controller.getState());
	}
	
	@Test
	public void handleMapClick_moveStepsExceeded() {
		
	    controller.setStateTesting(State.MOVE);
	    Player p1 = controller.getCurPlayer();
	    Coordinates startPos = new Coordinates(10,30);
	    Coordinates endPos = new Coordinates(2, 3);
	    
	    Ship testShip = new Ship(startPos);
	    p1.movedSteps = controller.getMaxSteps();
	    

	    controller.handleMapCLick(endPos);

	    assertEquals(startPos, testShip.pos);
	}
	
	@Test
	public void selectShip_successfulExactClick() {
	    
	    controller.setStateTesting(State.SELECT);
	    Player activePlayer = controller.getCurPlayer();
	    
	    Coordinates shipPosition = new Coordinates(40, 20);
	    Ship expectedShip = new Ship(shipPosition);
	    activePlayer.ships[0] = expectedShip;
	    
	    controller.handleMapCLick(shipPosition);
	    
	    assertEquals(expectedShip, controller.getCurShipTesting());
	    assertTrue(expectedShip.isSelected);
	    assertEquals(State.MOVE, controller.getState());
	}

	@Test
	public void selectShip_ignoredIfSunken() {
	    
	    controller.setStateTesting(State.SELECT);
	    Player activePlayer = controller.getCurPlayer();
	    
	    
	    Coordinates shipPosition = new Coordinates(40, 20);
	    Ship sunkenShip = new Ship(shipPosition);
	    sunkenShip.isSunken = true;
	    activePlayer.ships[0] = sunkenShip;
	    
	    controller.handleMapCLick(shipPosition);
	    
	    assertNull(controller.getCurShipTesting());
	    assertFalse(sunkenShip.isSelected);
	    assertEquals(State.SELECT, controller.getState());
	}
	
	@Test
	public void attackStart_withoutSelectedShip() {

		controller.setStateTesting(State.SELECT);
		
		controller.attackStart();
		
		assertEquals(State.SELECT, controller.getState());
	}

	@Test
	public void scanDamage_rammingOwnShipSinksIt() {
		
		Player p1 = controller.getCurPlayer();
		Player p2 = controller.players.get(1);
		
		Coordinates collisionPos = new Coordinates(20, 30);
		Coordinates dummyHarbour = new Coordinates(50, 30);
		
		// Hafen-Koordinaten setzen, damit saveGame() nicht abstürzt
		for (Player p : controller.players) {
			p.posHabour = dummyHarbour;
		}
		
		// Alle 5 Schiffe für alle Spieler initialisieren, um NullPointer zu verhindern
		for (int i = 0; i < 5; i++) {
			p1.ships[i] = new Ship(new Coordinates(20, 20));
			p2.ships[i] = new Ship(new Coordinates(99, 99)); 
			controller.players.get(2).ships[i] = new Ship(new Coordinates(50, 50)); 
		}

		p1.ships[0].pos = collisionPos;
		p2.ships[0].pos = collisionPos;
		
		
		controller.setCurShipTesting(p1.ships[0]);
		controller.setStateTesting(State.MOVE);
		
		// Ramm-Punkt übergeben
		ArrayList<Coordinates> points = new ArrayList<>();
		points.add(collisionPos);
		
		controller.scanDamage(points);
		
		assertTrue(p2.ships[0].isSunken);
		assertTrue(p1.ships[0].isSunken);
	}

	@Test
	public void placeHarbour_notEnoughWaterSpace() {

		controller.setStateTesting(State.PLACE_HARBOUR);
		
		Coordinates edgePos = new Coordinates(10, 3);
		
		controller.handleMapCLick(edgePos);
		
		assertNull(controller.getCurPlayer().posHabour);
	}
}
