package model;

/** Simple invitation for a user to join a hackathon. */
public class Invitation {
    private final Hackathon hackathon;
    private final Utente invitee;
    private Boolean accepted; // null=pending

    public Invitation(Hackathon hackathon, Utente invitee) {
        this.hackathon = hackathon;
        this.invitee = invitee;
    }

    public Hackathon getHackathon() { return hackathon; }
    public Utente getInvitee() { return invitee; }
    public Boolean getAccepted() { return accepted; }

    public void accept() { this.accepted = true; }
    public void reject() { this.accepted = false; }
}
