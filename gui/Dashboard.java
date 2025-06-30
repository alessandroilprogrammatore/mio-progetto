// File: src/main/java/gui/Dashboard.java
package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import gui.util.StyleUtil;
import java.util.List;

/**
 * Dashboard principale: mostra riepilogo e statistiche.
 */
public class Dashboard extends JFrame {
    private final Controller controller;

    public Dashboard(Controller controller) {
        super("Dashboard - Benvenuto");
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Overview", createOverviewPanel());
        tabs.addTab("Statistiche", createStatsPanel());
        if ("Partecipante".equals(controller.getCurrentUserRole()) && controller.currentUserHasTeam()) {
            tabs.addTab("Team", createTeamPanel());
        }
        tabs.addTab("Link", createLinksPanel());

        getContentPane().add(tabs);
        setVisible(true);
    }

    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        String role = controller.getCurrentUserRole();
        if ("Partecipante".equals(role)) {
            controller.getMyTeams().forEach(t -> listModel.addElement("Team: " + t));
        } else if ("Organizzatore".equals(role)) {
            controller.getMyHackathons().forEach(h -> listModel.addElement("Hackathon: " + h));
        } else {
            listModel.addElement("Nessun elemento disponibile.");
        }

        JList<String> list = new JList<>(listModel);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String role = controller.getCurrentUserRole();
        if ("Partecipante".equals(role)) {
            int cntTeams = controller.getMyTeams().size();
            int cntInv = controller.getMyInviti().size();
            panel.add(new JLabel("Numero team: " + cntTeams));
            panel.add(new JLabel("Inviti pendenti: " + cntInv));
            panel.add(new JLabel("Partecipazioni totali: " + cntTeams));
        } else if ("Organizzatore".equals(role)) {
            int cntHacks = controller.getMyHackathons().size();
            int cntTeamsEval = controller.getTeamsToEvaluate().size();
            panel.add(new JLabel("Hackathon creati: " + cntHacks));
            panel.add(new JLabel("Team da valutare: " + cntTeamsEval));
            panel.add(new JLabel("Organizzatore attivo"));
        } else if ("Giudice".equals(role)) {
            int cntVotes = controller.getVoti().size();
            int cntTeamsEval = controller.getTeamsToEvaluate().size();
            panel.add(new JLabel("Valutazioni effettuate: " + cntVotes));
            panel.add(new JLabel("Team da valutare: " + cntTeamsEval));
            panel.add(new JLabel("Giudice attivo"));
        }
        return panel;
    }

    private JPanel createTeamPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String teamName = controller.getMyTeams().get(0);
        JLabel info = new JLabel("Team: " + teamName + " (" + controller.getTeamMembersCount(teamName) + " membri)");
        panel.add(info, BorderLayout.NORTH);

        JPanel invitePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField emailField = new JTextField(15);
        JButton inviteBtn = StyleUtil.createButton("Invita", null);
        inviteBtn.setEnabled(controller.getTeamMembersCount(teamName) < controller.getMaxTeamSize());
        inviteBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inserisci email", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (controller.aggiungiMembroTeam(teamName, email)) {
                JOptionPane.showMessageDialog(this, "Partecipante aggiunto", "Successo", JOptionPane.INFORMATION_MESSAGE);
                emailField.setText("");
                info.setText("Team: " + teamName + " (" + controller.getTeamMembersCount(teamName) + " membri)");
                inviteBtn.setEnabled(controller.getTeamMembersCount(teamName) < controller.getMaxTeamSize());
            } else {
                JOptionPane.showMessageDialog(this, "Impossibile aggiungere partecipante", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
        invitePanel.add(new JLabel("Email:"));
        invitePanel.add(emailField);
        invitePanel.add(inviteBtn);
        panel.add(invitePanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createLinksPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton profBtn = StyleUtil.createButton("Profilo", null);
        profBtn.addActionListener(e -> new ProfiloUtenteGUI(controller).setVisible(true));
        panel.add(profBtn);

        JButton logoutB = StyleUtil.createButton("Logout", null);
        logoutB.addActionListener(e -> {
            dispose();
            new SignIn(controller).setVisible(true);
        });
        panel.add(logoutB);

        return panel;
    }
}
