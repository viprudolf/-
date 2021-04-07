package sample.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import sample.components.databaseHandler;
import sample.models.Client;
import sample.models.Lawyer;
import sample.models.privateCase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class editPrivateController {

    @FXML
    private JFXComboBox<String> comboResult;

    @FXML
    private JFXTextField lawyerField;

    @FXML
    private JFXTextField clientField;

    @FXML
    private JFXTextArea editAbout;

    databaseHandler db = new databaseHandler();

    private Boolean insert = false;
    private privateCase privateCase;

    private ArrayList<String> lawyers = new ArrayList<>();
    private ArrayList<String > cleints = new ArrayList<>();

    private PreparedStatement stmt;
    private Connection conn = db.getConnection();

    private ObservableList<String> results = FXCollections.observableArrayList();
    private Alert error = new Alert(Alert.AlertType.ERROR);
    private AutoCompletionBinding<String> autoComplete;
    private Pattern namePattern = Pattern.compile("^([А-я]+\\ [А-я]+\\ [А-я]+)");
    public editPrivateController() throws SQLException, ClassNotFoundException, IOException {
    }

    public void setPrivateCase(sample.models.privateCase privateCase) {
        this.privateCase = privateCase;
    }

    public void setInsert(Boolean insert) {
        this.insert = insert;
    }

    public void setCleints(ArrayList<String> cleints) {
        this.cleints = cleints;
    }

    public void setLawyer(ArrayList<String> lawyers) {
        this.lawyers = lawyers;
    }

    @FXML
    private void ok () throws SQLException {
        if (insert == true) {
            stmt = conn.prepareStatement("UPDATE private SET Result = ?, About = ? WHERE id = ?");
            stmt.setString(1, comboResult.getValue());
            stmt.setString(2, editAbout.getText());
            stmt.setInt(3, privateCase.getId());
            stmt.executeUpdate();
            privateCase.setResult(comboResult.getValue());
            privateCase.setAbout(editAbout.getText());
            Stage st = (Stage) lawyerField.getScene().getWindow();
            st.close();
        }
        else if (namePattern.matcher(lawyerField.getText()).find() != true) {
            error.setHeaderText("Введите имя адвоката корректно!");
            error.setContentText(null);
            error.setTitle("Ошибка");
            error.showAndWait();
            return;
        }
        else if (namePattern.matcher(clientField.getText()).find() != true) {
            error.setHeaderText("Введите имя клиента корректно!");
            error.setContentText(null);
            error.setTitle("Ошибка");
            error.showAndWait();
            return;
        }
        else if (insert == false) {
            stmt = conn.prepareStatement("INSERT INTO private (LawyerId, ClientId, Datetime, Result, About ) VALUES ((SELECT id FROM lawyer WHERE FullName= ?), (SELECT id FROM client WHERE FullName= ?), NOW(), ?, ? )");
            stmt.setString(1, lawyerField.getText());
            stmt.setString(2, clientField.getText());
            stmt.setString(3, comboResult.getValue());
            stmt.setString(4, editAbout.getText());
            stmt.executeUpdate();
            Stage st = (Stage) lawyerField.getScene().getWindow();
            st.close();
        }
    }
    @FXML
    private void cancel () {
        Stage st = (Stage) lawyerField.getScene().getWindow();
        st.close();
    }
    public void initialize () {
        results.addAll("Успешно", "Провалено", "В работе");
        comboResult.setItems(results);
        Platform.runLater(() -> {
            if (insert == true) {
                lawyerField.setDisable(true);
                clientField.setDisable(true);
                lawyerField.setText(privateCase.getLawyerName());
                clientField.setText(privateCase.getClientName());
                comboResult.setValue(privateCase.getResult());
                editAbout.setText(privateCase.getAbout());
            }
        });
        Platform.runLater(() -> {
            autoComplete = TextFields.bindAutoCompletion(lawyerField, lawyers);
            autoComplete.setMinWidth(330);
            autoComplete = TextFields.bindAutoCompletion(clientField, cleints);
            autoComplete.setMinWidth(330);
        });
        Platform.runLater(() -> {
            error.initOwner(clientField.getScene().getWindow());
        });
    }

}
