package testModel;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.GameMap;
import model.GameModel;
import model.Player;

class GameModelTest {

    private GameModel gameModel;
    private GameMap dummyMap; // Kann für diese Tests null sein, da das Model die Map nur durchreicht

    @BeforeEach
    void setUp() {
        // Vor jedem Test starten wir mit einem frischen GameModel
        gameModel = new GameModel(dummyMap);
        
        Player.setPlayerCount(Player.getPlayerCount());
    }

    @Test
    void testUpdatePlayersAddNewPlayers() {
        // 1. Erstelle zwei neue Testspieler
        Player p1 = new Player("Alice", Color.BLUE);
        Player p2 = new Player("Bob", Color.RED);
        ArrayList<Player> newPlayers = new ArrayList<>(Arrays.asList(p1, p2));

        // 2. Führe das Update aus
        gameModel.UpdatePlayers(newPlayers);

        // 3. Überprüfe, ob die Spieler hinzugefügt wurden
        assertEquals(2, gameModel.getPlayers().size(), "Es sollten 2 Spieler in der Liste sein.");
        assertTrue(gameModel.getPlayers().contains(p1), "Alice sollte in der Liste sein.");
        assertTrue(gameModel.getPlayers().contains(p2), "Bob sollte in der Liste sein.");
    }

    @Test
    void testUpdatePlayersRemoveDisconnectedPlayers() {
        Player p1 = new Player("Alice", Color.BLUE);
        Player p2 = new Player("Bob", Color.RED);
        
        // Startzustand: Beide Spieler sind im Spiel
        gameModel.UpdatePlayers(new ArrayList<>(Arrays.asList(p1, p2)));

        // Update-Liste enthält jetzt nur noch Bob (Alice fliegt raus)
        ArrayList<Player> updatedPlayers = new ArrayList<>(Arrays.asList(p2));
        gameModel.UpdatePlayers(updatedPlayers);

        // Überprüfen
        assertEquals(1, gameModel.getPlayers().size(), "Es sollte nur noch 1 Spieler übrig sein.");
        assertFalse(gameModel.getPlayers().contains(p1), "Alice sollte entfernt worden sein.");
        assertTrue(gameModel.getPlayers().contains(p2), "Bob sollte noch da sein.");
    }

    @Test
    void testUpdatePlayersNoDuplicates() {
        Player p1 = new Player("Alice", Color.BLUE);
        
        // Alice das erste Mal hinzufügen
        gameModel.UpdatePlayers(new ArrayList<>(Arrays.asList(p1)));
        
        // Wir senden die Liste mit Alice erneut
        ArrayList<Player> updatedPlayers = new ArrayList<>(Arrays.asList(p1));
        gameModel.UpdatePlayers(updatedPlayers);

        // Überprüfen, dass sie nicht doppelt existiert
        assertEquals(1, gameModel.getPlayers().size(), "Spieler sollten nicht doppelt hinzugefügt werden.");
    }

    @Test
    void testUpdatePlayersWithEmptyList() {
        Player p1 = new Player("Alice", Color.BLUE);
        gameModel.UpdatePlayers(new ArrayList<>(Arrays.asList(p1)));

        // Update mit einer komplett leeren Liste (simuliert: alle Spieler löschen)
        gameModel.UpdatePlayers(new ArrayList<>());

        assertTrue(gameModel.getPlayers().isEmpty(), "Die Spielerliste sollte jetzt komplett leer sein.");
    }
}