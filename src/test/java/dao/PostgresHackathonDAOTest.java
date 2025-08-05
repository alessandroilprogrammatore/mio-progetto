package dao;

import implementazionePostgresDAO.PostgresHackathonDAO;
import implementazionePostgresDAO.PostgresUtenteDAO;
import model.Hackathon;
import model.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/** Test per {@link PostgresHackathonDAO}. */
public class PostgresHackathonDAOTest {
    private PostgresHackathonDAO dao;

    @BeforeEach
    void setup() throws Exception {
        System.setProperty("db.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        System.setProperty("db.user", "sa");
        System.setProperty("db.password", "");
        dao = new PostgresHackathonDAO();
        try (Connection c = database.DatabaseConfig.getConnection();
             Statement st = c.createStatement()) {
            st.executeUpdate("CREATE TABLE utenti(id INT PRIMARY KEY, nome VARCHAR(100), email VARCHAR(100), ruolo VARCHAR(20))");
            st.executeUpdate("CREATE TABLE hackathons(id INT PRIMARY KEY, titolo VARCHAR(100), inizio DATE, fine DATE, chiusura_iscrizioni DATE, max_partecipanti INT, max_team INT, organizzatore_id INT)");
        }
    }

    @Test
    void saveAndFind() {
        Utente org = new Utente(1, "Org", "o@x.com", Utente.Ruolo.ORGANIZER);
        new PostgresUtenteDAO().save(org);
        Hackathon h = new Hackathon(1, "Hack", LocalDate.now(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(1), 10, 2, org);
        dao.save(h);
        assertEquals("Hack", dao.findById(1).orElseThrow().getTitolo());
    }
}
