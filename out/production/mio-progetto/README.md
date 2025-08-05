# Applicativo

Piattaforma demo per la gestione di hackathon. Include modelli di dominio, controller e un esempio di DAO PostgreSQL.

## Panoramica

Funzionalità principali:

- Registrazione degli utenti e creazione dei team.
- Gestione dell'iscrizione agli hackathon con chiusura delle registrazioni e limite di partecipanti.
- Caricamento dei documenti di avanzamento da parte dei team.
- Votazione finale da parte dei giudici con calcolo della classifica.

Entità di dominio:

- `Utente` con ruoli **ORGANIZER**, **JUDGE** e **PARTICIPANT**.
- `Hackathon` che gestisce regole e iscrizioni.
- `Team` con limite di membri e funzionalità di join.
- `Progress` per l'aggiornamento dei lavori dei team.
- `Vote` per il voto dei giudici ai team.

## Requisiti
- Java 17
- Maven
- PostgreSQL (runtime) / H2 (test)

## Setup database

### PostgreSQL per l'esecuzione
1. Installare PostgreSQL e creare un database ad esempio `app`.
2. Avviare l'applicazione fornendo l'URL e le credenziali tramite system properties:
   ```
   java -Ddb.url=jdbc:postgresql://localhost:5432/app \
        -Ddb.user=postgres \
        -Ddb.password=postgres \
        -cp target/Applicativo-1.0-SNAPSHOT.jar model.Main
   ```

### H2 per i test
I test usano un database in‑memory H2 configurato nel profilo Maven `test`.
Eseguire i test con:
```
mvn -DskipTests=false -P test test
```

## Build ed esecuzione

1. Compilare il progetto:
   ```
   mvn package
   ```
2. Eseguire l'applicazione:
   ```
   java -cp target/Applicativo-1.0-SNAPSHOT.jar model.Main
   ```

## Documentazione generata

- Javadoc: `target/site/apidocs/index.html`
- Diagrammi PlantUML: `docs/sequence/registration.puml`, `docs/sequence/finalVoting.puml`
