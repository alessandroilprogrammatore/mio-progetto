package dao;

import model.Utente;

import java.util.Optional;

/** DAO per Utente. */
public interface UtenteDAO {
    void save(Utente utente);
    Optional<Utente> findById(int id);
}
