package de.lernapp.lern_app.datenbank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

    public static void createAndInsert() {
        Connection connection = null;
        Statement statement = null;

        try {
            // 1. Verbindungsaufbau (Sicher via Umgebungsvariablen)
            String dbUser = System.getenv().getOrDefault("DB_USER", "root");
            String dbPass = System.getenv().getOrDefault("DB_PASSWORD", "root123");

            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/",
                    dbUser,
                    dbPass
            );

            statement = connection.createStatement();

            // 2. Datenbank learnapp erstellen (und Fehler vermeiden, falls sie existiert)
            statement.executeUpdate("DROP DATABASE IF EXISTS learnapp");
            statement.executeUpdate("CREATE DATABASE learnapp");
            statement.executeUpdate("USE learnapp");

            System.out.println("Datenbank 'learnapp' erfolgreich erstellt und ausgewählt.");

            // 3. Tabellenstruktur erstellen (categories und questions)
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS categories (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "name VARCHAR(100) NOT NULL" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4");

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS questions (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "text VARCHAR(500) NOT NULL, " +
                            "category_id INT NOT NULL, " +
                            "FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4");

            System.out.println("Tabellen erfolgreich angelegt.");

            // 4. Transaktion starten (AutoCommit deaktivieren für Sicherheit)
            connection.setAutoCommit(false);

            // 4.1. Zuerst Kategorien einfügen (PreparedStatement 1)
            String insertCategory = "INSERT INTO categories (name) VALUES (?)";
            try (PreparedStatement pstmtCat = connection.prepareStatement(insertCategory)) {
                pstmtCat.setString(1, "WISO");
                pstmtCat.executeUpdate();

                pstmtCat.setString(1, "IT-Systeme");
                pstmtCat.executeUpdate();

                pstmtCat.setString(1, "Vernetzung");
                pstmtCat.executeUpdate();
            }

            // 4.2. Dann Beispielfragen einfügen (PreparedStatement 2)
            String insertQuestion = "INSERT INTO questions (text, category_id) VALUES (?, ?)";
            try (PreparedStatement pstmtQuest = connection.prepareStatement(insertQuestion)) {
                // Frage für Kategorie 1 (WISO)
                pstmtQuest.setString(1, "Was bedeutet die Abkürzung GmbH?");
                pstmtQuest.setInt(2, 1);
                pstmtQuest.executeUpdate();

                // Frage für Kategorie 2 (IT-Systeme)
                pstmtQuest.setString(1, "Was ist der Unterschied zwischen RAM und ROM?");
                pstmtQuest.setInt(2, 2);
                pstmtQuest.executeUpdate();

                // Frage für Kategorie 3 (Vernetzung)
                pstmtQuest.setString(1, "Welcher Port ist Standard für HTTP?");
                pstmtQuest.setInt(2, 3);
                pstmtQuest.executeUpdate();

                // 5. Commit (Finale Bestätigung der Änderungen an die DB)
                connection.commit();
                System.out.println("Transaktion erfolgreich: Testdaten sicher in 'learnapp' gespeichert.");

            } catch (Exception e) {
                // Bei einem Fehler werden die eingefügten Kategorien/Fragen zurückgesetzt!
                connection.rollback();
                LOGGER.log(Level.SEVERE, "Fehler beim Einfügen der Testdaten, Rollback ausgeführt", e);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Genereller Fehler bei der Datenbank-Verbindung (SQL)", e);
            try {
                if (connection != null)
                    connection.rollback();
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Rollback fehlgeschlagen", ex);
            }
        } finally {
            // 6. Ressourcen sauber schließen (Speicher freigeben)
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
                System.out.println("Datenbankverbindung ordnungsgemäß geschlossen.");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Fehler bei der Tabellenerstellung", e);
            }
        }
    }

    /**
     * Stellt eine Verbindung zur 'learnapp' Datenbank her (Sicher via Env-Vars).
     * @return Connection Objekt
     */
    public static Connection getConnection() throws java.sql.SQLException {
        String url = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/learnapp");
        String user = System.getenv().getOrDefault("DB_USER", "root");
        String password = System.getenv().getOrDefault("DB_PASSWORD", "root123");
        return java.sql.DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {
        // Skript beim Start der Main-Methode ausführen
        createAndInsert();
    }
}
