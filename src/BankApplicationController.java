import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;

public class BankApplicationController {

    @FXML
    private Stage stage;
    private Scene scene;

    @FXML
    private Label accountNumberField, accountTypeField, balanceField, welcomeLabel, dateTimeLabel;

    @FXML
    private Button dashboardButton, manageAccountsButton, settingsButton, addTransactionButton;

    @FXML
    private VBox transactionList;
    private TransactionService transactionService = new TransactionService();

    private UserSession session = UserSession.getInstance();

    public void initialize() {
        String username = session.getUsername();
        updateUIWithUserData(username);
    }

    public void setWelcomeLabel(String name) {
        welcomeLabel.setText(name);
    }

    public void setAccountNumberField(long accountNumber) {
        accountNumberField.setText("Account number: " + Long.toString(accountNumber));
    }

    public void setBalanceField(double balance) {
        String formattedBalance = String.format("%.2f", balance);
        balanceField.setText('$' + formattedBalance);
    }

    public void setTimeDate() {
        DateTimeFormatter dft = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");

        KeyFrame frame = new KeyFrame(Duration.seconds(1), e -> {
            LocalDateTime time = LocalDateTime.now();
            dateTimeLabel.setText(dft.format(time));
        });

        Timeline timeline = new Timeline(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void setAccountType(String accountType) {
        accountTypeField.setText(accountType + " account");
    }

    public BankAccount updateUIWithUserData(String username) {
        BankDatabaseConnector bankDatabaseConnector = new BankDatabaseConnector();
        BankAccount bankAccount = bankDatabaseConnector.getUserDetails(username);
        setWelcomeLabel(bankAccount.getFullName());
        setAccountNumberField(bankAccount.getAccountNumber());
        setBalanceField(bankAccount.getBalance());
        setAccountType(bankAccount.getAccountType());
        loadUserTransactions();
        return bankAccount;
    }

    public void switchToSettingScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("settingsPage.fxml"));
            Parent root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Settings");
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchToBankDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("bankApplication.fxml"));
            Parent root = loader.load();
            updateUIWithUserData(session.getUsername());
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Dashboard");
            scene = new Scene(root);
            stage.setScene(scene);
            updateUIWithUserData(session.getUsername());
            stage.show();
            stage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadUserTransactions() {

        int currentUserCustomerID = session.getCustomerID();

        List<Transaction> transactionList = transactionService.getTransactionsByCustomerID(currentUserCustomerID);

        updateTransactionList(transactionList);
    }

    private void updateTransactionList(List<Transaction> transactions) {

        transactionList.getChildren().clear();

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);

            HBox transactionBox = new HBox(10);

            Label transactionTitle = new Label(transaction.getTransactionTitle());
            Label amountLabel = new Label(String.format("%.2f", transaction.getAmount()));
            Label transactionDate = new Label(transaction.getTransactionDate().toString());
            Label transactionType = new Label(transaction.getTransactionType());

            if ("Deposit".equals(transaction.getTransactionType())) {
                transactionBox.setStyle("-fx-background-color: lightgreen;");
            } else {
                transactionBox.setStyle("-fx-background-color: lightcoral;");
            }

            transactionBox.getChildren().addAll(transactionTitle, amountLabel, transactionDate, transactionType);

            transactionBox.setPadding(new Insets(10));
            transactionBox.setAlignment(Pos.CENTER_LEFT);

            transactionList.getChildren().add(transactionBox);
        }
    }

    public void openAddTransactionModal(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addTransactionModal.fxml"));
            Parent root = loader.load();

            addTransactionModalController modalController = loader.getController();

            modalController.setMainController(this);

            Stage modalStage = new Stage();
            modalStage.setTitle("Add Transaction");
            Scene scene = new Scene(root);
            modalStage.setScene(scene);
            modalStage.initModality(Modality.WINDOW_MODAL);
            Stage parentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            modalStage.initOwner(parentStage);
            modalStage.setResizable(false);

            modalStage.showAndWait();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void addNewTransactionToDisplay(Transaction transaction) {

        HBox transactionBox = new HBox(10);

        Label transactionTitle = new Label(transaction.getTransactionTitle());
        Label amountLabel = new Label(String.format("%.2f", transaction.getAmount()));
        Label transactionDate = new Label(transaction.getTransactionDate().toString());
        Label transactionType = new Label(transaction.getTransactionType());

        transactionBox.getChildren().addAll(transactionTitle, amountLabel, transactionDate, transactionType);

        if ("Deposit".equals(transaction.getTransactionType())) {
            transactionBox.setStyle("-fx-background-color: lightgreen;");
        } else {
            transactionBox.setStyle("-fx-background-color: lightcoral;");
        }

        transactionBox.setPadding(new Insets(10));
        transactionBox.setAlignment(Pos.CENTER_LEFT);

        transactionList.getChildren().add(0, transactionBox);
    }
}