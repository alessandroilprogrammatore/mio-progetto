// File: DocumentoGUI.java
package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import gui.util.StyleUtil;
import java.io.File;

public class DocumentoGUI extends JFrame {
    private final Controller controller;
    private final String hackathonTitle;

    public DocumentoGUI(Controller controller, String hTitle) {
        super("Carica Documento");
        this.controller = controller;
        this.hackathonTitle = hTitle;
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);

        JButton chooseFile = StyleUtil.createButton("Seleziona File", null);
        JTextField filePath = new JTextField(20);
        filePath.setEditable(false);

        chooseFile.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                filePath.setText(f.getAbsolutePath());
            }
        });

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(chooseFile, gbc);
        gbc.gridx = 1;
        panel.add(filePath, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JButton upload = StyleUtil.createButton("Carica", null);
        upload.addActionListener(e -> onUpload(filePath.getText()));
        panel.add(upload, gbc);

        add(panel);
        pack();
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    private void onUpload(String path) {
        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleziona un file prima.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        controller.caricaDocumento(path, hackathonTitle);
        JOptionPane.showMessageDialog(this, "Documento caricato.", "Successo", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
