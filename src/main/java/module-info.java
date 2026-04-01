module de.lernapp.lern_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens de.lernapp.lern_app to javafx.fxml;
    opens de.lernapp.lern_app.controllers to javafx.fxml;
    opens de.lernapp.lern_app.dao to javafx.fxml;

    exports de.lernapp.lern_app;
    exports de.lernapp.lern_app.datenbank;
    exports de.lernapp.lern_app.dao;
}