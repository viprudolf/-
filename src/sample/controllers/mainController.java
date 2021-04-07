package sample.controllers;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sample.components.databaseHandler;
import sample.models.*;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class mainController {

    @FXML
    private Pane journalPane;

    @FXML
    private Pane contractPane;

    @FXML
    private Pane clientPane;

    @FXML
    private Pane lawyerPane;

    @FXML
    private Pane privatePane;

    @FXML
    private JFXButton clientButton;

    @FXML
    private JFXButton privateButton;

    @FXML
    private JFXButton lawyerButton;

    @FXML
    private JFXButton contractButton;

    @FXML
    private JFXButton journalButton;

    @FXML
    private TableView<Client> clientTable;

    @FXML
    private TableColumn<Client, String> cllientId;

    @FXML
    private TableColumn<Client, String> fullName;

    @FXML
    private TableColumn<Client, String> dateBornCleint;

    @FXML
    private TableColumn<Client, String> mobileClient;

    @FXML
    private TableView<privateCase> privateTable;

    @FXML
    private TableColumn<privateCase, String> privateId;

    @FXML
    private TableColumn<privateCase, String> privateLawyer;

    @FXML
    private TableColumn<privateCase, String> privateClient;

    @FXML
    private TableColumn<privateCase, String> privateDate;

    @FXML
    private TableColumn<privateCase, String> privateRes;

    @FXML
    private TableColumn<privateCase, String> privateAbout;

    @FXML
    private JFXTextArea aboutArea;

    @FXML
    private TableView<Lawyer> lawyerTable;

    @FXML
    private TableColumn<Lawyer, String> lawyerName;

    @FXML
    private TableColumn<Lawyer, String> lawyerPhone;

    @FXML
    private Label successfulCount;

    @FXML
    private Label failedCount;

    @FXML
    private Label workCount;

    @FXML
    private RadioButton radioAll;

    @FXML
    private RadioButton radioSuccess;

    @FXML
    private RadioButton radioWorking;

    @FXML
    private RadioButton radioFailed;

    @FXML
    private JFXTextField searchLawyer;

    @FXML
    private JFXTextField searchPrivate;

    @FXML
    private RadioButton radioPrivateId;

    @FXML
    private RadioButton radioPrivateClient;

    @FXML
    private RadioButton radioPrivateDate;

    @FXML
    private JFXTextField searchCleint;

    @FXML
    private RadioButton radioClientName;

    @FXML
    private RadioButton radioClientMob;

    @FXML
    private TableView<Contract> contractTable;

    @FXML
    private TableColumn<Contract, String> numberCont;

    @FXML
    private TableColumn<Contract, String> nameLawyerCont;

    @FXML
    private TableColumn<Contract, String> nameClientCont;

    @FXML
    private TableColumn<Contract, String> startContDate;

    @FXML
    private TableColumn<Contract, String> endContDate;

    @FXML
    private TableColumn<Contract, String> costCont;

    @FXML
    private JFXTextField searchContract;

    @FXML
    private RadioButton radioNumbCotr;

    @FXML
    private RadioButton radioClientCont;

    @FXML
    private RadioButton radioLawyerCont;

    @FXML
    private JFXDatePicker endDateContract;

    @FXML
    private TableView<Journal> historyTable;

    @FXML
    private TableColumn<Journal, String> historyId;

    @FXML
    private TableColumn<Journal, String> hostoryClient;

    @FXML
    private TableColumn<Journal, String> historyLawyer;

    @FXML
    private TableColumn<Journal, String> historyDate;

    @FXML
    private JFXTextArea oldAbout;

    @FXML
    private TableColumn<Journal, String> oldAboutColumn;

    @FXML
    private JFXCheckBox checkIsValid;

    @FXML
    private JFXTextField withClientField;

    private Alert error = new Alert(Alert.AlertType.ERROR);

    private ToggleGroup toggleLawyer = new ToggleGroup();
    private ToggleGroup togglePrivate = new ToggleGroup();
    private ToggleGroup toggleClient = new ToggleGroup();
    private ToggleGroup toggleContract = new ToggleGroup();

    databaseHandler db = new databaseHandler();
    private Connection conn = db.getConnection();
    private PreparedStatement stmt;
    private ResultSet res;

    // Clients
    public ObservableList<Client> clients = FXCollections.observableArrayList();

    // Private cases
    private ObservableList<privateCase> cases = FXCollections.observableArrayList();
    private Pattern noFullName = Pattern.compile("(\\S+\\s)(\\S{1})\\S+\\s(\\S{1})\\S+");

    // Lawyer
    private ObservableList<Lawyer> lawyers = FXCollections.observableArrayList();

    public mainController() throws SQLException, ClassNotFoundException, ParseException, IOException {
    }

    // Contracts
    private ObservableList<Contract> contracts = FXCollections.observableArrayList();
    private ObservableList<Contract> sortedContracts = FXCollections.observableArrayList();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // History
    private ObservableList<Journal> history = FXCollections.observableArrayList();

    private Integer searchPrivateType = 0;
    private Integer searchClientType = 1;
    private Integer searchContractType = 0;

    private void initClient() throws SQLException {
        clients.clear();
        stmt = conn.prepareStatement("SELECT * FROM client");
        res = stmt.executeQuery();
        while (res.next()) {
            int id = res.getInt("Id");
            String FullName = res.getString("FullName");
            String MobilePhone = res.getString("MobilePhone");
            String DateBorn = res.getString("DateBorn");
            clients.addAll(new Client(id, FullName, MobilePhone, DateBorn));
        }
        clientTable.setItems(clients);
        clientTable.refresh();
    }

    private void initCases() throws SQLException {
        cases.clear();
        stmt = conn.prepareStatement("SELECT p.id, l.FullName, c.FullName, Datetime, Result, About FROM private as p, lawyer as l, client as c WHERE c.id = p.ClientId AND l.id = p.LawyerId;");
        res = stmt.executeQuery();
        while (res.next()) {
            int id = res.getInt("Id");
            String lawyerName = res.getString(2);
            String clientName = res.getString(3);
            String date = res.getString("Datetime");
            String result = res.getString("Result");
            String about = res.getString("About");
            cases.addAll(new privateCase(id, clientName.replaceAll(String.valueOf(noFullName), "$1$2.$3."), lawyerName.replaceAll(String.valueOf(noFullName), "$1$2.$3."), date, result, about));
        }
        privateTable.setItems(cases);
    }

    @FXML
    private void initAbout() {
        if (privateTable.getSelectionModel().getSelectedIndex() < 0) {
            return;
        } else {
            String about = privateTable.getSelectionModel().getSelectedItem().getAbout();
            aboutArea.setText(about);
        }
    }

    @FXML
    private void initLawyer() throws SQLException {
        lawyers.clear();
        stmt = conn.prepareStatement("SELECT * FROM lawyer");
        res = stmt.executeQuery();
        while (res.next()) {
            int id = res.getInt("Id");
            String fullName = res.getString("FullName");
            String mobile = res.getString("MobilePhone");
            lawyers.addAll(new Lawyer(id, fullName, mobile));
        }
        lawyerTable.setItems(lawyers);
        lawyerTable.refresh();
    }

    @FXML
    private void initStatLawyer() throws SQLException {
        if (lawyerTable.getSelectionModel().getSelectedIndex() < 0) {
            return;
        }
        int id = lawyerTable.getSelectionModel().getSelectedItem().getId();
        CallableStatement cStmt = conn.prepareCall("{call stat(?, ?, ?, ?)}");
        cStmt.setInt(1, id);
        cStmt.registerOutParameter(2, Types.INTEGER);
        cStmt.registerOutParameter(3, Types.INTEGER);
        cStmt.registerOutParameter(4, Types.INTEGER);
        cStmt.executeQuery();
        successfulCount.setText("Успешных: " + cStmt.getInt(2));
        workCount.setText("В работе: " + cStmt.getInt(4));
        failedCount.setText("Провалено: " + cStmt.getInt(3));
    }

    private void initContract() throws SQLException {
        contracts.clear();
        stmt = conn.prepareStatement("SELECT c.id, l.FullName, cl.FullName, c.StartDate, c.EndDate, c.Cost FROM contract AS c, lawyer AS l, client AS cl WHERE l.id = LawyerId AND cl.id = ClientId");
        res = stmt.executeQuery();
        while (res.next()) {
            int id = res.getInt("Id");
            String lawyerName = res.getString("l.FullName");
            String clientName = res.getString("cl.FullName");
            Date startDate = res.getDate("StartDate");
            Date endDate = res.getDate("EndDate");
            Double cost = res.getDouble("Cost");
            contracts.addAll(new Contract(id, lawyerName.replaceAll(String.valueOf(noFullName), "$1$2.$3."), clientName.replaceAll(String.valueOf(noFullName), "$1$2.$3."), startDate, endDate, cost));
        }
        contractTable.setItems(contracts);
        contractTable.refresh();
    }

    @FXML
    private void initValidContracts() throws SQLException {
        if (checkIsValid.isSelected() == true) {
            contracts.clear();
            stmt = conn.prepareStatement("SELECT c.id, l.FullName, cl.FullName, c.StartDate, c.EndDate, c.Cost FROM contract AS c, lawyer AS l, client AS cl WHERE l.id = LawyerId AND cl.id = ClientId AND EndDate > CURRENT_DATE() ORDER BY EndDate");
            res = stmt.executeQuery();
            while (res.next()) {
                int id = res.getInt("Id");
                String lawyerName = res.getString("l.FullName");
                String clientName = res.getString("cl.FullName");
                Date startDate = res.getDate("StartDate");
                Date endDate = res.getDate("EndDate");
                Double cost = res.getDouble("Cost");
                contracts.addAll(new Contract(id, lawyerName.replaceAll(String.valueOf(noFullName), "$1$2.$3."), clientName.replaceAll(String.valueOf(noFullName), "$1$2.$3."), startDate, endDate, cost));
            }
            contractTable.setItems(contracts);
            contractTable.refresh();
        } else {
            initContract();
        }

    }

    @FXML
    private void searchDate() throws ParseException, SQLException {
        if (endDateContract.getValue() == null) {
            return;
        }
        sortedContracts.clear();
        if (checkIsValid.isSelected() == true) {
            initValidContracts();
        }
        else {
            initContract();
        }
        Date dt = dateFormat.parse(String.valueOf(endDateContract.getValue()));
        for (Contract cont : contractTable.getItems()) {
            if (cont.getEndDate().before(dt) || cont.getEndDate().equals(dt)) {
                sortedContracts.addAll(new Contract(cont.getId(), cont.getLawyerName(), cont.getClientName(), cont.getStartDate(), cont.getEndDate(), cont.getCost()));
            }
        }
        contracts.clear();
        contractTable.setItems(sortedContracts);
        contractTable.refresh();
        contractTable.scrollTo(0);
    }

    @FXML
    private void clearDate() throws SQLException {
        if (checkIsValid.isSelected() == true) {
            initValidContracts();
        }
        else {
            endDateContract.getEditor().clear();
            sortedContracts.clear();
            contractTable.refresh();
            initContract();
            contractTable.scrollTo(0);
        }
    }

    @FXML
    private void sortOnSuccess() throws SQLException {
        lawyers.clear();
        stmt = conn.prepareStatement("SELECT l.id, l.FullName, l.MobilePhone FROM lawyer AS l, private AS p WHERE l.id = p.lawyerid AND p.Result = 'Успешно' GROUP BY FullName, MobilePhone ORDER BY count(p.result) DESC");
        res = stmt.executeQuery();
        while (res.next()) {
            int id = res.getInt("Id");
            String fullName = res.getString("FullName");
            String mobile = res.getString("MobilePhone");
            lawyers.addAll(new Lawyer(id, fullName, mobile));
        }
        lawyerTable.setItems(lawyers);
        lawyerTable.refresh();
    }

    @FXML
    private void sortOnWorking() throws SQLException {
        lawyers.clear();
        stmt = conn.prepareStatement("SELECT l.id, l.FullName, l.MobilePhone FROM lawyer AS l, private AS p WHERE l.id = p.lawyerid AND p.Result = 'В работе' GROUP BY FullName, MobilePhone ORDER BY count(p.result) DESC");
        res = stmt.executeQuery();
        while (res.next()) {
            int id = res.getInt("Id");
            String fullName = res.getString("FullName");
            String mobile = res.getString("MobilePhone");
            lawyers.addAll(new Lawyer(id, fullName, mobile));
        }
        lawyerTable.setItems(lawyers);
        lawyerTable.refresh();
    }

    @FXML
    private void sortOnFailed() throws SQLException {
        lawyers.clear();
        stmt = conn.prepareStatement("SELECT l.id, l.FullName, l.MobilePhone FROM lawyer AS l, private AS p WHERE l.id = p.lawyerid AND p.Result = 'Провалено' GROUP BY FullName, MobilePhone ORDER BY count(p.result) DESC");
        res = stmt.executeQuery();
        while (res.next()) {
            int id = res.getInt("Id");
            String fullName = res.getString("FullName");
            String mobile = res.getString("MobilePhone");
            lawyers.addAll(new Lawyer(id, fullName, mobile));
        }
        lawyerTable.setItems(lawyers);
        lawyerTable.refresh();
    }

    private void initJournal() throws SQLException {
        history.clear();
        stmt = conn.prepareStatement("SELECT h.id, c.FullName, l.FullName, h.Date, h.oldAbout FROM client AS c, lawyer AS l, history AS h WHERE\tc.id = h.clientid AND l.id = h.LawyerId");
        res = stmt.executeQuery();
        while (res.next()) {
            Integer id = res.getInt("Id");
            String cleintName = res.getString("c.FullName");
            String lawyerName = res.getString("l.FullName");
            Timestamp dateTime = res.getTimestamp("Date");
            String oldAbout = res.getString("h.oldAbout");
            history.addAll(new Journal(id, cleintName, lawyerName, dateTime, oldAbout));
        }
        historyTable.setItems(history);
    }

    @FXML
    private void initOldAbout() {
        if (historyTable.getSelectionModel().getSelectedIndex() < 0) {
            return;
        } else {
            String about = historyTable.getSelectionModel().getSelectedItem().getOldValue();
            oldAbout.setText(about);
        }
    }

    // Edit client
    @FXML
    private void editClient() throws IOException {
        if (clientTable.getSelectionModel().getSelectedIndex() < 0) {
            error.setHeaderText("Выберите запись");
            error.setTitle("Ошибка");
            error.setContentText(null);
            error.showAndWait();
            return;
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/editClient.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            editClientController editClientController = fxmlLoader.getController();
            editClientController.setInsert(true);
            editClientController.setClient(clientTable.getSelectionModel().getSelectedItem());
            stage.showAndWait();
            clientTable.refresh();
        }
    }

    @FXML
    private void addClient() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/editClient.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image("sample/images/lawyer.png"));
        stage.showAndWait();
        initClient();
        clientTable.refresh();
        clientTable.getSelectionModel().selectLast();
        clientTable.scrollTo(clientTable.getSelectionModel().getFocusedIndex());
    }

    @FXML
    private void editPrivateCase() throws IOException {
        if (privateTable.getSelectionModel().getSelectedIndex() < 0) {
            error.setHeaderText("Выберите запись");
            error.setTitle("Ошибка");
            error.setContentText(null);
            error.showAndWait();
            return;
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/editPrivate.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            editPrivateController editPrivateController = fxmlLoader.getController();
            editPrivateController.setInsert(true);
            editPrivateController.setPrivateCase(privateTable.getSelectionModel().getSelectedItem());
            stage.getIcons().add(new Image("sample/images/lawyer.png"));
            stage.showAndWait();
            privateTable.refresh();
        }
    }

    @FXML
    private void addPrivateCase() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/editPrivate.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        editPrivateController editPrivateController = fxmlLoader.getController();
        ArrayList<String> lawyers = new ArrayList<>();
        ArrayList<String> cleints = new ArrayList<>();
        for (Lawyer l : lawyerTable.getItems()) {
            lawyers.add(l.getFullName());
        }
        editPrivateController.setLawyer(lawyers);
        for (Client c : clientTable.getItems()) {
            cleints.add(c.getFullName());
        }
        editPrivateController.setCleints(cleints);
        stage.getIcons().add(new Image("sample/images/lawyer.png"));
        stage.showAndWait();
        initCases();
        clientTable.refresh();
        privateTable.getSelectionModel().selectLast();
        privateTable.scrollTo(privateTable.getSelectionModel().getFocusedIndex());
    }

    @FXML
    private void addLawyer() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/editLawyer.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image("sample/images/lawyer.png"));
        stage.showAndWait();
        initLawyer();
        lawyerTable.refresh();
        lawyerTable.getSelectionModel().selectLast();
        lawyerTable.scrollTo(lawyerTable.getSelectionModel().getFocusedIndex());
    }

    @FXML
    private void editLawyer() throws IOException {
        if (lawyerTable.getSelectionModel().getSelectedIndex() < 0) {
            error.setHeaderText("Выберите запись");
            error.setTitle("Ошибка");
            error.setContentText(null);
            error.showAndWait();
            return;
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/editLawyer.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            editLawyerController editLawyerController = fxmlLoader.getController();
            editLawyerController.setInsert(true);
            editLawyerController.setLawyer(lawyerTable.getSelectionModel().getSelectedItem());
            stage.getIcons().add(new Image("sample/images/lawyer.png"));
            stage.showAndWait();
            lawyerTable.refresh();
        }
    }

    @FXML
    private void addContract() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/editContract.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        editContractController editContractController = fxmlLoader.getController();
        ArrayList<String> lawyers = new ArrayList<>();
        ArrayList<String> cleints = new ArrayList<>();
        for (Lawyer l : lawyerTable.getItems()) {
            lawyers.add(l.getFullName());
        }
        editContractController.setLawyers(lawyers);
        for (Client c : clientTable.getItems()) {
            cleints.add(c.getFullName());
        }
        editContractController.setCleints(cleints);
        stage.getIcons().add(new Image("sample/images/lawyer.png"));
        stage.showAndWait();
        initContract();
        contractTable.refresh();
        contractTable.getSelectionModel().selectLast();
        contractTable.scrollTo(contractTable.getSelectionModel().getFocusedIndex());
    }

    @FXML
    private void toExcel () throws IOException, SQLException {

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet spreadsheet = wb.createSheet("Grades");
        Row row = spreadsheet.createRow(0);
        // Get columns from gradeTable
        for (int j = 0; j < historyTable.getColumns().size(); j++) {
            row.createCell(j).setCellValue(historyTable.getColumns().get(j).getText());
        }

        for (int j = 0; j < historyTable.getColumns().size(); j++) {
            row.createCell(j).setCellValue(historyTable.getColumns().get(j).getText());
        }

        for (int i = 0; i < historyTable.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 1);
            for (int j = 0; j < historyTable.getColumns().size(); j++) {
                if(historyTable.getColumns().get(j).getCellData(i) != null) {
                    row.createCell(j).setCellValue(historyTable.getColumns().get(j).getCellData(i).toString());
                    row.createCell(j+1).setCellValue("");
                }
                else {
                    row.createCell(j).setCellValue("");
                }
                spreadsheet.autoSizeColumn(j);
            }
        }
        FileChooser file = new FileChooser();
        File selected;
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel xlsx", "*.xlsx");
        file.getExtensionFilters().add(extFilter);
        if ((selected = file.showSaveDialog(historyTable.getScene().getWindow())) != null) {

            FileOutputStream fileOut = new FileOutputStream(selected);
            wb.write(fileOut);
            fileOut.close();
        }
    }

        public void initialize () throws SQLException {
            initClient();
            initCases();
            initLawyer();
            initContract();
            initJournal();
            Platform.runLater(() -> {
                error.initOwner(withClientField.getScene().getWindow());
            });
            // Clients
            fullName.setCellValueFactory(new PropertyValueFactory<>("FullName"));
            mobileClient.setCellValueFactory(new PropertyValueFactory<>("MobilePhone"));
            dateBornCleint.setCellValueFactory(new PropertyValueFactory<>("DateBorn"));
            radioClientName.setToggleGroup(toggleClient);
            radioClientMob.setToggleGroup(toggleClient);
            radioClientName.setSelected(true);
            radioClientName.setOnAction(event -> {
                searchCleint.clear();
                searchCleint.setPromptText("По имени");
                searchClientType = 1;
            });
            radioClientMob.setOnAction(event -> {
                searchCleint.clear();
                searchCleint.setPromptText("По номеру телефона");
                searchClientType = 3;
                clientTable.refresh();
            });
            // Private cases
            privateClient.setCellValueFactory(new PropertyValueFactory<>("clientName"));
            privateLawyer.setCellValueFactory(new PropertyValueFactory<>("lawyerName"));
            privateDate.setCellValueFactory(new PropertyValueFactory<>("datetime"));
            privateRes.setCellValueFactory(new PropertyValueFactory<>("result"));
            privateAbout.setCellValueFactory(new PropertyValueFactory<>("about"));
            privateId.setCellValueFactory(new PropertyValueFactory<>("id"));
            radioPrivateClient.setToggleGroup(togglePrivate);
            radioPrivateId.setToggleGroup(togglePrivate);
            radioPrivateDate.setToggleGroup(togglePrivate);
            radioPrivateId.setSelected(true);
            radioPrivateId.setOnAction(event -> {
                searchPrivate.clear();
                searchPrivate.setPromptText("По номеру");
                searchPrivateType = 0;
            });
            radioPrivateClient.setOnAction(event -> {
                searchPrivate.clear();
                searchPrivate.setPromptText("По фамилии клиента");
                searchPrivateType = 2;
            });
            radioPrivateDate.setOnAction(event -> {
                searchPrivate.clear();
                searchPrivate.setPromptText("По дате");
                searchPrivateType = 3;
            });

            // Lawyers
            lawyerName.setCellValueFactory(new PropertyValueFactory<>("FullName"));
            lawyerPhone.setCellValueFactory(new PropertyValueFactory<>("MobilePhone"));
            radioAll.setSelected(true);
            radioAll.setToggleGroup(toggleLawyer);
            radioSuccess.setToggleGroup(toggleLawyer);
            radioWorking.setToggleGroup(toggleLawyer);
            radioFailed.setToggleGroup(toggleLawyer);
            clientButton.setOnAction(event -> {
                searchCleint.clear();
                clientPane.toFront();
            });
            privateButton.setOnAction(event -> {
                try {
                    searchPrivate.clear();
                    initCases();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                privatePane.toFront();
            });
            lawyerButton.setOnAction(event -> {
                try {
                    searchLawyer.clear();
                    initLawyer();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                lawyerPane.toFront();
            });
            contractButton.setOnAction(event -> {
                try {
                    searchContract.clear();
                    initContract();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                contractPane.toFront();
            });
            journalButton.setOnAction(event -> {
                try {
                    withClientField.clear();
                    initJournal();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                journalPane.toFront();
            });

            // Contract
            numberCont.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameClientCont.setCellValueFactory(new PropertyValueFactory<>("lawyerName"));
            nameLawyerCont.setCellValueFactory(new PropertyValueFactory<>("clientName"));
            startContDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            endContDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            costCont.setCellValueFactory(new PropertyValueFactory<>("cost"));
            radioClientCont.setToggleGroup(toggleContract);
            radioLawyerCont.setToggleGroup(toggleContract);
            radioNumbCotr.setToggleGroup(toggleContract);
            radioNumbCotr.setSelected(true);
            radioLawyerCont.setOnAction(event -> {
                searchContract.clear();
                searchContract.setPromptText("По имени адвоката");
                searchContractType = 1;
            });
            radioNumbCotr.setOnAction(event -> {
                searchContract.clear();
                searchContract.setPromptText("По номеру контракта");
                searchContractType = 0;
            });
            radioClientCont.setOnAction(event -> {
                searchContract.clear();
                searchContract.setPromptText("По имени клиента");
                searchContractType = 2;
            });

            // History
            hostoryClient.setCellValueFactory(new PropertyValueFactory<>("clientName"));
            historyLawyer.setCellValueFactory(new PropertyValueFactory<>("lawyerName"));
            historyDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            historyId.setCellValueFactory(new PropertyValueFactory<>("id"));
            oldAboutColumn.setCellValueFactory(new PropertyValueFactory<>("oldValue"));


            //Search lawyer
            ObservableList lawyerData = lawyerTable.getItems();
            searchLawyer.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (oldValue != null && (newValue.length() < oldValue.length())) {
                    lawyerTable.setItems(lawyerData);
                }
                String value = newValue.toLowerCase();
                ObservableList<Lawyer> subLawyer = FXCollections.observableArrayList();

                long count = lawyerTable.getColumns().stream().count();
                for (int i = 0; i < lawyerTable.getItems().size(); i++) {
                    for (int j = 0; j < count; j++) {
                        String entry = "" + lawyerTable.getColumns().get(1).getCellData(i);
                        if (entry.toLowerCase().contains(value)) {
                            subLawyer.add(lawyerTable.getItems().get(i));
                            break;
                        }
                    }
                }
                lawyerTable.setItems(subLawyer);
            });

            // Search private case
            ObservableList parivateData = privateTable.getItems();
            searchPrivate.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (oldValue != null && (newValue.length() < oldValue.length())) {
                    privateTable.setItems(parivateData);
                }
                String value = newValue.toLowerCase();
                ObservableList<privateCase> subPrivateCase = FXCollections.observableArrayList();

                long count = privateTable.getColumns().stream().count();
                for (int i = 0; i < privateTable.getItems().size(); i++) {
                    for (int j = 0; j < count; j++) {
                        String entry = "" + privateTable.getColumns().get(searchPrivateType).getCellData(i);
                        if (entry.toLowerCase().contains(value)) {
                            subPrivateCase.add(privateTable.getItems().get(i));
                            break;
                        }
                    }
                }
                privateTable.setItems(subPrivateCase);
            });

            // Search client
            ObservableList clientData = clientTable.getItems();
            searchCleint.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (oldValue != null && (newValue.length() < oldValue.length())) {
                    clientTable.setItems(clientData);
                }
                String value = newValue.toLowerCase();
                ObservableList<Client> subClient = FXCollections.observableArrayList();

                long count = clientTable.getColumns().stream().count();
                for (int i = 0; i < clientTable.getItems().size(); i++) {
                    for (int j = 0; j < count; j++) {
                        String entry = "" + clientTable.getColumns().get(searchClientType).getCellData(i);
                        if (entry.toLowerCase().contains(value)) {
                            subClient.add(clientTable.getItems().get(i));
                            break;
                        }
                    }
                }
                clientTable.setItems(subClient);
            });

            // Search contract
            ObservableList contractData = contractTable.getItems();
            searchContract.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (oldValue != null && (newValue.length() < oldValue.length())) {
                    contractTable.setItems(contractData);
                }
                String value = newValue.toLowerCase();
                ObservableList<Contract> subContract = FXCollections.observableArrayList();

                long count = contractTable.getColumns().stream().count();
                for (int i = 0; i < contractTable.getItems().size(); i++) {
                    for (int j = 0; j < count; j++) {
                        String entry = "" + contractTable.getColumns().get(searchContractType).getCellData(i);
                        if (entry.toLowerCase().contains(value)) {
                            subContract.add(contractTable.getItems().get(i));
                            break;
                        }
                    }
                }
                contractTable.setItems(subContract);
            });

            // Journal
            ObservableList historyData = historyTable.getItems();
            withClientField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (oldValue != null && (newValue.length() < oldValue.length())) {
                    historyTable.setItems(historyData);
                }
                String value = newValue.toLowerCase();
                ObservableList<Journal> subHistory = FXCollections.observableArrayList();

                long count = historyTable.getColumns().stream().count();
                for (int i = 0; i < historyTable.getItems().size(); i++) {
                    for (int j = 0; j < count; j++) {
                        String entry = "" + historyTable.getColumns().get(1).getCellData(i);
                        if (entry.toLowerCase().contains(value)) {
                            subHistory.add(historyTable.getItems().get(i));
                            break;
                        }
                    }
                }
                historyTable.setItems(subHistory);
            });
        }
    }
