package de.lernapp.lern_app.controllers;

import de.lernapp.lern_app.MainApp;
import de.lernapp.lern_app.dao.UserDAO;
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

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label statusLabel;

    private final UserDAO userDAO = new UserDAO();


     //Verarbeitet die Registrierung eines neuen Benutzers.

    @FXML
    public void handleRegistration(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        // Validierung
        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Bitte alle Felder ausfüllen.");
            return;
        }

        if (!password.equals(confirm)) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Passwörter stimmen nicht überein!");
            return;
        }

        // Registrierung über DAO
        boolean success = userDAO.registerUser(username, password);

        if (success) {
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Registrierung erfolgreich! Bitte einloggen.");
            // Optional: Felder leeren
            usernameField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
        } else {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Registrierung fehlgeschlagen (Name vergeben?).");
        }
    }

     // Springt zurück zum Login-Fenster.

    @FXML
    public void switchToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("LoginView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Lern-App | Login");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            statusLabel.setText("Fehler beim Laden des Logins.");
            e.printStackTrace();
        }
    }
}
