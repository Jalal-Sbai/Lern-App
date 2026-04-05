# Lern-App für die IHK Prüfungsvorbereitung 

Willkommen in meinem Repository! Dies ist mein finales IHK-Abschlussprojekt. Ich habe hier eine datenbankgestützte Desktop-Anwendung gebaut, mit der man sich interaktiv auf Theorieprüfungen vorbereiten kann. 

Die Idee dahinter: Ein intelligentes Quiz, das sich merkt, welche Antworten falsch waren, und diese Fragen dann öfter wiederholt, bis sie sitzen (Adaptives Lernen).

## Features
- **Sicherer Login:** Echte Benutzerauthentifizierung mit sicherem Passwort-Hashing.
- **Intelligentes Quiz:** Fragen werden live aus der MySQL-Datenbank geladen. Schwachstellen des Nutzers werden gezielt trainiert. 
- **Live Leaderboard:** Ein dynamisches Ranking zeigt sofort an, wer die meisten Fragen richtig beantwortet hat.

## Mein Tech-Stack
Ich habe großen Wert auf eine saubere Softwarearchitektur gelegt und das Programm konsequent nach dem **MVC-Pattern** (Model-View-Controller) aufgebaut.
- **Sprache:** Java 21 LTS
- **Oberfläche (GUI):** JavaFX 21 (mit FXML)
- **Datenbank:** MySQL 8.0 
- **Datenzugriff:** DAO-Pattern (Data Access Objects) via JDBC
- **Build-Tool:** Maven

## So startest du das Projekt lokal 

Damit du den Code bei dir testen kannst, musst du nur kurz die Datenbank aufsetzen:

1. **Datenbank erstellen:**  
   Führe einfach das beiliegende Skript `schema.sql` auf deinem eigenen MySQL Server aus. Das erstellt automatisch alle nötigen Tabellen (wie z.B. `user_results`) für dich.

2. **Umgebungsvariablen setzen:**  
   Da ich keine Datenbank-Passwörter direkt im Quellcode abspeichern wollte (Sicherheit geht vor!), liest die App diese Daten aus den lokalen Umgebungsvariablen deines PCs. Lege eifach folgende Variablen in Windows an:
   - `DB_URL`: `jdbc:mysql://localhost:3306/learnapp`
   - `DB_USER`: Dein Datenbank-Benutzername (z.B. `root`)
   - `DB_PASSWORD`: Dein MySQL-Passwort

3. **App starten:**  
   Da das Projekt Maven nutzt, zieht es sich alle UI-Bibliotheken und Treiber selbst. Du kannst es einfach über deine IDE oder über das Terminal starten:
   ```bash
   mvn clean javafx:run
   ```

Viel Spaß beim Ausprobieren! 
