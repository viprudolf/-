package sample.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sample.components.databaseHandler;
import sample.models.Lawyer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class editLawyerController {

    @FXML
    private JFXTextField numberField;

    @FXML
    private JFXTextField fullNameField;

    databaseHandler db = new databaseHandler();
    private PreparedStatement stmt;
    private Connection conn = db.getConnection();

    private Pattern namePattern = Pattern.compile("^([А-я]+\\ [А-я]+\\ [А-я]+)");

    private Alert error = new Alert(Alert.AlertType.ERROR);

    private Boolean insert = false;
    private Lawyer lawyer;
    public editLawyerController() throws SQLException, ClassNotFoundException, IOException {
    }

    public void setInsert(Boolean insert) {
        this.insert = insert;
    }

    public void setLawyer(Lawyer lawyer) {
        this.lawyer = lawyer;
    }

    @FXML
    private void Ok() throws SQLException {
    	try {
        if (namePattern.matcher(fullNameField.getText()).find() != true) {
            error.setTitle("Ошибка");
            error.setHeaderText("Введите имя корректно!");
            error.setContentText(null);
            error.showAndWait();
            return;
        }
        else if (insert == true) {
            stmt = conn.prepareStatement("UPDATE lawyer SET FullName = ?, MobilePhone = ? WHERE id = ?");
            stmt.setString(1, fullNameField.getText());
            stmt.setString(2, numberField.getText());
            stmt.setInt(3, lawyer.getId());
            stmt.executeUpdate();
            Stage st = (Stage) fullNameField.getScene().getWindow();
            lawyer.setFullName(fullNameField.getText());
            lawyer.setMobilePhone(numberField.getText());
            st.close();
        }
        else if (insert == false) {
            stmt = conn.prepareStatement("INSERT INTO lawyer (FullName, MobilePhone) VALUES (?, ?)");
            stmt.setString(1, fullNameField.getText());
            stmt.setString(2, numberField.getText());
            stmt.executeUpdate();
            Stage st = (Stage) fullNameField.getScene().getWindow();
            st.close();
        }
    } catch (SQLException s) {
    	    error.setHeaderText("Такого адвоката или клиента не существует!");
            error.setContentText("Выберите имена из списка подсказок.");
            error.setTitle("Ошибка");
            error.showAndWait();
    }

    }

    @FXML
    private void exit() {
        Stage st = (Stage) fullNameField.getScene().getWindow();
        st.close();
    }

    public void initialize (){
        Platform.runLater(() -> {
            if (insert == true) {
                fullNameField.setText(lawyer.getFullName());
                numberField.setText(lawyer.getMobilePhone());
            }
        });
        Platform.runLater(() -> {
            error.initOwner(numberField.getScene().getWindow());
        });
    }
}
