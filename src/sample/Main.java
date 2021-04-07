package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.components.databaseHandler;
import java.sql.Connection;
import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            Connection conn = databaseHandler.getConnection();
        } catch (ClassNotFoundException | SQLException e1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(null);
            alert.setHeaderText("Соединение с базой не установлено");
            alert.showAndWait();
            return;
        }
        Parent root = FXMLLoader.load(getClass().getResource("views/main.fxml"));
        primaryStage.setTitle("Адвокатская контора");
        primaryStage.setScene(new Scene(root, 1063, 497));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("sample/images/lawyer.png"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
