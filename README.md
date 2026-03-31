# Lern-Applikation zur adaptiven Prüfungsvorbereitung

Diese Desktop-Anwendung wurde im Rahmen eines IHK-Abschlussprojekts entwickelt. Sie dient der Vorbereitung auf fachtheoretische Prüfungen durch einen Fragenpool mit Datenbankanbindung.

Das System nutzt einen Algorithmus für die Wiederholung von Fragen, wobei falsch beantwortete Inhalte priorisiert werden (adaptive Lernmethode).

## Funktionen
- **Login-System:** Benutzerauthentifizierung mit Passworthashing.
- **Prüfungsmodus:** Strukturierte Abfrage verschiedener Kategorien.
- **Adaptives Lernen:** Automatisierte Auswahl von Schwachpunkten.
- **Auswertung:** Anzeige der erreichten Punktzahl und Ranking.

---

## Architektur und Technologien
Das Projekt wurde konsequent nach dem **Model-View-Controller (MVC) Muster** aufgebaut. 
* **Backend:** Java 21 LTS
* **Frontend:** JavaFX 21 (FXML)
* **Datenbank:** MySQL 8.0 Server
* **Persistenz-Layer:** Data Access Objects (DAO) via JDBC

---

## Installation und Setup

### 1. Datenbank einrichten
Nutze die Datei `schema.sql`, um deine Datenbankumgebung lokal vorbereiten.

### 2. Umgebungsvariablen (Environment Variables)
Das Programm kommuniziert aus Sicherheitsgründen nicht über fest programmierte Passwörter. Bitte konfiguriere folgende Umgebungsvariablen auf deinem PC:
* `DB_URL` : `jdbc:mysql://localhost:3306/learnapp`
* `DB_USER` : Dein MySQL-Benutzer (z.B. root)
* `DB_PASSWORD` : Dein MySQL-Passwort

### 3. Ausführung
Da das Projekt Maven verwendet, können alle Abhängigkeiten automatisch geladen werden:
```bash
mvn clean javafx:run
```
