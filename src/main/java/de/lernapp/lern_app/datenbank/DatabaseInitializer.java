package de.lernapp.lern_app.datenbank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

 // Diese Klasse dient dem einmaligen Setup der Datenbankstruktur.
 // Sie nutzt PreparedStatements und Transaktionen (Rollback im Fehlerfall).

public class DatabaseInitializer {
    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());

    public static void main(String[] args) {
        initializeDatabase();
    }

    public static void initializeDatabase() {
        Connection connection = null;
        Statement statement = null;

        try {
            // 1. Verbindungsaufbau (Sicher via Umgebungsvariablen)
            String dbUser = System.getenv().getOrDefault("DB_USER", "root");
            String dbPass = System.getenv().getOrDefault("DB_PASSWORD", "root123");

            // Verbindung zum Server (ohne DB-Name) zum Erstellen der DB
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/",
                    dbUser,
                    dbPass
            );

            statement = connection.createStatement();

            // 2. Datenbank erstellen (wie von dir gewünscht)
            statement.executeUpdate("DROP DATABASE IF EXISTS learnapp");
            statement.executeUpdate("CREATE DATABASE learnapp");
            statement.executeUpdate("USE learnapp");

            System.out.println("Datenbank 'learnapp' erfolgreich erstellt und ausgewählt.");

            // 3. Tabellenstruktur erstellen (wie von dir vorgegeben mit InnoDB und UTF8MB4)
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS categories (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4");

            // Hinweis: Ich behalte die Antwort-Spalten (a, b, c, d, correct) bei, 
            // damit die Quiz-App auch wirklich funktioniert.
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS questions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "text VARCHAR(500) NOT NULL, " +
                    "option_a VARCHAR(255), " +
                    "option_b VARCHAR(255), " +
                    "option_c VARCHAR(255), " +
                    "option_d VARCHAR(255), " +
                    "correct_answer CHAR(1), " +
                    "category_id INT NOT NULL, " +
                    "FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4");

            // Nutzer-Tabelle (für Login)
            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) UNIQUE NOT NULL, " +
                "password_hash VARCHAR(255) NOT NULL)"
            );

            // 4. Ergebnisse (Für den adaptiven Algorithmus / Leaderboard)
            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS user_results (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "question_id INT NOT NULL, " +
                "is_correct BOOLEAN NOT NULL, " +
                "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE" +
                ") ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4"
            );

            System.out.println("Tabellen erfolgreich angelegt.");

            // 4. Transaktion starten (AutoCommit deaktivieren für Sicherheit)
            connection.setAutoCommit(false);

            try {
                // 4.1. Kategorien einfügen (PreparedStatement)
                String insertCategory = "INSERT INTO categories (name) VALUES (?)";
                try (PreparedStatement pstmtCat = connection.prepareStatement(insertCategory)) {
                    String[] categories = {"WISO", "IT-Systeme", "Vernetzung"};
                    for (String cat : categories) {
                        pstmtCat.setString(1, cat);
                        pstmtCat.executeUpdate();
                    }
                }

                // 4.2. Beispielfragen einfügen (PreparedStatement)
                String insertQuestion = "INSERT INTO questions (text, category_id, option_a, option_b, option_c, option_d, correct_answer) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmtQuest = connection.prepareStatement(insertQuestion)) {
                    // Frage 1: WISO (Kat 1)
                    pstmtQuest.setString(1, "Was bedeutet die Abkürzung GmbH?");
                    pstmtQuest.setInt(2, 1);
                    pstmtQuest.setString(3, "Gesellschaft mit beschränkter Haftung");
                    pstmtQuest.setString(4, "Große moderne Bau-Holding");
                    pstmtQuest.setString(5, "Gute Mitglieder bei Hilfe");
                    pstmtQuest.setString(6, "None");
                    pstmtQuest.setString(7, "A");
                    pstmtQuest.executeUpdate();

                    // Frage 2: IT-Systeme (Kat 2)
                    pstmtQuest.setString(1, "Was ist der Unterschied zwischen RAM und ROM?");
                    pstmtQuest.setInt(2, 2);
                    pstmtQuest.setString(3, "RAM=flüchtig, ROM=statisch");
                    pstmtQuest.setString(4, "Beide gleich");
                    pstmtQuest.setString(5, "RAM=Read All Media");
                    pstmtQuest.setNull(6, java.sql.Types.VARCHAR);
                    pstmtQuest.setString(7, "A");
                    pstmtQuest.executeUpdate();
                }
                
                // 4.3. Test-Nutzer (student / ihk2024)
                statement.executeUpdate("INSERT INTO users (username, password_hash) VALUES ('student', 'ihk2024')");

                // 5. Commit
                connection.commit();
                System.out.println("Transaktion erfolgreich: Testdaten sicher gespeichert.");

            } catch (Exception e) {
                // Rollback im Fehlerfall
                connection.rollback();
                LOGGER.log(Level.SEVERE, "Fehler beim Einfügen der Testdaten, Rollback ausgeführt", e);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Genereller Fehler bei der Datenbank-Initialisierung", e);
        } finally {
            // 6. Ressourcen schließen
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
                System.out.println("Verbindung ordnungsgemäß geschlossen.");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Fehler beim Schließen der Verbindung", e);
            }
        }
    }
}
