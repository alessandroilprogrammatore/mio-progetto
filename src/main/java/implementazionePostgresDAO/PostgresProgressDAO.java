package implementazionePostgresDAO;

import dao.ProgressDAO;
import database.DatabaseConfig;
import model.Progress;
import model.Team;
import model.Utente;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

/** Implementazione PostgreSQL di {@link ProgressDAO}. */
public class PostgresProgressDAO implements ProgressDAO {
    @Override
    public void save(Progress p) {
        String sql = "INSERT INTO progressi(id,team_id,descrizione,timestamp) VALUES(?,?,?,?)";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, p.getId());
            ps.setInt(2, p.getTeam().getId());
            ps.setString(3, p.getDescrizione());
            ps.setTimestamp(4, Timestamp.valueOf(p.getTimestamp()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Progress> findById(int id) {
        String sql = "SELECT id,team_id,descrizione,timestamp FROM progressi WHERE id=?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Team team = new Team(rs.getInt("team_id"), "", 0,
                            new Utente(0, "", "", Utente.Ruolo.PARTICIPANT));
                    Progress p = new Progress(
                            rs.getInt("id"),
                            team,
                            rs.getString("descrizione"),
                            rs.getTimestamp("timestamp").toLocalDateTime()
                    );
                    return Optional.of(p);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
