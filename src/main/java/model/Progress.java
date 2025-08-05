package model;

import java.time.LocalDateTime;

/**
 * Documento di avanzamento caricato da un team.
 */
public class Progress {
    private final int id;
    private final Team team;
    private final String descrizione;
    private final LocalDateTime timestamp;

    public Progress(int id, Team team, String descrizione, LocalDateTime timestamp) {
        this.id = id;
        this.team = team;
        this.descrizione = descrizione;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public Team getTeam() { return team; }
    public String getDescrizione() { return descrizione; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
