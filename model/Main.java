// File: src/main/java/model/Main.java
package model;

import javax.swing.SwingUtilities;
import controller.Controller;
import controller.FileStateRepository;
import controller.StateRepository;
import gui.MainMenuGUI;

/**
 * Punto d'ingresso dell'applicazione.
 */
public class Main {
    public static void main(String[] args) {
        StateRepository repo = new FileStateRepository();
        Controller ctrl = new Controller(repo);
        Runtime.getRuntime().addShutdownHook(new Thread(ctrl::save));

        // Avvia la GUI principale sul thread Swing
        SwingUtilities.invokeLater(() -> {
            MainMenuGUI menu = new MainMenuGUI(ctrl);
            menu.setVisible(true);
        });
    }
}
