package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Panel allowing judges to vote teams and display the ranking.
 */
public class VotePanel extends JPanel {
    private final JTextField teamIdField = new JTextField(5);
    private final JTextField valueField = new JTextField(3);
    private final JTextArea rankingArea = new JTextArea(10, 30);
    private final JButton voteButton = new JButton("Vote");
    private final JButton rankingButton = new JButton("Ranking");

    public VotePanel() {
        setLayout(new BorderLayout(5,5));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Team ID:"), gbc);
        gbc.gridx = 1; form.add(teamIdField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Vote (0-10):"), gbc);
        gbc.gridx = 1; form.add(valueField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel buttons = new JPanel();
        buttons.add(voteButton);
        buttons.add(rankingButton);
        form.add(buttons, gbc);

        add(form, BorderLayout.NORTH);

        rankingArea.setEditable(false);
        add(new JScrollPane(rankingArea), BorderLayout.CENTER);
    }

    public void addVoteListener(ActionListener listener) {
        voteButton.addActionListener(listener);
    }

    public void addShowRankingListener(ActionListener listener) {
        rankingButton.addActionListener(listener);
    }

    public JTextField getTeamIdField() { return teamIdField; }
    public JTextField getValueField() { return valueField; }
    public JTextArea getRankingArea() { return rankingArea; }
}
