
        package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import gui.util.StyleUtil;

/**
 * Finestra per la valutazione dei team: interfaccia più moderna ed elegante
 */
public class ValutaTeamGUI extends JFrame {
    private final Controller controller;
    private JTable teamTable;
    private DefaultTableModel tableModel;

    public ValutaTeamGUI(Controller controller) {
        super("Valuta Team - Hackathon Manager");
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        // Look & Feel di sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(45, 62, 80));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titolo
        JLabel title = new JLabel("Valuta i Team Partecipanti", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        mainPanel.add(title, BorderLayout.NORTH);

        // Tabella dei team
        String[] columns = {"Nome Team", "Partecipanti"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        teamTable = new JTable(tableModel);
        teamTable.setRowHeight(30);
        teamTable.setFont(new Font("SansSerif", Font.PLAIN, 16));
        teamTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        teamTable.getTableHeader().setBackground(new Color(243, 156, 18));
        teamTable.getTableHeader().setForeground(Color.WHITE);
        teamTable.setSelectionBackground(new Color(243, 156, 18));
        JScrollPane scrollPane = new JScrollPane(teamTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Barra dei bottoni
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setOpaque(false);

        JButton evaluateBtn = StyleUtil.createButton("Valuta", new Dimension(120, 40));
        evaluateBtn.addActionListener(e -> evaluateSelectedTeam());
        buttonPanel.add(evaluateBtn);

        JButton closeBtn = StyleUtil.createButton("Chiudi", new Dimension(120, 40));
        closeBtn.addActionListener(e -> dispose());
        buttonPanel.add(closeBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        populateTable();
        setVisible(true);
    }

    private void populateTable() {
        tableModel.setRowCount(0);
        var teams = controller.getTeamsToEvaluate();
        if (teams.isEmpty()) {
            tableModel.addRow(new Object[]{"Nessun team disponibile", ""});
        } else {
            teams.forEach(name -> {
                String partecipanti = String.join(", ", controller.getTeamMembers(name));
                tableModel.addRow(new Object[]{name, partecipanti});
            });
        }
    }

    private void evaluateSelectedTeam() {
        int selectedRow = teamTable.getSelectedRow();
        if (selectedRow < 0 || teamTable.getValueAt(selectedRow, 0).equals("Nessun team disponibile")) {
            JOptionPane.showMessageDialog(this, "Seleziona un team da valutare.",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String teamName = (String) tableModel.getValueAt(selectedRow, 0);

        String input = JOptionPane.showInputDialog(this,
                "Inserisci il voto per \"" + teamName + "\" (0-10):", "0");
        try {
            if (input == null) return; // utente ha annullato
            int score = Integer.parseInt(input);
            if (score < 0 || score > 10) throw new NumberFormatException();
            controller.inviaVotazione(teamName, score);
            JOptionPane.showMessageDialog(this, "Voto salvato con successo!",
                    "Fatto", JOptionPane.INFORMATION_MESSAGE);
            populateTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Valore non valido. Inserisci un numero tra 0 e 10.",
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }


}
