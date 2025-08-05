package implementazionePostgresDAO;

import dao.ProgressDAO;
import database.DatabaseConfig;
import model.Progress;
import model.Team;
import model.Utente;

import java.sql.*;
import java.util.Optional;

/** Implementazione PostgreSQL di {@link ProgressDAO}. */
public class PostgresProgressDAO implements ProgressDAO {
    @Override
    public void save(Progress progress) {
        String sql = "INSERT INTO progressi(id, team_id, descrizione, timestamp) VALUES(?,?,?,?)";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, progress.getId());
            ps.setInt(2, progress.getTeam().getId());
            ps.setString(3, progress.getDescrizione());
            ps.setTimestamp(4, Timestamp.valueOf(progress.getTimestamp()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Progress> findById(int id) {
        String sql = "SELECT p.id, p.descrizione, p.timestamp, t.id AS tid, t.nome AS tnome, t.max_size, t.creatore_id, " +
                "u.id AS uid, u.nome AS unome, u.email AS uemail, u.ruolo AS uruolo " +
                "FROM progressi p JOIN teams t ON p.team_id = t.id JOIN utenti u ON t.creatore_id = u.id WHERE p.id = ?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Utente creatore = new Utente(
                            rs.getInt("uid"),
                            rs.getString("unome"),
                            rs.getString("uemail"),
                            Utente.Ruolo.valueOf(rs.getString("uruolo"))
                    );
                    Team team = new Team(
                            rs.getInt("tid"),
                            rs.getString("tnome"),
                            rs.getInt("max_size"),
                            creatore
                    );
                    Progress progress = new Progress(
                            rs.getInt("id"),
                            team,
                            rs.getString("descrizione"),
                            rs.getTimestamp("timestamp").toLocalDateTime()
                    );
                    return Optional.of(progress);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void update(Progress progress) {
        String sql = "UPDATE progressi SET team_id=?, descrizione=?, timestamp=? WHERE id=?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, progress.getTeam().getId());
            ps.setString(2, progress.getDescrizione());
            ps.setTimestamp(3, Timestamp.valueOf(progress.getTimestamp()));
            ps.setInt(4, progress.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM progressi WHERE id = ?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
