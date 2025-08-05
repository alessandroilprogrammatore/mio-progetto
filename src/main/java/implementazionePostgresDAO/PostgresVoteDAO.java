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
        String sql = "INSERT INTO voti(id,team_id,giudice_id,valore) VALUES(?,?,?,?)";
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
        String sql = "SELECT id,team_id,giudice_id,valore FROM voti WHERE id=?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Team team = new Team(rs.getInt("team_id"), "", 0,
                            new Utente(0, "", "", Utente.Ruolo.PARTICIPANT));
                    Utente giudice = new Utente(rs.getInt("giudice_id"), "", "", Utente.Ruolo.JUDGE);
                    Vote v = new Vote(rs.getInt("id"), team, giudice, rs.getInt("valore"));
                    return Optional.of(v);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
