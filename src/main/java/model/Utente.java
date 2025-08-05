package model;

/**
 * Rappresenta un utente della piattaforma.
 */
public class Utente {
    /** Ruolo dell'utente. */
    public enum Ruolo { ORGANIZER, JUDGE, PARTICIPANT }

    private final int id;
    private final String nome;
    private final String email;
    private final Ruolo ruolo;

    public Utente(int id, String nome, String email, Ruolo ruolo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.ruolo = ruolo;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }
}
