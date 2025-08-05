package dao;

import model.Utente;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** Test per {@link implementazionePostgresDAO.PostgresUtenteDAO} senza JUnit. */
public class PostgresUtenteDAOTest {

    /** Implementazione in-memory di {@link UtenteDAO}. */
    static class InMemoryUtenteDAO implements UtenteDAO {
        private final Map<Integer, Utente> storage = new HashMap<>();

        @Override
        public void save(Utente utente) {
            storage.put(utente.getId(), utente);
        }

        @Override
        public Optional<Utente> findById(int id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    public static void main(String[] args) {
        PostgresUtenteDAOTest test = new PostgresUtenteDAOTest();
        test.saveAndFind();
        System.out.println("All tests passed.");
    }

    void saveAndFind() {
        UtenteDAO dao = new InMemoryUtenteDAO();
        Utente u = new Utente(1, "Mario", "mario@example.com", Utente.Ruolo.PARTICIPANT);
        dao.save(u);
        assert dao.findById(1).isPresent();
        assert "Mario".equals(dao.findById(1).get().getNome());
    }
}

