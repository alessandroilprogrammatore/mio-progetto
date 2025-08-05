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

/** Test di integrazione per {@link PostgresVoteDAO}. */
public class PostgresVoteDAOTest {
    private PostgresVoteDAO dao;
    private Team team;
    private Utente giudice;

    @BeforeEach
    void setup() throws Exception {
        System.setProperty("db.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        System.setProperty("db.user", "sa");
        System.setProperty("db.password", "");
        dao = new PostgresVoteDAO();
        Utente creatore = new Utente(1, "Mario", "mario@example.com", Utente.Ruolo.PARTICIPANT);
        giudice = new Utente(2, "Giulia", "giulia@example.com", Utente.Ruolo.JUDGE);
        team = new Team(1, "Team1", 5, creatore);
        try (Connection c = database.DatabaseConfig.getConnection();
             Statement st = c.createStatement()) {
            st.executeUpdate("CREATE TABLE utenti(id INT PRIMARY KEY, nome VARCHAR(100), email VARCHAR(100), ruolo VARCHAR(20))");
            st.executeUpdate("CREATE TABLE teams(id INT PRIMARY KEY, nome VARCHAR(100), max_size INT, creatore_id INT)");
            st.executeUpdate("CREATE TABLE voti(id INT PRIMARY KEY, team_id INT, giudice_id INT, valore INT)");
            st.executeUpdate("INSERT INTO utenti(id, nome, email, ruolo) VALUES(1, 'Mario', 'mario@example.com', 'PARTICIPANT')");
            st.executeUpdate("INSERT INTO utenti(id, nome, email, ruolo) VALUES(2, 'Giulia', 'giulia@example.com', 'JUDGE')");
            st.executeUpdate("INSERT INTO teams(id, nome, max_size, creatore_id) VALUES(1, 'Team1', 5, 1)");
        }
    }

    @Test
    void crud() {
        Vote v = new Vote(1, team, giudice, 7);
        dao.save(v);
        Vote found = dao.findById(1).orElseThrow();
        assertEquals(7, found.getValore());
        Vote upd = new Vote(1, team, giudice, 8);
        dao.update(upd);
        assertEquals(8, dao.findById(1).orElseThrow().getValore());
        dao.delete(1);
        assertTrue(dao.findById(1).isEmpty());
    }
}
