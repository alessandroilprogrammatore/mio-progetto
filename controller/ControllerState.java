package controller;

import model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ControllerState implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Utente> utenti;
    private List<Documento> docs;
    private List<Voto> voti;
    private List<Invito> inviti;
    private List<Team> teams;
    private List<Hackathon> hacks;

    public ControllerState() {
        this.utenti = new ArrayList<>();
        this.docs = new ArrayList<>();
        this.voti = new ArrayList<>();
        this.inviti = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.hacks = new ArrayList<>();
    }

    public List<Utente> getUtenti() { return utenti; }
    public void setUtenti(List<Utente> utenti) { this.utenti = utenti; }
    public List<Documento> getDocs() { return docs; }
    public void setDocs(List<Documento> docs) { this.docs = docs; }
    public List<Voto> getVoti() { return voti; }
    public void setVoti(List<Voto> voti) { this.voti = voti; }
    public List<Invito> getInviti() { return inviti; }
    public void setInviti(List<Invito> inviti) { this.inviti = inviti; }
    public List<Team> getTeams() { return teams; }
    public void setTeams(List<Team> teams) { this.teams = teams; }
    public List<Hackathon> getHacks() { return hacks; }
    public void setHacks(List<Hackathon> hacks) { this.hacks = hacks; }
}
