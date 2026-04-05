package de.lernapp.lern_app.controllers;

import de.lernapp.lern_app.dao.QuizDAO;
import de.lernapp.lern_app.models.LeaderboardEntry;
import de.lernapp.lern_app.models.Question;
import de.lernapp.lern_app.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import java.util.List;

 //  Controller für die Quiz-Ansicht.
 // Nutzt die QuizDAO-Klasse für den Datenzugriff auf das Leaderboard.

public class QuizController {

    @FXML private ListView<String> leaderboardListView;
    @FXML private Label questionLabel;
    @FXML private Button btnOptionA;
    @FXML private Button btnOptionB;
    @FXML private Button btnOptionC;
    @FXML private Button btnOptionD;
    @FXML private Label feedbackLabel;
    @FXML private Button btnNext;

    private QuizDAO quizDAO = new QuizDAO();
    private User currentUser;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private Question currentQuestion;

    public void setUser(User user) {
        this.currentUser = user;
    }

    @FXML
    public void initialize() {
        refreshLeaderboard();
        loadQuestions();
    }
    
    private void loadQuestions() {
        questions = quizDAO.getQuestions();
        if (questions != null && !questions.isEmpty()) {
            showQuestion(0);
        } else {
            questionLabel.setText("Keine Fragen in der Datenbank gefunden.");
            disableAnswerButtons(true);
        }
    }

    private void showQuestion(int index) {
        if (index >= questions.size()) {
            questionLabel.setText("Quiz beendet! Du hast alle Fragen beantwortet.");
            disableAnswerButtons(true);
            btnNext.setVisible(false);
            return;
        }
        
        currentQuestion = questions.get(index);
        questionLabel.setText(currentQuestion.getText());
        
        btnOptionA.setText("A: " + currentQuestion.getOptionA());
        btnOptionB.setText("B: " + currentQuestion.getOptionB());
        
        if (currentQuestion.getOptionC() != null) {
            btnOptionC.setText("C: " + currentQuestion.getOptionC());
            btnOptionC.setVisible(true);
        } else {
            btnOptionC.setVisible(false);
        }
        
        if (currentQuestion.getOptionD() != null) {
            btnOptionD.setText("D: " + currentQuestion.getOptionD());
            btnOptionD.setVisible(true);
        } else {
            btnOptionD.setVisible(false);
        }
        
        feedbackLabel.setText("");
        btnNext.setVisible(false);
        btnNext.setDisable(true);
        disableAnswerButtons(false);
    }
    
    @FXML public void handleAnswerA(ActionEvent event) { processAnswer("A"); }
    @FXML public void handleAnswerB(ActionEvent event) { processAnswer("B"); }
    @FXML public void handleAnswerC(ActionEvent event) { processAnswer("C"); }
    @FXML public void handleAnswerD(ActionEvent event) { processAnswer("D"); }

    private void processAnswer(String selectedOption) {
        disableAnswerButtons(true);
        boolean isCorrect = selectedOption.equalsIgnoreCase(currentQuestion.getCorrectAnswer());
        
        if (isCorrect) {
            feedbackLabel.setText("Richtig!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
        } else {
            feedbackLabel.setText("Falsch! Richtig wäre: " + currentQuestion.getCorrectAnswer());
            feedbackLabel.setStyle("-fx-text-fill: red;");
        }
        // Vermeidung von Null-Pointern und Speichern in DAO
        if (currentUser != null) {
            quizDAO.saveResult(currentUser.getId(), currentQuestion.getId(), isCorrect);
            refreshLeaderboard();
        }
        
        btnNext.setVisible(true);
        btnNext.setDisable(false);
    }

    @FXML
    public void handleNext(ActionEvent event) {
        currentQuestionIndex++;
        showQuestion(currentQuestionIndex);
    }
    
    private void disableAnswerButtons(boolean disable) {
        btnOptionA.setDisable(disable);
        btnOptionB.setDisable(disable);
        btnOptionC.setDisable(disable);
        btnOptionD.setDisable(disable);
    }
     //  Lädt die aktuellen Ranking-Daten über den DAO und zeigt sie in der Liste an.

    private void refreshLeaderboard() {
        if (leaderboardListView == null) return;
        
        List<LeaderboardEntry> list = quizDAO.getLeaderboard();
        leaderboardListView.getItems().clear();
        
        for (LeaderboardEntry entry : list) {
            leaderboardListView.getItems().add(entry.getUsername() + ": " + entry.getTotalScore() + " Punkte");
        }
    }
}
