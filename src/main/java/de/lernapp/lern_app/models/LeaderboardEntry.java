package de.lernapp.lern_app.models;

public class LeaderboardEntry {

    private final String username;
    private final int totalScore;

    public LeaderboardEntry(String username, int totalScore) {
        this.username = username;
        this.totalScore = totalScore;
    }

    // Getter für die JavaFX TableView
    public String getUsername() {

        return username; }

    public int getTotalScore() {
        return totalScore; }
}
