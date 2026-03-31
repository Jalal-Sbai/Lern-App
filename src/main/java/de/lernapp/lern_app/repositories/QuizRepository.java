package de.lernapp.lern_app.repositories; // <- WICHTIG: Das hier an deinen Ordnernamen anpassen!

import de.lernapp.lern_app.models.LeaderboardEntry;

import de.lernapp.lern_app.datenbank.DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class QuizRepository {

    // Liest das Ranking dynamisch aus der Datenbank aus
    public List<LeaderboardEntry> getLeaderboard() {
        List<LeaderboardEntry> ranking = new ArrayList<>();

        // SQL-Befehl: Wertet die korrekten Antworten pro User aus
        String sql = "SELECT u.username, COUNT(r.id) AS score " +
                "FROM users u " +
                "JOIN user_results r ON u.id = r.user_id " +
                "WHERE r.is_correct = true " +
                "GROUP BY u.username " +
                "ORDER BY score DESC";

        DriverManager DatabaseConnection;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Das DTO-Objekt mit Namen und Punkten füllen
                ranking.add(new LeaderboardEntry(
                        rs.getString("username"),
                        rs.getInt("score")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ranking;
    }
} // <- DIESE KLAMMER FEHLTE BEI DIR
