package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import gui.util.StyleUtil;
import java.util.List;

/**
 * Visualizza la classifica dei team in base ai voti ricevuti.
 */
public class ClassificaGUI extends JFrame {
    public ClassificaGUI(Controller controller) {
        super("Classifica Team");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        DefaultListModel<String> model = new DefaultListModel<>();
        List<String> ranking = controller.getClassifica();
        if (ranking.isEmpty()) {
            model.addElement("Nessuna classifica disponibile");
        } else {
            ranking.forEach(model::addElement);
        }
        JList<String> list = new JList<>(model);
        JScrollPane scroll = new JScrollPane(list);

        JButton close = StyleUtil.createButton("Chiudi", new Dimension(120, 40));
        close.addActionListener(e -> dispose());
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(close, BorderLayout.SOUTH);

        add(panel);
        pack();
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}
