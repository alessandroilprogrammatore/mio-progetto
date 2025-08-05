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

/** Test per {@link PostgresProgressDAO}. */
public class PostgresProgressDAOTest {
    private PostgresProgressDAO dao;

    @BeforeEach
    void setup() throws Exception {
        System.setProperty("db.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        System.setProperty("db.user", "sa");
        System.setProperty("db.password", "");
        dao = new PostgresProgressDAO();
        try (Connection c = database.DatabaseConfig.getConnection();
             Statement st = c.createStatement()) {
            st.executeUpdate("CREATE TABLE progressi(id INT PRIMARY KEY, team_id INT, descrizione VARCHAR(255), timestamp TIMESTAMP)");
        }
    }

    @Test
    void saveAndFind() {
        Team team = new Team(1, "T", 3, new Utente(1, "A", "a@x.com", Utente.Ruolo.PARTICIPANT));
        Progress p = new Progress(1, team, "descr", LocalDateTime.now());
        dao.save(p);
        assertEquals("descr", dao.findById(1).orElseThrow().getDescrizione());
    }
}
