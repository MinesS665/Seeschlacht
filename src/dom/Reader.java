package dom;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import model.Coordinates;
import model.Player;
import model.Ship;

public class Reader {
	
	private Document tree1;
	private String map;
	private ArrayList<Player> players = new ArrayList<>(); 
	private int curPlayerId;
	private SaveGame game;
	private Exception parseException;
	
	public Reader() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		
		try {
			File xmlFile = new File("saves/savegame.xml");
			
			//Prüft, ob die Datei existiert und nicht leer ist
			if (xmlFile.exists() && xmlFile.length() > 0) {
				DocumentBuilder builder = factory.newDocumentBuilder();
				tree1 = builder.parse(xmlFile);
				tree1.getDocumentElement().normalize();
			} else {
				tree1 = null; //Verhindert spätere Abstürze beim Auslesen
			}
			
		} catch(ParserConfigurationException | SAXException | IOException e) {
			parseException = e;
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
				this.curPlayerId = Integer.parseInt(content);
			} 

			else if (node.getNodeName().equals("player")) {
				
				//Knoten zu Element machen
				Element playerElement = (Element) node;
				
				//Daten laden
				int id = Integer.parseInt(playerElement.getAttribute("id"));
				String name = playerElement.getElementsByTagName("name").item(0).getTextContent().trim();
				int harbourX = Integer.parseInt(playerElement.getElementsByTagName("harbourX").item(0).getTextContent().trim());
			    int harbourY = Integer.parseInt(playerElement.getElementsByTagName("harbourY").item(0).getTextContent().trim());
			    
			    //Farbe laden
			    String strColor = playerElement.getElementsByTagName("color").item(0).getTextContent().trim();
			    Color pColor = new Color(Integer.parseInt(strColor));
				
				//Spieler-Objekt erzeugen
				Player player = new Player(id, name, pColor);
				player.posHarbour = new Coordinates(harbourX, harbourY);
				
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
				    
				    Ship ship = new Ship(pos1, pos2);
				    ship.isSunken = isSunken;
				    
				    //Schiff ins Array des Spielers legen
				    player.ships[shipNum - 1] = ship;
				}
				
				//Spieler der Liste hinzufügen
				players.add(player);
				
			}
		}
		
		//Rekursiver Kinder durchlaufen exkl. player
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

		if (parseException instanceof SAXException) throw (SAXException) parseException;
		if (parseException instanceof IOException) throw (IOException) parseException;
		if (parseException instanceof ParserConfigurationException) throw (ParserConfigurationException) parseException;
		if (this.tree1 == null) throw new FileNotFoundException("Kein Spielstand vorhanden");
		
		players.clear();
		
		//Parsen starten
	    this.find(this.tree1.getDocumentElement());
	        
	    this.game = new SaveGame(this.map, this.players, this.curPlayerId);
	    return this.game;

	}

}
