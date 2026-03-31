package de.lernapp.lern_app.repositories;

import de.lernapp.lern_app.models.User;
import de.lernapp.lern_app.datenbank.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRepository {
    private static final Logger LOGGER = Logger.getLogger(UserRepository.class.getName());

    public User authenticateUser(String username, String rawPassword) {
        String sql = "SELECT id, username, password_hash FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String dbPasswordHash = rs.getString("password_hash");
                    
                    if (rawPassword.equals(dbPasswordHash)) {
                        return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            dbPasswordHash
                        );
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Fehler bei der Benutzer-Authentifizierung", e);
        }
        return null;
    }
}
