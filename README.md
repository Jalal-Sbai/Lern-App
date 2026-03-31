# Lern-Applikation zur adaptiven Prüfungsvorbereitung 🎓

Diese Desktop-Anwendung wurde im Rahmen eines IHK-Abschlussprojekts entworfen. Sie ermöglicht Teilnehmern, sich auf ihre Abschlussprüfung aus einem Fragenpool datenbankgestützt vorzubereiten. 

Das System zeichnet sich durch seinen **adaptiven Wiederhol-Algorithmus** aus: Fragen, die bei vergangenen Übungen falsch beantwortet wurden, werden priorisiert wiederholt.

## 🌟 Kernfunktionen
- **Sicheres Login-System:** Benutzerauthentifizierung inklusive Bcrypt-Passworthashing.
- **Multiple-Choice Prüfungen:** Randomisierte und strukturierte Abfrage von Kategorien.
- **Adaptives Lernen (Spaced Repetition):** Algorithmus-gesteuerte Fokus-Selektion schwacher Themengebiete.
- **Echtzeit-Scoring:** Punktzahlberechnung am Ende des Durchlaufs mit Leaderboard-Darstellung.

---

## 🏗️ Architektur & Technologien
Das Projekt wurde konsequent nach dem **Model-View-Controller (MVC) Muster** aufgebaut. 
* **Backend:** Java 21 LTS
* **Frontend:** JavaFX 21 (FXML)
* **Datenbank:** MySQL 8.0 Server
* **Persistenz-Layer:** Data Access Objects (DAO) via JDBC

---

## 🚀 Installation & Setup 

### 1. Datenbank einrichten
Nutze die Datei `schema.sql`, um deine Datenbankumgebung lokal vorzubereiten.

### 2. Umgebungsvariablen (Environment Variables)
Das Programm kommuniziert aus Sicherheitsgründen **nicht** über fest programmierte Passwörter. Bitte konfiguriere folgende Umgebungsvariablen auf deinem PC:
* `DB_URL` : `jdbc:mysql://localhost:3306/learnapp`
* `DB_USER` : Dein MySQL-Benutzer (Z.B. root)
* `DB_PASSWORD` : Dein MySQL-Passwort

### 3. Ausführung
Da das Projekt Maven verwendet, können alle Abhängigkeiten automatisch aufgelöst werden:
```bash
mvn clean javafx:run
```
