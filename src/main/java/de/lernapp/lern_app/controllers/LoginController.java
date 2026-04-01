package de.lernapp.lern_app.controllers;

import de.lernapp.lern_app.MainApp;
import de.lernapp.lern_app.dao.UserDAO;
import de.lernapp.lern_app.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private UserDAO userDAO = new UserDAO();

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Bitte fülle alle Felder aus!");
            return;
        }

        User loggedInUser = userDAO.authenticateUser(username, password);

        if (loggedInUser != null) {
            try {
                // Wechsel zur Quiz-View und Übergabe des Nutzers
                switchSceneToQuiz(event, loggedInUser);
            } catch (IOException e) {
                errorLabel.setText("Fehler beim Laden der Quiz-Ansicht.");
                e.printStackTrace();
            }
        } else {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Benutzername oder Passwort falsch.");
        }
    }

    @FXML
    public void switchToRegister(ActionEvent event) {
        try {
            switchScene(event, "RegisterView.fxml", "Lern-App | Registrierung");
        } catch (IOException e) {
            errorLabel.setText("Fehler beim Laden der Registrierung.");
            e.printStackTrace();
        }
    }

    private void switchScene(ActionEvent event, String fxmlFile, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void switchSceneToQuiz(ActionEvent event, User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("QuizView.fxml"));
        Parent root = loader.load();
        
        QuizController quizController = loader.getController();
        quizController.setUser(user);
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Lern-App | Quiz & Statistik");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
