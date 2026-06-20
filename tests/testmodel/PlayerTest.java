package testmodel;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Coordinates;
import model.Player;
import model.Ship;

class PlayerTest {

    @BeforeEach
    void resetPlayerCount() {
        //Da playerCount static ist vor jedem Test zurücksetzten
        Player.setPlayerCount(Player.getPlayerCount()); 
    }

    @Test
    void testPlayerConstructorAndIDIncrement() {
        Player p1 = new Player("Alice", Color.BLUE);
        Player p2 = new Player("Bob", Color.RED);

        assertEquals("Alice", p1.getName());
        assertEquals(Color.BLUE, p1.getColour());
        
        // Testet, ob die id automatisch hochgezählt wird
        assertTrue(p2.getId() > p1.getId(), "Die id des zweiten Spielers muss höher sein.");
    }

    @Test
    void testLoadConstructor() {
        // Testet den Konstruktor für Spielstände (manuelle id-Vergabe)
        Player p = new Player(99, "Charlie", Color.GREEN);
        
        assertEquals(99, p.getId());
        assertEquals("Charlie", p.getName());
    }

    @Test
    void testSetSteps() {
        Player p = new Player("Alice", Color.BLUE);
        assertEquals(0, p.movedSteps);

        p.setSteps(5);
        assertEquals(5, p.movedSteps);

        p.setSteps(3);
        assertEquals(8, p.movedSteps);
    }

    @Test
    void testPlayerDefeatNotDefeatedInitially() {
        Player p = new Player("Alice", Color.BLUE);
        
        // Schiffe initialisieren
        for (int i = 0; i < p.aShips; i++) {
            p.ships[i] = new Ship(new Coordinates(30, 50));
            p.ships[i].isSunken = false;
        }

        assertFalse(p.playerDefeat(), "Spieler sollte nicht besiegt sein, wenn Schiffe intakt sind.");
        assertFalse(p.isDefeated());
    }

    @Test
    void testPlayerDefeatTriggersExactlyOnce() {
        Player p = new Player("Alice", Color.BLUE);
        
        // Alle Schiffe als versenkt markieren
        for (int i = 0; i < p.aShips; i++) {
            p.ships[i] = new Ship(new Coordinates(30, 50));
            p.ships[i].isSunken = true;
        }

        // Das erste Mal: Methode sollte TRUE zurückgeben
        assertTrue(p.playerDefeat(), "Sollte true zurückgeben, wenn der Spieler DAS ERSTE MAL ausscheidet.");
        assertTrue(p.isDefeated(), "isDefeated sollte nun true sein.");

        // Das zweite Mal: Methode sollte FALSE zurückgeben, da bereits als besiegt markiert
        assertFalse(p.playerDefeat(), "Sollte false zurückgeben beim erneuten Aufruf.");
    }

}
