package de.lernapp.lern_app.dao;

import de.lernapp.lern_app.models.User;
import de.lernapp.lern_app.datenbank.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;


 // Data Access Object (DAO) für die Benutzerverwaltung.
 //Implementiert Login und Registrierung.

public class UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());


     // Authentifiziert einen Benutzer gegen die Datenbank.

    public User authenticateUser(String username, String rawPassword) {
        String sql = "SELECT id, username, password_hash FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String dbPassword = rs.getString("password_hash");
                    if (rawPassword.equals(dbPassword)) {
                        return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            dbPassword
                        );
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Fehler bei der Authentifizierung im UserDAO", e);
        }
        return null;
    }


    //  Registriert einen neuen Benutzer in der Datenbank.

    public boolean registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, username);
            ps.setString(2, password);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            LOGGER.log(Level.WARNING, "Name bereits vergeben: " + username);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Fehler bei der Registrierung im UserDAO", e);
        }
        return false;
    }
}