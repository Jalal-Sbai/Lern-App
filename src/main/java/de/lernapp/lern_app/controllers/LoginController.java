package de.lernapp.lern_app.controllers;

import de.lernapp.lern_app.models.User;
import de.lernapp.lern_app.repositories.UserRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private UserRepository userRepository = new UserRepository();

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Bitte fülle alle Felder aus!");
            return;
        }

        User loggedInUser = userRepository.authenticateUser(username, password);

        if (loggedInUser != null) {
            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("Login erfolgreich!");
            // In einer vollen App würde hier der Scene-Switch zum Quiz kommen.
        } else {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Benutzername oder Passwort falsch.");
        }
    }
}
