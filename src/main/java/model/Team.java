package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Rappresenta un team di partecipanti.
 */
public class Team {
    private final int id;
    private final String nome;
    private final int maxSize;
    private final List<Utente> membri = new ArrayList<>();

    public Team(int id, String nome, int maxSize, Utente creatore) {
        this.id = id;
        this.nome = nome;
        this.maxSize = maxSize;
        this.membri.add(creatore);
    }

    /**
     * Aggiunge un membro se non si supera la dimensione massima.
     *
     * @param utente utente da aggiungere
     * @return true se aggiunto
     */
    public boolean aggiungiMembro(Utente utente) {
        if (membri.size() >= maxSize) {
            return false;
        }
        return membri.add(utente);
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public int getMaxSize() { return maxSize; }
    public List<Utente> getMembri() { return Collections.unmodifiableList(membri); }
}
