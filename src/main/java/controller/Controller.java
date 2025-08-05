package controller;

import dao.HackathonDAO;
import dao.TeamDAO;
import dao.UtenteDAO;
import dao.VoteDAO;
import model.Hackathon;
import model.Team;
import model.Utente;
import model.Invitation;
import model.Vote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Coordina i casi d'uso principali dell'applicazione.
 */
public class Controller {
    private final UtenteDAO utenteDAO;
    private final HackathonDAO hackathonDAO;
    private final TeamDAO teamDAO;
    private final VoteDAO voteDAO;
    private final List<Invitation> invitations = new ArrayList<>();
    private final List<Vote> votes = new ArrayList<>();

    public Controller(UtenteDAO utenteDAO, HackathonDAO hackathonDAO, TeamDAO teamDAO, VoteDAO voteDAO) {
        this.utenteDAO = utenteDAO;
        this.hackathonDAO = hackathonDAO;
        this.teamDAO = teamDAO;
        this.voteDAO = voteDAO;
    }

    /** Crea un nuovo hackathon. */
    public void createHackathon(Hackathon h) {
        hackathonDAO.save(h);
    }

    /** Registra un utente a un hackathon rispettando le regole. */
    public boolean registerUser(Hackathon h, Utente u) {
        boolean ok = h.registraPartecipante(u);
        if (ok) {
            utenteDAO.save(u);
            hackathonDAO.save(h);
        }
        return ok;
    }

    /** Forma un team con un creatore. */
    public Team formTeam(Hackathon h, String nome, Utente creatore) {
        Team team = new Team((int) (Math.random() * 100000), nome, h.getMaxTeam(), creatore);
        teamDAO.save(team);
        return team;
    }

    /** Aggiunge un membro a un team. */
    public boolean joinTeam(Team team, Utente utente) {
        boolean ok = team.aggiungiMembro(utente);
        if (ok) {
            teamDAO.save(team);
        }
        return ok;
    }

    /** Crea un invito per un partecipante. */
    public Invitation invite(Hackathon h, Utente u) {
        Invitation inv = new Invitation(h, u);
        invitations.add(inv);
        return inv;
    }

    /** Risponde a un invito. */
    public void respondInvitation(Invitation inv, boolean accept) {
        if (accept) inv.accept(); else inv.reject();
    }

    /** Salva un voto e aggiorna le statistiche. */
    public void addVote(Vote v) {
        votes.add(v);
        voteDAO.save(v);
    }

    /** Restituisce la classifica dei team in base alla media dei voti. */
    public List<Team> getRanking() {
        Map<Team, List<Vote>> map = new HashMap<>();
        for (Vote v : votes) {
            map.computeIfAbsent(v.getTeam(), k -> new ArrayList<>()).add(v);
        }
        return map.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(avg(e2.getValue()), avg(e1.getValue())))
                .map(Map.Entry::getKey)
                .toList();
    }

    private double avg(List<Vote> vs) {
        return vs.stream().mapToInt(Vote::getValore).average().orElse(0);
    }
}
