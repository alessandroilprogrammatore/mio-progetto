package controller;

import dao.HackathonDAO;
import dao.TeamDAO;
import dao.UtenteDAO;
import dao.VoteDAO;
import model.Hackathon;
import model.Team;
import model.Utente;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/** Test dei vincoli nel controller. */
public class ControllerTest {
    @Test
    void teamSizeLimit() {
        Utente creator = new Utente(1, "Alice", "a@b.com", Utente.Ruolo.PARTICIPANT);
        Hackathon h = new Hackathon(1, "Hack", LocalDate.now(), LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(1), 10, 2, creator);
        UtenteDAO udao = Mockito.mock(UtenteDAO.class);
        HackathonDAO hdao = Mockito.mock(HackathonDAO.class);
        TeamDAO tdao = Mockito.mock(TeamDAO.class);
        VoteDAO vdao = Mockito.mock(VoteDAO.class);
        Controller c = new Controller(udao, hdao, tdao, vdao);
        Team team = c.formTeam(h, "team", creator);
        assertTrue(c.joinTeam(team, new Utente(2, "Bob", "b@b.com", Utente.Ruolo.PARTICIPANT)));
        assertFalse(c.joinTeam(team, new Utente(3, "Carl", "c@b.com", Utente.Ruolo.PARTICIPANT)));
        Mockito.verify(tdao, Mockito.times(2)).save(Mockito.eq(team));
    }
}
