package model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Rappresenta un hackathon con regole di registrazione.
 */
public class Hackathon {
    private final int id;
    private String titolo;
    private final LocalDate inizio;
    private final LocalDate fine;
    private final LocalDate chiusuraIscrizioni;
    private final int maxPartecipanti;
    private final int maxTeam;
    private final Utente organizzatore;
    private final Set<Utente> iscritti = new HashSet<>();

    public Hackathon(int id, String titolo, LocalDate inizio, LocalDate fine, LocalDate chiusuraIscrizioni,
                     int maxPartecipanti, int maxTeam, Utente organizzatore) {
        this.id = id;
        this.titolo = titolo;
        this.inizio = inizio;
        this.fine = fine;
        this.chiusuraIscrizioni = chiusuraIscrizioni;
        this.maxPartecipanti = maxPartecipanti;
        this.maxTeam = maxTeam;
        this.organizzatore = organizzatore;
    }

    /**
     * Registra un partecipante se c'\u00e8 posto e la scadenza non \u00e8 passata.
     *
     * @param partecipante utente da registrare
     * @return true se registrato
     */
    public boolean registraPartecipante(Utente partecipante) {
        if (LocalDate.now().isAfter(chiusuraIscrizioni)) {
            return false;
        }
        if (iscritti.size() >= maxPartecipanti) {
            return false;
        }
        return iscritti.add(partecipante);
    }

    public int getId() { return id; }
    public String getTitolo() { return titolo; }
    public LocalDate getInizio() { return inizio; }
    public LocalDate getFine() { return fine; }
    public LocalDate getChiusuraIscrizioni() { return chiusuraIscrizioni; }
    public int getMaxPartecipanti() { return maxPartecipanti; }
    public int getMaxTeam() { return maxTeam; }
    public Utente getOrganizzatore() { return organizzatore; }
    public Set<Utente> getIscritti() { return Set.copyOf(iscritti); }
}
