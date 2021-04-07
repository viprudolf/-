package sample.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sample.components.databaseHandler;
import sample.models.Client;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.regex.Pattern;

public class editClientController {

    @FXML
    private JFXTextField numberField;

    @FXML
    private JFXTextField dateBornField;

    @FXML
    private JFXTextField fullNameField;

    databaseHandler db = new databaseHandler();

    private PreparedStatement stmt;
    private Connection conn = db.getConnection();

    private Client client;

    private Boolean insert = false;

    private Alert error = new Alert(Alert.AlertType.ERROR);

    private Pattern datePattern = Pattern.compile("^([0-9]{4}+\\-[0-9]{2}+\\-[0-9]{2})");
    private Pattern namePattern = Pattern.compile("^([А-я]+\\ [А-я]+\\ [А-я]+)");
    public editClientController() throws SQLException, ClassNotFoundException, ParseException, IOException {
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setInsert(Boolean insert) {
        this.insert = insert;
    }

    @FXML
    private void exit () {
        Stage st = (Stage) fullNameField.getScene().getWindow();
        st.close();
    }

    @FXML
    private void Ok () throws SQLException, ParseException, ClassNotFoundException {
        
        if (datePattern.matcher(dateBornField.getText()).find() != true) {
            error.setHeaderText("Введите правильно дату ГГГГ-ММ-ДД");
            error.setTitle("Ошибка");
            error.setContentText(null);
            error.showAndWait();
            return;
        }
        else if (namePattern.matcher(fullNameField.getText()).find() != true) {
            error.setHeaderText("Введите полное имя клиента");
            error.setContentText(null);
            error.setTitle("Ошибка");
            error.showAndWait();
            return;
        }
        if (insert == true) {
            stmt = conn.prepareStatement("UPDATE client SET FullName = ?, MobilePhone = ?, DateBorn = ? WHERE id = ?");
            stmt.setString(1, fullNameField.getText());
            stmt.setString(2, numberField.getText());
            stmt.setString(3, dateBornField.getText());
            stmt.setInt(4, client.getId());
            stmt.executeUpdate();
            Stage st = (Stage) fullNameField.getScene().getWindow();
            st.close();
            set();
        }
        else if (insert == false) {
            stmt = conn.prepareStatement("INSERT INTO client (FullName, MobilePhone, DateBorn) VALUES (?, ?, ?)");
            stmt.setString(1, fullNameField.getText());
            stmt.setString(2, numberField.getText());
            stmt.setString(3, dateBornField.getText());
            stmt.executeUpdate();
            Stage st = (Stage) fullNameField.getScene().getWindow();
            st.close();
        }
    }
    private void set () {
        client.setFullName(fullNameField.getText());
        client.setDateBorn(dateBornField.getText());
        client.setMobilePhone(numberField.getText());
    }
    public void initialize () {

        Platform.runLater(() -> {
            if (insert == true) {
                fullNameField.setText(client.getFullName());
                dateBornField.setText(client.getDateBorn());
                numberField.setText(client.getMobilePhone());
            }
        });
        Platform.runLater(() -> {
            error.initOwner(numberField.getScene().getWindow());
        });
    }
}
