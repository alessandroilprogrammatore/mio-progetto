package model;

/**
 * Voto assegnato da un giudice a un team.
 */
public class Vote {
    private final int id;
    private final Team team;
    private final Utente giudice;
    private final int valore; // 0-10

    public Vote(int id, Team team, Utente giudice, int valore) {
        if (valore < 0 || valore > 10) {
            throw new IllegalArgumentException("Valore non valido");
        }
        this.id = id;
        this.team = team;
        this.giudice = giudice;
        this.valore = valore;
    }

    public int getId() { return id; }
    public Team getTeam() { return team; }
    public Utente getGiudice() { return giudice; }
    public int getValore() { return valore; }
}
