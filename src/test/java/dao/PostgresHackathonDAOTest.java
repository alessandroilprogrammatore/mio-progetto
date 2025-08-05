package dao;

import implementazionePostgresDAO.PostgresHackathonDAO;
import model.Hackathon;
import model.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/** Test di integrazione per {@link PostgresHackathonDAO}. */
public class PostgresHackathonDAOTest {
    private PostgresHackathonDAO dao;
    private Utente organizzatore;

    @BeforeEach
    void setup() throws Exception {
        System.setProperty("db.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        System.setProperty("db.user", "sa");
        System.setProperty("db.password", "");
        dao = new PostgresHackathonDAO();
        organizzatore = new Utente(1, "Org", "org@example.com", Utente.Ruolo.ORGANIZER);
        try (Connection c = database.DatabaseConfig.getConnection();
             Statement st = c.createStatement()) {
            st.executeUpdate("CREATE TABLE utenti(id INT PRIMARY KEY, nome VARCHAR(100), email VARCHAR(100), ruolo VARCHAR(20))");
            st.executeUpdate("CREATE TABLE hackathon(id INT PRIMARY KEY, titolo VARCHAR(100), inizio DATE, fine DATE, chiusura_iscrizioni DATE, max_partecipanti INT, max_team INT, organizzatore_id INT)");
            st.executeUpdate("INSERT INTO utenti(id, nome, email, ruolo) VALUES(1, 'Org', 'org@example.com', 'ORGANIZER')");
        }
    }

    @Test
    void crud() {
        Hackathon h = new Hackathon(1, "Hack1", LocalDate.now(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 100, 10, organizzatore);
        dao.save(h);
        Hackathon found = dao.findById(1).orElseThrow();
        assertEquals("Hack1", found.getTitolo());
        Hackathon h2 = new Hackathon(1, "Hack2", h.getInizio(), h.getFine(), h.getChiusuraIscrizioni(), h.getMaxPartecipanti(), h.getMaxTeam(), organizzatore);
        dao.update(h2);
        assertEquals("Hack2", dao.findById(1).orElseThrow().getTitolo());
        dao.delete(1);
        assertTrue(dao.findById(1).isEmpty());
    }
}
