package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Panel dealing with team creation and invitations.
 */
public class TeamPanel extends JPanel {
    private final JTextField teamNameField = new JTextField(20);
    private final JTextField inviteEmailField = new JTextField(20);
    private final JButton createTeamButton = new JButton("Create Team");
    private final JButton inviteButton = new JButton("Invite");

    public TeamPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Team name:"), gbc);
        gbc.gridx = 1; add(teamNameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Invite email:"), gbc);
        gbc.gridx = 1; add(inviteEmailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel buttons = new JPanel();
        buttons.add(createTeamButton);
        buttons.add(inviteButton);
        add(buttons, gbc);
    }

    public void addCreateTeamListener(ActionListener listener) {
        createTeamButton.addActionListener(listener);
    }

    public void addInviteListener(ActionListener listener) {
        inviteButton.addActionListener(listener);
    }

    public JTextField getTeamNameField() { return teamNameField; }
    public JTextField getInviteEmailField() { return inviteEmailField; }
}
