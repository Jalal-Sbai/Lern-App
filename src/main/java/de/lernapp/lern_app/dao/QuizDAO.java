package de.lernapp.lern_app.dao;

import de.lernapp.lern_app.models.LeaderboardEntry;
import de.lernapp.lern_app.models.Question;
import de.lernapp.lern_app.datenbank.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


 //Data Access Object (DAO) für Quiz-bezogene Daten.
 // Kapselt Zugriffe auf Fragen, Kategorien und Ergebnisse.

public class QuizDAO {
    private static final Logger LOGGER = Logger.getLogger(QuizDAO.class.getName());

     //Liest das Ranking (Leaderboard) dynamisch aus der Datenbank aus.

    public List<LeaderboardEntry> getLeaderboard() {
        List<LeaderboardEntry> ranking = new ArrayList<>();

        // SQL-Befehl: Wertet die korrekten Antworten pro User aus
        String sql = "SELECT u.username, COUNT(r.id) AS score " +
                "FROM users u " +
                "JOIN user_results r ON u.id = r.user_id " +
                "WHERE r.is_correct = true " +
                "GROUP BY u.username " +
                "ORDER BY score DESC";

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
            LOGGER.log(Level.SEVERE, "Fehler beim Laden des Leaderboards im QuizDAO", e);
        }
        return ranking;
    }


     // Lädt alle Fragen aus der Datenbank.

    public List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT id, text, option_a, option_b, option_c, option_d, correct_answer FROM questions";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                questions.add(new Question(
                        rs.getInt("id"),
                        rs.getString("text"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct_answer")
                ));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Fehler beim Laden der Fragen im QuizDAO", e);
        }
        return questions;
    }

     // Speichert das Ergebnis einer beantworteten Frage für einen Benutzer.

    public void saveResult(int userId, int questionId, boolean isCorrect) {
        String sql = "INSERT INTO user_results (user_id, question_id, is_correct) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, questionId);
            ps.setBoolean(3, isCorrect);
            ps.executeUpdate();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Fehler beim Speichern des Ergebnisses im QuizDAO", e);
        }
    }
}
