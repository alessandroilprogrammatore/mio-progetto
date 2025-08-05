package dao;

import implementazionePostgresDAO.PostgresTeamDAO;
import implementazionePostgresDAO.PostgresUtenteDAO;
import model.Team;
import model.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/** Test per {@link PostgresTeamDAO}. */
public class PostgresTeamDAOTest {
    private PostgresTeamDAO dao;

    @BeforeEach
    void setup() throws Exception {
        System.setProperty("db.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        System.setProperty("db.user", "sa");
        System.setProperty("db.password", "");
        dao = new PostgresTeamDAO();
        try (Connection c = database.DatabaseConfig.getConnection();
             Statement st = c.createStatement()) {
            st.executeUpdate("CREATE TABLE utenti(id INT PRIMARY KEY, nome VARCHAR(100), email VARCHAR(100), ruolo VARCHAR(20))");
            st.executeUpdate("CREATE TABLE teams(id INT PRIMARY KEY, nome VARCHAR(100), max_size INT)");
            st.executeUpdate("CREATE TABLE team_members(team_id INT, utente_id INT)");
        }
    }

    @Test
    void saveAndFind() {
        Utente u1 = new Utente(1, "A", "a@x.com", Utente.Ruolo.PARTICIPANT);
        Utente u2 = new Utente(2, "B", "b@x.com", Utente.Ruolo.PARTICIPANT);
        PostgresUtenteDAO udao = new PostgresUtenteDAO();
        udao.save(u1);
        udao.save(u2);
        Team t = new Team(1, "Team", 3, u1);
        t.aggiungiMembro(u2);
        dao.save(t);
        assertEquals(2, dao.findById(1).orElseThrow().getMembri().size());
    }
}
