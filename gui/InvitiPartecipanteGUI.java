// File: InvitiPartecipanteGUI.java
package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import gui.util.StyleUtil;
import java.awt.event.ActionEvent;
import java.util.List;

public class InvitiPartecipanteGUI extends JFrame {
    private final Controller controller;

    public InvitiPartecipanteGUI(Controller controller) {
        super("I miei Inviti");
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(400, 300);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("I miei inviti", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        mainPanel.add(title, BorderLayout.NORTH);

        java.util.List<String> inviti = controller.getMyInviti();
        if (inviti.isEmpty()) {
            JLabel empty = new JLabel("Non ci sono inviti al momento.", SwingConstants.CENTER);
            mainPanel.add(empty, BorderLayout.CENTER);
        } else {
            JPanel listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
            int idx = 0;
            for (String invito : inviti) {
                JPanel row = new JPanel(new BorderLayout(5, 5));
                row.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                JLabel lbl = new JLabel(invito);
                row.add(lbl, BorderLayout.CENTER);

                JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
                JButton accetta = StyleUtil.createButton("Accetta", null);
                final int current = idx;
                accetta.addActionListener((ActionEvent e) -> handle(current, true));
                JButton rifiuta = StyleUtil.createButton("Rifiuta", null);
                rifiuta.addActionListener((ActionEvent e) -> handle(current, false));
                btns.add(accetta);
                btns.add(rifiuta);
                row.add(btns, BorderLayout.EAST);

                listPanel.add(row);
                listPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                idx++;
            }
            JScrollPane scroll = new JScrollPane(listPanel);
            mainPanel.add(scroll, BorderLayout.CENTER);
        }

        add(mainPanel);
        setVisible(true);
    }

    private void handle(int index, boolean accept) {
        controller.rispondiInvito(index, accept);
        JOptionPane.showMessageDialog(this,
                accept ? "Invito accettato." : "Invito rifiutato.",
                "Info", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new InvitiPartecipanteGUI(controller);
    }
}