package DOM;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import controller.State;
import model.Coordinates;
import model.Player;
import model.Ship;

public class Reader {
	
	private Document tree1;
	private String map;
	private ArrayList<Player> players = new ArrayList<>(); 
	private State curState;
	private int curPlayerID;
	private SaveGame game;
	
	public Reader() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			// Lädt die Datei aus dem "saves"-Ordner
			tree1 = builder.parse(new File("saves/savegame.xml"));
			tree1.getDocumentElement().normalize();
			
			System.out.println("Strukturbaum erfolgreich gebaut\n");
			
		} catch(ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void find(Node node) {
		
		//Filtern nach Tags
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			
			//Inhalt der Tags
			String content = node.getTextContent().trim();
			
			if (node.getNodeName().equals("map")) {
				this.map = content;
			} 
			else if (node.getNodeName().equals("curPlayerId") || node.getNodeName().equals("curPlayer")) {
				this.curPlayerID = Integer.parseInt(content);
			} 
			else if (node.getNodeName().equals("curState")) {
				this.curState = State.valueOf(content.toUpperCase());

			} 
			else if (node.getNodeName().equals("player")) {
				
				//Knoten zu Element machen
				Element playerElement = (Element) node;
				
				//Daten laden
				int id = Integer.parseInt(playerElement.getAttribute("id"));
				String name = playerElement.getElementsByTagName("name").item(0).getTextContent().trim();
				int movedSteps = Integer.parseInt(playerElement.getElementsByTagName("movedSteps").item(0).getTextContent().trim());
				int harbourX = Integer.parseInt(playerElement.getElementsByTagName("harbourX").item(0).getTextContent().trim());
			    int harbourY = Integer.parseInt(playerElement.getElementsByTagName("harbourY").item(0).getTextContent().trim());
			    
			    //Farbe laden
			    String hexColor = playerElement.getElementsByTagName("color").item(0).getTextContent().trim();
			    Color playerColor;
			    
			    try {
			        playerColor = Color.decode(hexColor);
			    } catch (NumberFormatException e) {
			        playerColor = Color.GRAY;
			    }
				
				//Spieler-Objekt erzeugen
				Player player = new Player(id, name, playerColor);
				player.movedSteps = movedSteps;
				player.setPosHabour(new Coordinates(harbourX, harbourY));
				
				//Schiffe dieses Spielers auslesen
				NodeList shipList = playerElement.getElementsByTagName("ship");
				
				for (int j = 0; j < shipList.getLength(); j++) {
				    Element shipElement = (Element) shipList.item(j);
				    
				    int shipNum = Integer.parseInt(shipElement.getAttribute("num"));
				    
				    //Sicherstellen, dass wir NUR die direkten Kinder dieses einen Schiffs ansprechen:
				    String x1Str = ((Element) shipElement.getElementsByTagName("posX").item(0)).getTextContent().trim();
				    String y1Str = ((Element) shipElement.getElementsByTagName("posY").item(0)).getTextContent().trim();
				    String x2Str = ((Element) shipElement.getElementsByTagName("secPosX").item(0)).getTextContent().trim();
				    String y2Str = ((Element) shipElement.getElementsByTagName("secPosY").item(0)).getTextContent().trim();
				    String sunkenStr = ((Element) shipElement.getElementsByTagName("isSunken").item(0)).getTextContent().trim();
				    
				    int x1 = Integer.parseInt(x1Str);
				    int y1 = Integer.parseInt(y1Str);
				    int x2 = Integer.parseInt(x2Str);
				    int y2 = Integer.parseInt(y2Str);
				    boolean isSunken = Boolean.parseBoolean(sunkenStr);
				    
				    //Schiff-Objekt bauen
				    Coordinates pos1 = new Coordinates(x1, y1);
				    Coordinates pos2 = new Coordinates(x2, y2);
				    
				    Ship ship = new Ship(player, pos1, pos2);
				    ship.isSunken = isSunken;
				    
				    //Schiff ins Array des Spielers legen
				    player.ships[shipNum - 1] = ship;
				}
				
				//Spieler der Liste hinzufügen
				players.add(player);
				
			}
		}
		
		//Rekursiver Kinder durachlaufen exkl. player
		if (node.hasChildNodes() && !node.getNodeName().equals("player")) {
			
			NodeList childs = node.getChildNodes();
			for (int i = 0; i < childs.getLength(); i++) {
				if (childs.item(i).getNodeType() == Node.ELEMENT_NODE) {
					this.find(childs.item(i));
				}
			}
		}
	}
	
	public SaveGame getSaveGame() throws SAXException, IOException, ParserConfigurationException {

		//Parsen starten
	    this.find(this.tree1.getDocumentElement());
	        
	    this.game = new SaveGame(this.map, this.players, this.curState, this.curPlayerID);
	    return this.game;

	}

	@Override
	public String toString() {
		return "Reader [\n  map=" + map + ", \n  curState=" + curState + ", \n  curPlayerID=" + curPlayerID 
				+ ", \n  geladene Spieler Anzahl=" + (players != null ? players.size() : 0) + "\n]";
	}
}