package main;

import view.MainWindow;
import model.GameMap;
import model.GameModel;
import controller.GameController;
import javax.swing.SwingUtilities;

/**
 * Einstiegspunkt der Anwendung. Initialisiert Map, Model, View und Controller
 * und startet die Swing-Event-Dispatch-Thread.
 */
public class Main {

    /**
     * Default-Konstruktor (keine spezielle Initialisierung).
     */
    public Main() {}

    /**
     * Startet die Anwendung.
     * @param args Programmargumente (nicht genutzt)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            
            GameMap map = new GameMap("/assets/Map4.png");
            
            GameModel model = new GameModel(map);
            MainWindow view = new MainWindow();
            GameController controller = new GameController(view, model, map);
            
            view.setVisible(true);
        });

    }

}
