package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Main application frame hosting all panels using a {@link CardLayout}.
 */
public class MainFrame extends JFrame {
    private final CardLayout layout = new CardLayout();
    private final JPanel container = new JPanel(layout);

    private final LoginPanel loginPanel = new LoginPanel();
    private final HackathonPanel hackathonPanel = new HackathonPanel();
    private final TeamPanel teamPanel = new TeamPanel();
    private final ProgressPanel progressPanel = new ProgressPanel();
    private final VotePanel votePanel = new VotePanel();

    public MainFrame() {
        super("Hackathon Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(container);

        container.add(loginPanel, "login");
        container.add(hackathonPanel, "hackathon");
        container.add(teamPanel, "team");
        container.add(progressPanel, "progress");
        container.add(votePanel, "vote");

        setSize(900, 600);
        setLocationRelativeTo(null);
    }

    public void showLogin() { layout.show(container, "login"); }
    public void showHackathon() { layout.show(container, "hackathon"); }
    public void showTeam() { layout.show(container, "team"); }
    public void showProgress() { layout.show(container, "progress"); }
    public void showVote() { layout.show(container, "vote"); }

    public LoginPanel getLoginPanel() { return loginPanel; }
    public HackathonPanel getHackathonPanel() { return hackathonPanel; }
    public TeamPanel getTeamPanel() { return teamPanel; }
    public ProgressPanel getProgressPanel() { return progressPanel; }
    public VotePanel getVotePanel() { return votePanel; }
}
