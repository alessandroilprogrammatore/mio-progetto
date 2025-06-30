
// File: Controller.java
package controller;

import model.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller centralizza la logica applicativa e persistenza in file.
 */
public class Controller implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int MAX_TEAM_SIZE = 4;

    // Collezioni di dominio
    private List<Utente> utenti;
    private List<Documento> docs;
    private List<Voto> voti;
    private List<Invito> inviti;
    private List<Team> teams;
    private List<Hackathon> hacks;

    private transient Utente currentUser;

    public Controller() {
        this.utenti = new ArrayList<>();
        this.docs    = new ArrayList<>();
        this.voti    = new ArrayList<>();
        this.inviti  = new ArrayList<>();
        this.teams   = new ArrayList<>();
        this.hacks   = new ArrayList<>();
    }

    /**
     * Carica stato da file, o nuovo controller se non esiste.
     */
    public static Controller loadState() {
        File file = new File("data/state.dat");
        if (!file.exists()) return new Controller();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (Controller) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new Controller();
        }
    }

    /**
     * Salva stato su file.
     */
    public void saveState() {
        new File("data").mkdirs();
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("data/state.dat"))) {
            out.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registra un nuovo utente con ruolo: Partecipante, Organizzatore o Giudice.
     */
    public boolean registraUtente(String nome,
                                 String cognome,
                                 String email,
                                 String password,
                                 String ruolo) {
        boolean exists = utenti.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
        if (exists) return false;

        Utente u;
        switch (ruolo) {
            case "Organizzatore": u = new Organizzatore(nome, cognome, null, email, password); break;
            case "Giudice":      u = new Giudice(nome, cognome, null, email, password);      break;
            default:               u = new Partecipante(nome, cognome, null, email, password); break;
        }
        utenti.add(u);
        return true;
    }

    /**
     * Login con email e password.
     */
    public boolean login(String email, String pwd) {
        Optional<Utente> opt = utenti.stream()
                .filter(u -> u.checkCredentials(email, pwd))
                .findFirst();
        if (opt.isPresent()) {
            currentUser = opt.get();
            return true;
        }
        return false;
    }

    public Utente getCurrentUser() {
        return currentUser;
    }

    // Informazioni sull'utente corrente
    public String getCurrentUserRole() {
        if (currentUser instanceof Partecipante) return "Partecipante";
        if (currentUser instanceof Organizzatore) return "Organizzatore";
        if (currentUser instanceof Giudice) return "Giudice";
        return "";
    }

    public String getCurrentUserNome() { return currentUser != null ? currentUser.getNome() : ""; }
    public String getCurrentUserCognome() { return currentUser != null ? currentUser.getCognome() : ""; }
    public LocalDate getCurrentUserDataNascita() { return currentUser != null ? currentUser.getDataNascita() : null; }
    public String getCurrentUserEmail() { return currentUser != null ? currentUser.getEmail() : ""; }
    public String getCurrentUserPassword() { return currentUser != null ? currentUser.getPassword() : ""; }

    public void aggiornaCurrentUser(String nome,
                                    String cognome,
                                    LocalDate dataNascita,
                                    String email,
                                    String password) {
        if (currentUser != null) {
            aggiornaUtente(currentUser, nome, cognome, dataNascita, email, password);
        }
    }

    /**
     * Aggiorna dati profilo utente.
     */
    public void aggiornaUtente(Utente u,
                               String nome,
                               String cognome,
                               LocalDate dataNascita,
                               String email,
                               String password) {
        u.setNome(nome);
        u.setCognome(cognome);
        u.setDataNascita(dataNascita);
        u.setEmail(email);
        u.setPassword(password);
    }

    // DOCUMENTI
    public void caricaDocumento(Documento d) { docs.add(d); }

    /**
     * Carica un nuovo documento dal percorso fornito associandolo
     * all'hackathon indicato.
     */
    private Hackathon findHackathon(String titolo) {
        return hacks.stream()
                .filter(h -> h.getTitolo().equalsIgnoreCase(titolo))
                .findFirst().orElse(null);
    }

    public boolean caricaDocumento(String path, String hackathonTitle) {
        Hackathon h = findHackathon(hackathonTitle);
        if (h == null) return false;
        Documento doc = new Documento(new File(path));
        doc.setHackathon(h);
        docs.add(doc);
        return true;
    }

    public List<Documento> getDocumenti() { return Collections.unmodifiableList(docs); }
    public void modificaDocumento(Documento d, String contenuto) { d.modificaDocumento(contenuto); }
    public void cancellaDocumento(Documento d) { docs.remove(d); }

    // VOTI
    public void valutaTeam(Voto voto) {
        if (currentUser instanceof Giudice) voti.add(voto);
    }

    /**
     * Invia una votazione al team indicato.
     */
    private Team findTeam(String name) {
        return teams.stream()
                .filter(t -> t.getNome().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public boolean inviaVotazione(String teamName, int punteggio) {
        if (!(currentUser instanceof Giudice)) return false;
        Team team = findTeam(teamName);
        if (team == null) return false;
        voti.add(new Voto(team, punteggio));
        return true;
    }

    public List<Voto> getVoti() {
        if (currentUser instanceof Giudice) return Collections.unmodifiableList(voti);
        return Collections.emptyList();
    }

    // INVITI
    public void creaInvito(Invito i) {
        if (currentUser instanceof Organizzatore) inviti.add(i);
        else if (currentUser instanceof Partecipante) {
            ((Partecipante) currentUser).addInvito(i);
            inviti.add(i);
        }
    }

    public List<String> getMyInviti() {
        if (currentUser instanceof Partecipante) {
            Partecipante p = (Partecipante) currentUser;
            return p.getInviti().stream()
                    .map(inv -> inv.getHackathon().getTitolo())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public void rispondiInvito(int index, boolean accept) {
        if (currentUser instanceof Partecipante) {
            Partecipante p = (Partecipante) currentUser;
            if (index >= 0 && index < p.getInviti().size()) {
                Invito invito = p.getInviti().get(index);
                if (accept) invito.accetta(); else invito.rifiuta();
            }
        }
    }

    // TEAM
    public void creaTeam(Team t) {
        if (!(currentUser instanceof Partecipante)) return;
        Partecipante p = (Partecipante) currentUser;
        if (hasTeam(p)) return;
        if (isTeamNameTaken(t.getNome())) return;
        t.addPartecipante(p);
        teams.add(t);
    }

    /**
     * Crea un team con il nome indicato aggiungendo l'utente corrente.
     */
    public boolean creaTeam(String nome) {
        if (!(currentUser instanceof Partecipante)) return false;
        Partecipante p = (Partecipante) currentUser;
        if (hasTeam(p) || isTeamNameTaken(nome)) return false;
        Team t = new Team(nome);
        t.addPartecipante(p);
        teams.add(t);
        return true;
    }

    private boolean aggiungiMembroTeam(Team team, String email) {
        if (!(currentUser instanceof Partecipante)) return false;
        if (team == null || !team.getPartecipanti().contains(currentUser)) return false;
        if (team.getPartecipanti().size() >= MAX_TEAM_SIZE) return false;

        Partecipante target = utenti.stream()
                .filter(u -> u instanceof Partecipante && u.getEmail().equalsIgnoreCase(email))
                .map(u -> (Partecipante) u)
                .findFirst().orElse(null);
        if (target == null) return false;
        if (hasTeam(target)) return false;

        team.addPartecipante(target);
        return true;
    }

    public boolean aggiungiMembroTeam(String teamName, String email) {
        return aggiungiMembroTeam(findTeam(teamName), email);
    }

    public boolean hasTeam(Partecipante p) {
        return teams.stream().anyMatch(t -> t.getPartecipanti().contains(p));
    }

    private boolean isTeamNameTaken(String nome) {
        return teams.stream().anyMatch(t -> t.getNome().equalsIgnoreCase(nome));
    }

    public int getMaxTeamSize() {
        return MAX_TEAM_SIZE;
    }

    public boolean currentUserHasTeam() {
        if (currentUser instanceof Partecipante) {
            return hasTeam((Partecipante) currentUser);
        }
        return false;
    }

    public List<String> getMyTeams() {
        if (currentUser instanceof Partecipante) {
            Partecipante p = (Partecipante) currentUser;
            return teams.stream()
                    .filter(t -> t.getPartecipanti().contains(p))
                    .map(Team::getNome)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public int getTeamMembersCount(String teamName) {
        Team t = findTeam(teamName);
        return t != null ? t.getPartecipanti().size() : 0;
    }

    public List<String> getTeamMembers(String teamName) {
        Team t = findTeam(teamName);
        if (t == null) return Collections.emptyList();
        return t.getPartecipanti().stream()
                .map(p -> p.getNome() + " " + p.getCognome())
                .collect(Collectors.toList());
    }

    public List<String> getTeamsToEvaluate() {
        if (currentUser instanceof Giudice) {
            return teams.stream().map(Team::getNome).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * Restituisce la classifica dei team ordinata per voto decrescente.
     */
    public List<String> getClassifica() {
        if (!(currentUser instanceof Giudice)) return Collections.emptyList();
        return teams.stream()
                .filter(t -> t.getVoto() >= 0)
                .sorted(Comparator.comparingInt(Team::getVoto).reversed())
                .map(t -> t.getNome() + ": " + t.getVoto())
                .collect(Collectors.toList());
    }

    // HACKATHON
    public void creaHackathon(Hackathon h) {
        if (currentUser instanceof Organizzatore) {
            h.setOrganizzatore((Organizzatore) currentUser);
            hacks.add(h);
        }
    }

    /**
     * Crea e registra un nuovo hackathon dai parametri forniti.
     */
    public boolean creaHackathon(String titolo, String sede,
                                  LocalDateTime inizio, LocalDateTime fine,
                                  int maxPartecipanti, int dimensioneTeam) {
        if (currentUser instanceof Organizzatore) {
            Hackathon h = new Hackathon();
            h.setTitolo(titolo);
            h.setSede(sede);
            h.setDataInizio(inizio);
            h.setDataFine(fine);
            h.setMassimoPartecipanti(maxPartecipanti);
            h.setDimensioneTeam(dimensioneTeam);
            h.setOrganizzatore((Organizzatore) currentUser);
            hacks.add(h);
            return true;
        }
        return false;
    }

    public List<String> getMyHackathons() {
        if (currentUser instanceof Organizzatore) {
            Organizzatore o = (Organizzatore) currentUser;
            return hacks.stream()
                    .filter(h -> h.getOrganizzatore().equals(o))
                    .map(Hackathon::getTitolo)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
