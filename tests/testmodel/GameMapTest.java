package testmodel;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import model.*;

class GameMapTest {

    private static final String TEST_MAP_PATH = "/test_map.png";
    
    // Testbild erstellen
    @BeforeAll
    static void setUpTestData() throws IOException {
        // Erzeuge 2x2 Testbild
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        
        // Pixel (0,0): Exakt das Ziel-Blau -> Sollte WATER werden
        img.setRGB(0, 0, new Color(127, 214, 209).getRGB());
        
        // Pixel (1,0): Leicht abweichendes Blau (innerhalb Toleranz 25) -> Sollte WATER werden
        img.setRGB(1, 0, new Color(135, 220, 200).getRGB());
        
        // Pixel (0,1): Klares Rot -> Sollte LAND werden
        img.setRGB(0, 1, new Color(255, 0, 0).getRGB());
        
        // Pixel (1,1): Weit außerhalb der Toleranz -> Sollte LAND werden
        img.setRGB(1, 1, new Color(100, 150, 250).getRGB());

        // Speichert das Bild im Resource-Ordner der Test-Klasse
        URL assets = GameMapTest.class.getResource("/");
        if (assets != null) {
            File resFolder = new File(assets.getFile());
            File testImage = new File(resFolder, "test_map.png");
            ImageIO.write(img, "png", testImage);
        }
    }

    @Test
    void testMapDimensions() {
        GameMap map = new GameMap(TEST_MAP_PATH);
        
        // Überprüft, ob die Dimensionen korrekt aus dem Bild gelesen wurden
        assertEquals(2, map.getWidth(), "Die Breite der Map sollte 2 sein.");
        assertEquals(2, map.getHeight(), "Die Höhe der Map sollte 2 sein.");
    }

    @Test
    void testGetTileByCoordinatesXY() {
        GameMap map = new GameMap(TEST_MAP_PATH);

        // Teste exaktes Blau (0,0)
        assertEquals(TileTyp.WATER, map.getTile(0, 0), "(0,0) sollte WATER sein (exaktes Blau)");

        // Teste toleriertes Blau (1,0)
        assertEquals(TileTyp.WATER, map.getTile(1, 0), "(1,0) sollte WATER sein (Blau innerhalb Toleranz)");

        // Teste Rot (0,1)
        assertEquals(TileTyp.LAND, map.getTile(0, 1), "(0,1) sollte LAND sein");
    }

    @Test
    void testGetTileByCoordinatesObject() {
        GameMap map = new GameMap(TEST_MAP_PATH);
        
        Coordinates waterCoord = new Coordinates(0, 0);
        Coordinates landCoord = new Coordinates(0, 1);

        assertEquals(TileTyp.WATER, map.getTile(waterCoord), "Sollte WATER für Coordinates(0,0) zurückgeben");
        assertEquals(TileTyp.LAND, map.getTile(landCoord), "Sollte LAND für Coordinates(0,1) zurückgeben");
    }

    @Test
    void testOutOfBoundsReturnsLand() {
        GameMap map = new GameMap(TEST_MAP_PATH);

        // Teste Koordinaten außerhalb der Map-Grenzen (Sollte laut Code LAND liefern)
        assertEquals(TileTyp.LAND, map.getTile(-1, 0), "Negatives X sollte LAND liefern");
        assertEquals(TileTyp.LAND, map.getTile(0, -1), "Negatives Y sollte LAND liefern");
        assertEquals(TileTyp.LAND, map.getTile(2, 1), "X außerhalb der Breite sollte LAND liefern");
        assertEquals(TileTyp.LAND, map.getTile(1, 2), "Y außerhalb der Höhe sollte LAND liefern");
        
        // Gleiche mit Coordinates-Objekt
        assertEquals(TileTyp.LAND, map.getTile(new Coordinates(5, 5)), "Weit außerhalb sollte LAND liefern");
    }

    @Test
    void testInvalidImagePathDoesNotCrash() {
        // Wir erwarten jetzt, dass KEINE Exception fliegt, da der GameMap-Konstruktor 
        // den null-Pfad sauber abfängt (sofern du das Refactoring in GameMap eingebaut hast!)
        assertDoesNotThrow(() -> {
            GameMap errorMap = new GameMap("/gibts_nicht.png");
            
            // Da die Datei nicht existiert, wurden width und height nie gesetzt und sind standardmäßig 0
            assertEquals(0, errorMap.getWidth(), "Breite sollte 0 sein bei Fehlern");
            assertEquals(0, errorMap.getHeight(), "Höhe sollte 0 sein bei Fehlern");
        });
    }
}