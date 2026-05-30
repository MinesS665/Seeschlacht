package main;

import view.MainWindow;
import model.GameMap;
import model.GameModel;
import controller.GameController;
import javax.swing.SwingUtilities;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			
			GameMap map = new GameMap("/assets/Map.png");
            
            GameModel model = new GameModel(map);
            MainWindow view = new MainWindow();
            GameController controller = new GameController(view, model, map);
            
            view.setVisible(true);
        });

	}

}
