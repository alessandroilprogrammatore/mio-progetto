package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Panel for uploading and reviewing project progress documents.
 */
public class ProgressPanel extends JPanel {
    private final JTextArea descriptionArea = new JTextArea(5, 30);
    private final JTextArea reviewArea = new JTextArea(10, 30);
    private final JButton uploadButton = new JButton("Upload");
    private final JButton reviewButton = new JButton("Review");

    public ProgressPanel() {
        setLayout(new BorderLayout(5,5));

        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Description:"), BorderLayout.NORTH);
        top.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        JPanel buttons = new JPanel();
        buttons.add(uploadButton);
        buttons.add(reviewButton);
        add(buttons, BorderLayout.CENTER);

        reviewArea.setEditable(false);
        add(new JScrollPane(reviewArea), BorderLayout.SOUTH);
    }

    public void addUploadListener(ActionListener listener) {
        uploadButton.addActionListener(listener);
    }

    public void addReviewListener(ActionListener listener) {
        reviewButton.addActionListener(listener);
    }

    public JTextArea getDescriptionArea() { return descriptionArea; }
    public JTextArea getReviewArea() { return reviewArea; }
}
