package de.lernapp.lern_app.datenbank;

import java.sql.Connection;
import java.sql.DriverManager;

 //  Utility-Klasse für den Aufbau der Datenbankverbindung.
 // Die Zugangsdaten werden sicher über Umgebungsvariablen ausgelesen.

public class DatabaseConnection {


     //Stellt eine Verbindung zur 'learnapp' Datenbank her (Sicher via Env-Vars).
     //return Connection Objekt
     //throws java.sql.SQLException bei Verbindungsfehlern

    public static Connection getConnection() throws java.sql.SQLException {
        String envUrl = System.getenv("DB_URL");
        System.out.println("DEBUG: DB_URL aus Umgebungsvariable: " + (envUrl != null ? envUrl : "NICHT GESETZT"));

        String url = (envUrl != null && !envUrl.isEmpty()) ? envUrl : "jdbc:mysql://localhost:3306/learnapp?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
        String user = System.getenv().getOrDefault("DB_USER", "root");
        String password = System.getenv().getOrDefault("DB_PASSWORD", "root123");
        
        System.out.println("DEBUG: Verbinde mit URL: " + url);
        return DriverManager.getConnection(url, user, password);
    }
}
