package dao;

import model.Progress;

import java.util.Optional;

/** DAO per Progress. */
public interface ProgressDAO {
    void save(Progress progress);
    Optional<Progress> findById(int id);
    void update(Progress progress);
    void delete(int id);
}
