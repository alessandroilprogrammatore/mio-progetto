package dao;

import model.Hackathon;

import java.util.Optional;

/** DAO per Hackathon. */
public interface HackathonDAO {
    void save(Hackathon hackathon);
    Optional<Hackathon> findById(int id);
}
