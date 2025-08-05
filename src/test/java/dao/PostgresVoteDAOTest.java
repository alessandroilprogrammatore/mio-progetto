package dao;

import implementazionePostgresDAO.PostgresVoteDAO;
import model.Team;
import model.Utente;
import model.Vote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/** Test per {@link PostgresVoteDAO}. */
public class PostgresVoteDAOTest {
    private PostgresVoteDAO dao;

    @BeforeEach
    void setup() throws Exception {
        System.setProperty("db.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        System.setProperty("db.user", "sa");
        System.setProperty("db.password", "");
        dao = new PostgresVoteDAO();
        try (Connection c = database.DatabaseConfig.getConnection();
             Statement st = c.createStatement()) {
            st.executeUpdate("CREATE TABLE voti(id INT PRIMARY KEY, team_id INT, giudice_id INT, valore INT)");
        }
    }

    @Test
    void saveAndFind() {
        Team team = new Team(1, "T", 3, new Utente(1, "A", "a@x.com", Utente.Ruolo.PARTICIPANT));
        Utente judge = new Utente(2, "J", "j@x.com", Utente.Ruolo.JUDGE);
        Vote v = new Vote(1, team, judge, 8);
        dao.save(v);
        assertEquals(8, dao.findById(1).orElseThrow().getValore());
    }
}
