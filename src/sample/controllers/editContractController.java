package sample.controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import sample.components.databaseHandler;
import sample.models.Contract;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class editContractController {

    @FXML
    private JFXTextField fullLawyerName;

    @FXML
    private JFXTextField fullClientName;

    @FXML
    private JFXTextField priceField;

    @FXML
    private JFXDatePicker dateStart;

    @FXML
    private JFXDatePicker dateEnd;

    private ArrayList<String> lawyers = new ArrayList<>();
    private ArrayList<String > cleints = new ArrayList<>();
    private AutoCompletionBinding<String> autoComplete;
    private Pattern namePattern = Pattern.compile("^([А-я]+\\ [А-я]+\\ [А-я]+)");
    private Pattern pricePattern = Pattern.compile("^([1-9]\\d{1,8}\\.[0-9]\\d{0,1})");
    private Boolean insert = false;
    private Alert error = new Alert(Alert.AlertType.ERROR);
    private Contract contract;


    databaseHandler db = new databaseHandler();
    private PreparedStatement stmt;
    private Connection conn = db.getConnection();

    public editContractController() throws SQLException, ClassNotFoundException, IOException {
    }

   /* public void setInsert(Boolean insert) {
        this.insert = insert;
    }*/

    public void setCleints(ArrayList<String> cleints) {
        this.cleints = cleints;
    }

    public void setLawyers(ArrayList<String> lawyers) {
        this.lawyers = lawyers;
    }

   /* public void setContract(Contract contract) {
        this.contract = contract;
    }
*/
    @FXML
    private void ok () throws SQLException {
        try {
        if (namePattern.matcher(fullLawyerName.getText()).find() != true) {
            error.setHeaderText("Введите имя адвоката корректно!");
            error.setContentText(null);
            error.setTitle("Ошибка");
            error.showAndWait();

        } else if (namePattern.matcher(fullClientName.getText()).find() != true) {
            error.setHeaderText("Введите имя клиента корректно!");
            error.setContentText(null);
            error.setTitle("Ошибка");
            error.showAndWait();

        } else if (dateEnd.getValue().isBefore(dateStart.getValue())) {
            error.setHeaderText("Выберите корректную дату!");
            error.setContentText(null);
            error.setTitle("Ошибка");
            error.showAndWait();

        } else if (Double.parseDouble(priceField.getText().replace(',', '.')) < 0 || pricePattern.matcher(priceField.getText().replace(',','.')).find() != true) {
            error.setHeaderText("Введите корректную сумму!");
            error.setContentText(null);
            error.setTitle("Ошибка");
            error.showAndWait();

        } else if (insert == true) {

            stmt = conn.prepareStatement("UPDATE contract SET FullName ");
        } else if (insert == false) {
            stmt = conn.prepareStatement("INSERT INTO contract (LawyerId, ClientId, StartDate, EndDate, Cost) VALUES ((SELECT id FROM lawyer WHERE FullName= ?), (SELECT id FROM client WHERE FullName= ?), ?, ?, ?)");
            stmt.setString(1, fullLawyerName.getText());
            stmt.setString(2, fullClientName.getText());
            stmt.setString(3, String.valueOf(dateStart.getValue()));
            System.out.println(dateEnd.getValue());
            stmt.setString(4, dateEnd.getValue().toString());
            stmt.setDouble(5, Double.parseDouble(priceField.getText().replace(',', '.')));
            stmt.executeUpdate();
            Stage st = (Stage) priceField.getScene().getWindow();
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
    private void cancel () {
        Stage st = (Stage) priceField.getScene().getWindow();
        st.close();
    }

    public void initialize () throws ParseException {
        Platform.runLater(() -> {
            error.initOwner(fullClientName.getScene().getWindow());
        });
        Platform.runLater(() -> {
            autoComplete = TextFields.bindAutoCompletion(fullLawyerName, lawyers);
            autoComplete.setMinWidth(330);
            autoComplete = TextFields.bindAutoCompletion(fullClientName, cleints);
            autoComplete.setMinWidth(330);
        });
    }

}

