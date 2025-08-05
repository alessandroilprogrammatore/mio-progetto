package controller;

import dao.HackathonDAO;
import dao.TeamDAO;
import dao.UtenteDAO;
import dao.VoteDAO;
import model.Hackathon;
import model.Invitation;
import model.Team;
import model.Utente;
import model.Vote;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Additional tests for {@link Controller}. */
public class ControllerFeaturesTest {
    private Controller createController() {
        UtenteDAO udao = Mockito.mock(UtenteDAO.class);
        HackathonDAO hdao = Mockito.mock(HackathonDAO.class);
        TeamDAO tdao = Mockito.mock(TeamDAO.class);
        VoteDAO vdao = Mockito.mock(VoteDAO.class);
        return new Controller(udao, hdao, tdao, vdao);
    }

    @Test
    void registrationDeadlineEnforced() {
        UtenteDAO udao = Mockito.mock(UtenteDAO.class);
        Controller c = new Controller(udao, Mockito.mock(HackathonDAO.class), Mockito.mock(TeamDAO.class), Mockito.mock(VoteDAO.class));
        Utente org = new Utente(1, "Org", "o@x.com", Utente.Ruolo.ORGANIZER);
        Hackathon h = new Hackathon(1, "Hack", LocalDate.now(), LocalDate.now().plusDays(1),
                LocalDate.now().minusDays(1), 10, 2, org);
        boolean ok = c.registerUser(h, new Utente(2, "Bob", "b@x.com", Utente.Ruolo.PARTICIPANT));
        assertFalse(ok);
        Mockito.verify(udao, Mockito.never()).save(Mockito.any());
    }

    @Test
    void maxParticipantsPerHackathon() {
        Utente org = new Utente(1, "Org", "o@x.com", Utente.Ruolo.ORGANIZER);
        Hackathon h = new Hackathon(1, "Hack", LocalDate.now(), LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1), 1, 2, org);
        UtenteDAO udao = Mockito.mock(UtenteDAO.class);
        Controller c = new Controller(udao, Mockito.mock(HackathonDAO.class), Mockito.mock(TeamDAO.class), Mockito.mock(VoteDAO.class));
        assertTrue(c.registerUser(h, new Utente(2, "Bob", "b@x.com", Utente.Ruolo.PARTICIPANT)));
        assertFalse(c.registerUser(h, new Utente(3, "Carl", "c@x.com", Utente.Ruolo.PARTICIPANT)));
        Mockito.verify(udao, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void invitationAcceptanceRejection() {
        Controller c = createController();
        Utente org = new Utente(1, "Org", "o@x.com", Utente.Ruolo.ORGANIZER);
        Hackathon h = new Hackathon(1, "Hack", LocalDate.now(), LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1), 10, 2, org);
        Utente invitee = new Utente(2, "Bob", "b@x.com", Utente.Ruolo.PARTICIPANT);
        Invitation inv = Mockito.spy(c.invite(h, invitee));
        c.respondInvitation(inv, true);
        Mockito.verify(inv).accept();
        Invitation inv2 = Mockito.spy(c.invite(h, invitee));
        c.respondInvitation(inv2, false);
        Mockito.verify(inv2).reject();
    }

    @Test
    void voteAggregationAndRanking() {
        UtenteDAO udao = Mockito.mock(UtenteDAO.class);
        HackathonDAO hdao = Mockito.mock(HackathonDAO.class);
        TeamDAO tdao = Mockito.mock(TeamDAO.class);
        VoteDAO vdao = Mockito.mock(VoteDAO.class);
        Controller c = new Controller(udao, hdao, tdao, vdao);
        Utente judge = new Utente(10, "J", "j@x.com", Utente.Ruolo.JUDGE);
        Utente creator = new Utente(1, "C", "c@x.com", Utente.Ruolo.PARTICIPANT);
        Team t1 = new Team(1, "T1", 3, creator);
        Team t2 = new Team(2, "T2", 3, creator);
        c.addVote(new Vote(1, t1, judge, 5));
        c.addVote(new Vote(2, t1, judge, 7));
        c.addVote(new Vote(3, t2, judge, 8));
        Mockito.verify(vdao, Mockito.times(3)).save(Mockito.any());
        List<Team> ranking = c.getRanking();
        assertEquals(t2, ranking.get(0));
    }
}
