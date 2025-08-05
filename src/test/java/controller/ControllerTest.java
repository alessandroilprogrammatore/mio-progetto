package controller;

import dao.HackathonDAO;
import dao.TeamDAO;
import dao.UtenteDAO;
import model.Hackathon;
import model.Team;
import model.Utente;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Simple manual tests for Controller.
 */
public class ControllerTest {
    public static void main(String[] args) {
        testTeamSizeLimit();
        System.out.println("All tests passed.");
    }

    /**
     * Ensure that teams respect their size limit and DAO saves are triggered.
     */
    static void testTeamSizeLimit() {
        Utente creator = new Utente(1, "Alice", "a@b.com", Utente.Ruolo.PARTICIPANT);
        Hackathon h = new Hackathon(1, "Hack", LocalDate.now(), LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(1), 10, 2, creator);

        FakeUtenteDAO udao = new FakeUtenteDAO();
        FakeHackathonDAO hdao = new FakeHackathonDAO();
        FakeTeamDAO tdao = new FakeTeamDAO();
        Controller c = new Controller(udao, hdao, tdao);

        Team team = c.formTeam(h, "team", creator);
        assert c.joinTeam(team, new Utente(2, "Bob", "b@b.com", Utente.Ruolo.PARTICIPANT));
        assert !c.joinTeam(team, new Utente(3, "Carl", "c@b.com", Utente.Ruolo.PARTICIPANT));
        assert tdao.saveCount == 2;
        System.out.println("testTeamSizeLimit passed");
    }

    // ---- Fake DAO implementations ----
    static class FakeUtenteDAO implements UtenteDAO {
        private final Map<Integer, Utente> store = new HashMap<>();
        @Override
        public void save(Utente utente) { store.put(utente.getId(), utente); }
        @Override
        public Optional<Utente> findById(int id) { return Optional.ofNullable(store.get(id)); }
    }

    static class FakeHackathonDAO implements HackathonDAO {
        private final Map<Integer, Hackathon> store = new HashMap<>();
        @Override
        public void save(Hackathon hackathon) { store.put(hackathon.getId(), hackathon); }
        @Override
        public Optional<Hackathon> findById(int id) { return Optional.ofNullable(store.get(id)); }
    }

    static class FakeTeamDAO implements TeamDAO {
        private final Map<Integer, Team> store = new HashMap<>();
        int saveCount = 0;
        @Override
        public void save(Team team) {
            saveCount++;
            store.put(team.getId(), team);
        }
        @Override
        public Optional<Team> findById(int id) { return Optional.ofNullable(store.get(id)); }
    }
}
