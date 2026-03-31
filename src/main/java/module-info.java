module de.lernapp.lern_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens de.lernapp.lern_app to javafx.fxml;
    exports de.lernapp.lern_app;
    exports de.lernapp.lern_app.datenbank;
    opens de.lernapp.lern_app.datenbank to javafx.fxml;
}