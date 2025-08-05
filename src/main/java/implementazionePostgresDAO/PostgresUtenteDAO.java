package implementazionePostgresDAO;

import dao.UtenteDAO;
import database.DatabaseConfig;
import model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/** Implementazione PostgreSQL di {@link UtenteDAO}. */
public class PostgresUtenteDAO implements UtenteDAO {
    @Override
    public void save(Utente utente) {
        String sql = "INSERT INTO utenti(id, nome, email, ruolo) VALUES(?,?,?,?)";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, utente.getId());
            ps.setString(2, utente.getNome());
            ps.setString(3, utente.getEmail());
            ps.setString(4, utente.getRuolo().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Utente> findById(int id) {
        String sql = "SELECT id, nome, email, ruolo FROM utenti WHERE id = ?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Utente utente = new Utente(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            Utente.Ruolo.valueOf(rs.getString("ruolo")));
                    return Optional.of(utente);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
