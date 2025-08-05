package dao;

import implementazionePostgresDAO.PostgresProgressDAO;
import model.Progress;
import model.Team;
import model.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/** Test di integrazione per {@link PostgresProgressDAO}. */
public class PostgresProgressDAOTest {
    private PostgresProgressDAO dao;
    private Team team;

    @BeforeEach
    void setup() throws Exception {
        System.setProperty("db.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        System.setProperty("db.user", "sa");
        System.setProperty("db.password", "");
        dao = new PostgresProgressDAO();
        Utente creatore = new Utente(1, "Mario", "mario@example.com", Utente.Ruolo.PARTICIPANT);
        team = new Team(1, "Team1", 5, creatore);
        try (Connection c = database.DatabaseConfig.getConnection();
             Statement st = c.createStatement()) {
            st.executeUpdate("CREATE TABLE utenti(id INT PRIMARY KEY, nome VARCHAR(100), email VARCHAR(100), ruolo VARCHAR(20))");
            st.executeUpdate("CREATE TABLE teams(id INT PRIMARY KEY, nome VARCHAR(100), max_size INT, creatore_id INT)");
            st.executeUpdate("CREATE TABLE progressi(id INT PRIMARY KEY, team_id INT, descrizione VARCHAR(255), timestamp TIMESTAMP)");
            st.executeUpdate("INSERT INTO utenti(id, nome, email, ruolo) VALUES(1, 'Mario', 'mario@example.com', 'PARTICIPANT')");
            st.executeUpdate("INSERT INTO teams(id, nome, max_size, creatore_id) VALUES(1, 'Team1', 5, 1)");
        }
    }

    @Test
    void crud() {
        Progress p = new Progress(1, team, "desc", LocalDateTime.now());
        dao.save(p);
        Progress found = dao.findById(1).orElseThrow();
        assertEquals("desc", found.getDescrizione());
        Progress upd = new Progress(1, team, "nuova", LocalDateTime.now().plusHours(1));
        dao.update(upd);
        assertEquals("nuova", dao.findById(1).orElseThrow().getDescrizione());
        dao.delete(1);
        assertTrue(dao.findById(1).isEmpty());
    }
}
