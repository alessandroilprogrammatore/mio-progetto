// File: ControllerState.java
package controller;

import java.util.List;
import model.*;

/**
 * Rappresenta lo stato completo del Controller da persistere.
 */
public class ControllerState {
    private List<Utente> utenti;
    private List<Documento> docs;
    private List<Voto> voti;
    private List<Invito> inviti;
    private List<Team> teams;
    private List<Hackathon> hacks;

    public ControllerState() {}

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
