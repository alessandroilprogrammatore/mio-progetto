package implementazionePostgresDAO;

import dao.HackathonDAO;
import database.DatabaseConfig;
import model.Hackathon;
import model.Utente;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

/** Implementazione PostgreSQL di {@link HackathonDAO}. */
public class PostgresHackathonDAO implements HackathonDAO {
    @Override
    public void save(Hackathon h) {
        String sql = "INSERT INTO hackathons(id,titolo,inizio,fine,chiusura_iscrizioni,max_partecipanti,max_team,organizzatore_id) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, h.getId());
            ps.setString(2, h.getTitolo());
            ps.setDate(3, Date.valueOf(h.getInizio()));
            ps.setDate(4, Date.valueOf(h.getFine()));
            ps.setDate(5, Date.valueOf(h.getChiusuraIscrizioni()));
            ps.setInt(6, h.getMaxPartecipanti());
            ps.setInt(7, h.getMaxTeam());
            ps.setInt(8, h.getOrganizzatore().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Hackathon> findById(int id) {
        String sql = "SELECT id,titolo,inizio,fine,chiusura_iscrizioni,max_partecipanti,max_team,organizzatore_id FROM hackathons WHERE id=?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Utente org = new Utente(rs.getInt("organizzatore_id"), "", "", Utente.Ruolo.ORGANIZER);
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
}
