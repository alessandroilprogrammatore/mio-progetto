package dao;

import implementazionePostgresDAO.PostgresUtenteDAO;
import model.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/** Test di integrazione per {@link PostgresUtenteDAO}. */
public class PostgresUtenteDAOTest {
    private PostgresUtenteDAO dao;

    @BeforeEach
    void setup() throws Exception {
        System.setProperty("db.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        System.setProperty("db.user", "sa");
        System.setProperty("db.password", "");
        dao = new PostgresUtenteDAO();
        try (Connection c = database.DatabaseConfig.getConnection();
             Statement st = c.createStatement()) {
            st.executeUpdate("CREATE TABLE utenti(id INT PRIMARY KEY, nome VARCHAR(100), email VARCHAR(100), ruolo VARCHAR(20))");
        }
    }

    @Test
    void saveAndFind() {
        Utente u = new Utente(1, "Mario", "mario@example.com", Utente.Ruolo.PARTICIPANT);
        dao.save(u);
        assertTrue(dao.findById(1).isPresent());
    }
}
