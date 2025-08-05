package dao;

import implementazionePostgresDAO.PostgresTeamDAO;
import model.Team;
import model.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/** Test di integrazione per {@link PostgresTeamDAO}. */
public class PostgresTeamDAOTest {
    private PostgresTeamDAO dao;
    private Utente creatore;

    @BeforeEach
    void setup() throws Exception {
        System.setProperty("db.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        System.setProperty("db.user", "sa");
        System.setProperty("db.password", "");
        dao = new PostgresTeamDAO();
        creatore = new Utente(1, "Mario", "mario@example.com", Utente.Ruolo.PARTICIPANT);
        try (Connection c = database.DatabaseConfig.getConnection();
             Statement st = c.createStatement()) {
            st.executeUpdate("CREATE TABLE utenti(id INT PRIMARY KEY, nome VARCHAR(100), email VARCHAR(100), ruolo VARCHAR(20))");
            st.executeUpdate("CREATE TABLE teams(id INT PRIMARY KEY, nome VARCHAR(100), max_size INT, creatore_id INT)");
            st.executeUpdate("INSERT INTO utenti(id, nome, email, ruolo) VALUES(1, 'Mario', 'mario@example.com', 'PARTICIPANT')");
        }
    }

    @Test
    void crud() {
        Team team = new Team(1, "Team1", 5, creatore);
        dao.save(team);
        Team found = dao.findById(1).orElseThrow();
        assertEquals("Team1", found.getNome());
        Team updated = new Team(1, "Team1", 6, creatore);
        dao.update(updated);
        assertEquals(6, dao.findById(1).orElseThrow().getMaxSize());
        dao.delete(1);
        assertTrue(dao.findById(1).isEmpty());
    }
}
