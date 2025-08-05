package implementazionePostgresDAO;

import dao.VoteDAO;
import database.DatabaseConfig;
import model.Team;
import model.Utente;
import model.Vote;

import java.sql.*;
import java.util.Optional;

/** Implementazione PostgreSQL di {@link VoteDAO}. */
public class PostgresVoteDAO implements VoteDAO {
    @Override
    public void save(Vote vote) {
        String sql = "INSERT INTO voti(id, team_id, giudice_id, valore) VALUES(?,?,?,?)";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vote.getId());
            ps.setInt(2, vote.getTeam().getId());
            ps.setInt(3, vote.getGiudice().getId());
            ps.setInt(4, vote.getValore());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Vote> findById(int id) {
        String sql = "SELECT v.id, v.valore, t.id AS tid, t.nome AS tnome, t.max_size, t.creatore_id, " +
                "uc.id AS cuid, uc.nome AS cunome, uc.email AS cuemail, uc.ruolo AS curuolo, " +
                "ug.id AS jid, ug.nome AS jnome, ug.email AS jemail, ug.ruolo AS jruolo " +
                "FROM voti v JOIN teams t ON v.team_id = t.id " +
                "JOIN utenti uc ON t.creatore_id = uc.id " +
                "JOIN utenti ug ON v.giudice_id = ug.id WHERE v.id = ?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Utente creatore = new Utente(
                            rs.getInt("cuid"),
                            rs.getString("cunome"),
                            rs.getString("cuemail"),
                            Utente.Ruolo.valueOf(rs.getString("curuolo"))
                    );
                    Team team = new Team(
                            rs.getInt("tid"),
                            rs.getString("tnome"),
                            rs.getInt("max_size"),
                            creatore
                    );
                    Utente giudice = new Utente(
                            rs.getInt("jid"),
                            rs.getString("jnome"),
                            rs.getString("jemail"),
                            Utente.Ruolo.valueOf(rs.getString("jruolo"))
                    );
                    Vote vote = new Vote(
                            rs.getInt("id"),
                            team,
                            giudice,
                            rs.getInt("valore")
                    );
                    return Optional.of(vote);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void update(Vote vote) {
        String sql = "UPDATE voti SET team_id=?, giudice_id=?, valore=? WHERE id=?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vote.getTeam().getId());
            ps.setInt(2, vote.getGiudice().getId());
            ps.setInt(3, vote.getValore());
            ps.setInt(4, vote.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM voti WHERE id = ?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
