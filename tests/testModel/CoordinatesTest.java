package testModel;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;

public class CoordinatesTest {
	
	Coordinates testCoords;
	
	@BeforeEach
	public void setUp() {
		testCoords = new Coordinates(30,50);
	}
	

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void equalsTest() {
		
		Coordinates testCoords2 = testCoords;
		Coordinates testCoords3 = new Coordinates (30, 50);
		Coordinates testCoords4 = null;
		
		assertTrue(testCoords.equals(testCoords2));
		assertTrue(testCoords.equals(testCoords3));
		assertFalse(testCoords.equals(testCoords4));
		assertFalse(testCoords.equals("Test"));
	}
	
	@Test
	public void isClose() {
		
		Coordinates testCoords2 = new Coordinates(30, 51);
		Coordinates testCoords3 = new Coordinates(29, 51);
		Coordinates testCoords4 = new Coordinates(28, 50);
		
		assertTrue(testCoords.isClose(testCoords));
		assertTrue(testCoords.isClose(testCoords2));
		assertTrue(testCoords.isClose(testCoords3));
		assertFalse(testCoords.isClose(testCoords4));
	}
}
