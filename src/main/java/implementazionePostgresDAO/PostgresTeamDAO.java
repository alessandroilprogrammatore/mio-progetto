package implementazionePostgresDAO;

import dao.TeamDAO;
import database.DatabaseConfig;
import model.Team;
import model.Utente;

import java.sql.*;
import java.util.Optional;

/** Implementazione PostgreSQL di {@link TeamDAO}. */
public class PostgresTeamDAO implements TeamDAO {
    @Override
    public void save(Team team) {
        String sql = "INSERT INTO teams(id, nome, max_size, creatore_id) VALUES(?,?,?,?)";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, team.getId());
            ps.setString(2, team.getNome());
            ps.setInt(3, team.getMaxSize());
            ps.setInt(4, team.getMembri().get(0).getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Team> findById(int id) {
        String sql = "SELECT t.id, t.nome, t.max_size, u.id AS uid, u.nome AS unome, u.email AS uemail, u.ruolo AS uruolo " +
                "FROM teams t JOIN utenti u ON t.creatore_id = u.id WHERE t.id = ?";
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
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getInt("max_size"),
                            creatore
                    );
                    return Optional.of(team);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void update(Team team) {
        String sql = "UPDATE teams SET nome=?, max_size=?, creatore_id=? WHERE id=?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, team.getNome());
            ps.setInt(2, team.getMaxSize());
            ps.setInt(3, team.getMembri().get(0).getId());
            ps.setInt(4, team.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM teams WHERE id = ?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
