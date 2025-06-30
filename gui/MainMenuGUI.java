// File: gui/MainMenuGUI.java
package gui;

import controller.Controller;
import model.Utente;
import model.Partecipante;
import model.Organizzatore;
import model.Giudice;

import javax.swing.*;
import java.awt.*;
import gui.util.StyleUtil;

/**
 * Finestra principale dopo il login: mostra azioni disponibili in base al ruolo
 */
public class MainMenuGUI extends JFrame {
    public MainMenuGUI(Controller controller) {
        super("Hackathon Manager");
        initUI(controller);
    }

    private void initUI(Controller controller) {
        // Look & Feel di sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Pannello principale
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(45, 62, 80));

        // Titolo
        JLabel title = new JLabel("Hackathon Manager", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        // Pannello dei bottoni
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        Utente u = controller.getCurrentUser();
        if (u instanceof Partecipante) {
            JButton creaTeam = StyleUtil.createButton("Crea Team", null);
            creaTeam.setAlignmentX(Component.CENTER_ALIGNMENT);
            creaTeam.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            creaTeam.addActionListener(e -> new CreaTeamGUI(controller));
            if (controller.hasTeam((Partecipante) u)) {
                creaTeam.setEnabled(false);
            }
            btnPanel.add(creaTeam);
            btnPanel.add(Box.createVerticalStrut(10));

            JButton inviti = StyleUtil.createButton("Inviti", null);
            inviti.setAlignmentX(Component.CENTER_ALIGNMENT);
            inviti.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            inviti.addActionListener(e -> new InvitiPartecipanteGUI((Partecipante) u, controller));
            btnPanel.add(inviti);

        } else if (u instanceof Organizzatore) {
            JButton creaHack = StyleUtil.createButton("Crea Hackathon", null);
            creaHack.setAlignmentX(Component.CENTER_ALIGNMENT);
            creaHack.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            creaHack.addActionListener(e -> new CreaHackathonGUI(controller));
            btnPanel.add(creaHack);

        } else if (u instanceof Giudice) {
            JButton valuta = StyleUtil.createButton("Valuta Team", null);
            valuta.setAlignmentX(Component.CENTER_ALIGNMENT);
            valuta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            valuta.addActionListener(e -> new ValutaTeamGUI(controller));
            btnPanel.add(valuta);
        }

        btnPanel.add(Box.createVerticalStrut(20));
        JButton logout = StyleUtil.createButton("Logout", null);
        logout.setAlignmentX(Component.CENTER_ALIGNMENT);
        logout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        logout.addActionListener(e -> {
            new SignIn(controller);
            dispose();
        });
        btnPanel.add(logout);

        mainPanel.add(btnPanel, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel("© 2025 Hackathon Manager", SwingConstants.CENTER);
        footer.setForeground(Color.LIGHT_GRAY);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(footer, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setVisible(true);
    }

}
