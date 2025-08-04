package model;

import javax.swing.SwingUtilities;
import controller.Controller;
import gui.MainMenuGUI;

/**
 * Punto d'ingresso dell'applicazione.
 */
public class Main {
    public static void main(String[] args) {
        // Istanzia direttamente il controller dell'applicazione
        Controller ctrl = new Controller();

        // Avvia la GUI principale sul thread Swing
        SwingUtilities.invokeLater(() -> {
            MainMenuGUI menu = new MainMenuGUI(ctrl);
            menu.setVisible(true);
        });
    }
}
