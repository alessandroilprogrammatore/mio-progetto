package dao;

import model.Vote;

import java.util.Optional;

/** DAO per Vote. */
public interface VoteDAO {
    void save(Vote vote);
    Optional<Vote> findById(int id);
}
