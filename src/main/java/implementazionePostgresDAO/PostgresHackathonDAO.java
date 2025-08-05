package implementazionePostgresDAO;

import dao.HackathonDAO;
import database.DatabaseConfig;
import model.Hackathon;
import model.Utente;

import java.sql.*;
import java.util.Optional;

/** Implementazione PostgreSQL di {@link HackathonDAO}. */
public class PostgresHackathonDAO implements HackathonDAO {
    @Override
    public void save(Hackathon hackathon) {
        String sql = "INSERT INTO hackathon(id, titolo, inizio, fine, chiusura_iscrizioni, max_partecipanti, max_team, organizzatore_id) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, hackathon.getId());
            ps.setString(2, hackathon.getTitolo());
            ps.setDate(3, Date.valueOf(hackathon.getInizio()));
            ps.setDate(4, Date.valueOf(hackathon.getFine()));
            ps.setDate(5, Date.valueOf(hackathon.getChiusuraIscrizioni()));
            ps.setInt(6, hackathon.getMaxPartecipanti());
            ps.setInt(7, hackathon.getMaxTeam());
            ps.setInt(8, hackathon.getOrganizzatore().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Hackathon> findById(int id) {
        String sql = "SELECT h.id, h.titolo, h.inizio, h.fine, h.chiusura_iscrizioni, h.max_partecipanti, h.max_team, " +
                "u.id AS oid, u.nome AS onome, u.email AS oemail, u.ruolo AS oruolo " +
                "FROM hackathon h JOIN utenti u ON h.organizzatore_id = u.id WHERE h.id = ?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Utente org = new Utente(
                            rs.getInt("oid"),
                            rs.getString("onome"),
                            rs.getString("oemail"),
                            Utente.Ruolo.valueOf(rs.getString("oruolo"))
                    );
                    Hackathon h = new Hackathon(
                            rs.getInt("id"),
                            rs.getString("titolo"),
                            rs.getDate("inizio").toLocalDate(),
                            rs.getDate("fine").toLocalDate(),
                            rs.getDate("chiusura_iscrizioni").toLocalDate(),
                            rs.getInt("max_partecipanti"),
                            rs.getInt("max_team"),
                            org
                    );
                    return Optional.of(h);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void update(Hackathon hackathon) {
        String sql = "UPDATE hackathon SET titolo=?, inizio=?, fine=?, chiusura_iscrizioni=?, max_partecipanti=?, max_team=?, organizzatore_id=? WHERE id=?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, hackathon.getTitolo());
            ps.setDate(2, Date.valueOf(hackathon.getInizio()));
            ps.setDate(3, Date.valueOf(hackathon.getFine()));
            ps.setDate(4, Date.valueOf(hackathon.getChiusuraIscrizioni()));
            ps.setInt(5, hackathon.getMaxPartecipanti());
            ps.setInt(6, hackathon.getMaxTeam());
            ps.setInt(7, hackathon.getOrganizzatore().getId());
            ps.setInt(8, hackathon.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM hackathon WHERE id = ?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
