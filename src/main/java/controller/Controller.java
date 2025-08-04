package controller;

import dao.HackathonDAO;
import dao.TeamDAO;
import dao.UtenteDAO;
import model.Hackathon;
import model.Team;
import model.Utente;

/**
 * Coordina i casi d'uso principali dell'applicazione.
 */
public class Controller {
    private final UtenteDAO utenteDAO;
    private final HackathonDAO hackathonDAO;
    private final TeamDAO teamDAO;

    public Controller(UtenteDAO utenteDAO, HackathonDAO hackathonDAO, TeamDAO teamDAO) {
        this.utenteDAO = utenteDAO;
        this.hackathonDAO = hackathonDAO;
        this.teamDAO = teamDAO;
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
}
