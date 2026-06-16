package DOM;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import model.Player;

public class Manipulator {
	
	public void saveGame(SaveGame game) {
		
		try {
			File xmlFile = new File("saves/savegame.xml");
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document tree = builder.newDocument();
			Element rootElement = tree.createElement("game");
			tree.appendChild(rootElement);
			
			//neue Spieldaten schreiben
			Element map = tree.createElement("map");
			map.setTextContent(game.map);
			rootElement.appendChild(map);
			
			Element curPlayer = tree.createElement("curPlayer");
			curPlayer.setTextContent(String.valueOf(game.curPlayerID));
			rootElement.appendChild(curPlayer);
			
			for(Player p : game.players) {
				
				Element player = tree.createElement("player");
				player.setAttribute("id", String.valueOf(p.getID()));
				rootElement.appendChild(player);
				
				Element name = tree.createElement("name");
				name.setTextContent(p.getName());
				player.appendChild(name);
				
				Element color = tree.createElement("color");
				color.setTextContent(String.valueOf(p.getColour().getRGB()));
				player.appendChild(color);
				
				Element harbourX = tree.createElement("harbourX");
				harbourX.setTextContent(String.valueOf(p.posHabour.getX()));
				player.appendChild(harbourX);
				
				Element harbourY = tree.createElement("harbourY");
				harbourY.setTextContent(String.valueOf(p.posHabour.getY()));
				player.appendChild(harbourY);
				
				for (int i = 0; i<5; i++) {
					
					Element ship = tree.createElement("ship");
					ship.setAttribute("num", String.valueOf(i+1));
					player.appendChild(ship);
					
					Element posX = tree.createElement("posX");
					posX.setTextContent(String.valueOf(p.ships[i].pos.getX()));
					ship.appendChild(posX);
					
					Element posY = tree.createElement("posY");
					posY.setTextContent(String.valueOf(p.ships[i].pos.getY()));
					ship.appendChild(posY);
					
					Element secPosX = tree.createElement("secPosX");
					secPosX.setTextContent(String.valueOf(p.ships[i].secPos.getX()));
					ship.appendChild(secPosX);
					
					Element secPosY = tree.createElement("secPosY");
					secPosY.setTextContent(String.valueOf(p.ships[i].secPos.getY()));
					ship.appendChild(secPosY);
					
					Element isSunken = tree.createElement("isSunken");
					isSunken.setTextContent(String.valueOf(p.ships[i].isSunken));
					ship.appendChild(isSunken);
				}
				
			}

            //In Datei speichern
            DOMSource source = new DOMSource(tree);
            StreamResult res = new StreamResult(xmlFile);
            
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // Nutzt Tabs für den Einzug wie dein Original
            
            transformer.transform(source, res);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clearSaveGame() {
		
	    File xmlFile = new File("saves/savegame.xml");
	    
	    //prüfen, ob die Datei existiert
	    if (xmlFile.exists()) {
	        
	        try {
	        	//FileWriter ohne 'true' überschreibt Datei mit "Nichts"
	        	FileWriter writer = new FileWriter(xmlFile);
	            writer.close();
	        	
	        } catch (IOException e) {
	            System.out.println("Fehler beim Leeren der Datei: " + e.getMessage());
	            e.printStackTrace();
	        }
	    } else {
	        System.out.println("Datei existiert nicht, Leeren nicht notwendig.");
	    }
	}
}

