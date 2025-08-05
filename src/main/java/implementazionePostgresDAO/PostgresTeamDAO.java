package implementazionePostgresDAO;

import dao.TeamDAO;
import database.DatabaseConfig;
import model.Team;
import model.Utente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Implementazione PostgreSQL di {@link TeamDAO}. */
public class PostgresTeamDAO implements TeamDAO {
    @Override
    public void save(Team team) {
        try (Connection c = DatabaseConfig.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO teams(id,nome,max_size) VALUES(?,?,?)")) {
                ps.setInt(1, team.getId());
                ps.setString(2, team.getNome());
                ps.setInt(3, team.getMaxSize());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO team_members(team_id,utente_id) VALUES(?,?)")) {
                for (Utente u : team.getMembri()) {
                    ps.setInt(1, team.getId());
                    ps.setInt(2, u.getId());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Team> findById(int id) {
        String teamSql = "SELECT id,nome,max_size FROM teams WHERE id=?";
        String memSql = "SELECT utente_id FROM team_members WHERE team_id=?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement tps = c.prepareStatement(teamSql);
             PreparedStatement mps = c.prepareStatement(memSql)) {
            tps.setInt(1, id);
            try (ResultSet trs = tps.executeQuery()) {
                if (!trs.next()) return Optional.empty();
                String nome = trs.getString("nome");
                int max = trs.getInt("max_size");
                mps.setInt(1, id);
                try (ResultSet mrs = mps.executeQuery()) {
                    List<Integer> ids = new ArrayList<>();
                    while (mrs.next()) ids.add(mrs.getInt("utente_id"));
                    Utente creator;
                    if (ids.isEmpty()) {
                        creator = new Utente(0, "", "", Utente.Ruolo.PARTICIPANT);
                    } else {
                        creator = new Utente(ids.get(0), "", "", Utente.Ruolo.PARTICIPANT);
                    }
                    Team team = new Team(id, nome, max, creator);
                    for (int i = 1; i < ids.size(); i++) {
                        team.aggiungiMembro(new Utente(ids.get(i), "", "", Utente.Ruolo.PARTICIPANT));
                    }
                    return Optional.of(team);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
