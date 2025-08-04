package dao;

import model.Team;

import java.util.Optional;

/** DAO per Team. */
public interface TeamDAO {
    void save(Team team);
    Optional<Team> findById(int id);
}
