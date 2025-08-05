package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Panel managing hackathon lifecycle and registrations.
 */
public class HackathonPanel extends JPanel {
    private final JTextField idField = new JTextField(5);
    private final JTextField titleField = new JTextField(20);
    private final JTextField startField = new JTextField(10);
    private final JTextField endField = new JTextField(10);
    private final JTextField deadlineField = new JTextField(10);
    private final JTextField maxParticipantsField = new JTextField(5);
    private final JTextField maxTeamField = new JTextField(5);

    private final JButton createButton = new JButton("Create");
    private final JButton editButton = new JButton("Edit");
    private final JButton deleteButton = new JButton("Delete");
    private final JButton registerButton = new JButton("Register");

    public HackathonPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; add(idField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; add(titleField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Start date:"), gbc);
        gbc.gridx = 1; add(startField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("End date:"), gbc);
        gbc.gridx = 1; add(endField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Reg. deadline:"), gbc);
        gbc.gridx = 1; add(deadlineField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; add(new JLabel("Max participants:"), gbc);
        gbc.gridx = 1; add(maxParticipantsField, gbc);
        gbc.gridx = 0; gbc.gridy = 6; add(new JLabel("Max team size:"), gbc);
        gbc.gridx = 1; add(maxTeamField, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        JPanel buttons = new JPanel();
        buttons.add(createButton);
        buttons.add(editButton);
        buttons.add(deleteButton);
        buttons.add(registerButton);
        add(buttons, gbc);
    }

    public void addCreateListener(ActionListener listener) {
        createButton.addActionListener(listener);
    }

    public void addEditListener(ActionListener listener) {
        editButton.addActionListener(listener);
    }

    public void addDeleteListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void addRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    public JTextField getIdField() { return idField; }
    public JTextField getTitleField() { return titleField; }
    public JTextField getStartField() { return startField; }
    public JTextField getEndField() { return endField; }
    public JTextField getDeadlineField() { return deadlineField; }
    public JTextField getMaxParticipantsField() { return maxParticipantsField; }
    public JTextField getMaxTeamField() { return maxTeamField; }
}
