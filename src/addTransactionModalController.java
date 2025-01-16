import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class addTransactionModalController {

    private BankApplicationController mainController;

    @FXML
    private MenuButton transactionMenuButton;
    @FXML
    private TextField transactionTitleInput, transactionAmountInput;
    @FXML
    private DatePicker transactionDateInput;
    @FXML
    private Button submitTransactionButton, cancelTransactionButton;

    public void setMainController(BankApplicationController mainController) {
        this.mainController = mainController;
    }

    private UserSession session = UserSession.getInstance();
    private TransactionService transactionService = new TransactionService();

    @FXML
    private void initialize() {

        for (MenuItem item : transactionMenuButton.getItems()) {
            item.setOnAction(e -> transactionMenuButton.setText(item.getText()));
        }
    }

    public void handleNewTransaction(ActionEvent e) {
        if (checkAndHighlightEmptyFields()) {
            Transaction newTransaction = new Transaction(
                    session.getCustomerID(),
                    transactionTitleInput.getText(),
                    Double.parseDouble(transactionAmountInput.getText()),
                    transactionMenuButton.getText(),
                    transactionDateInput.getValue());

            submitTransactionToDatabase(newTransaction);

            Stage stage = (Stage) submitTransactionButton.getScene().getWindow();
            stage.close();
        }
    }

    public void handleCancelAction(ActionEvent e) {
        Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();

        stage.close();
    }

    private boolean checkAndHighlightEmptyFields() {
        boolean isValid = true;

        if (transactionTitleInput.getText().isEmpty() || !transactionTitleInput.getText().matches("^[a-zA-Z ]+$")) {
            transactionTitleInput.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(transactionTitleInput).play();
            isValid = false;
        } else {
            transactionTitleInput.setStyle(null);
        }

        try {
            double amount = Double.parseDouble(transactionAmountInput.getText());
            if (amount <= 0) {
                transactionAmountInput.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                new animatefx.animation.Shake(transactionAmountInput).play();
                isValid = false;
            } else {
                transactionAmountInput.setStyle(null);
            }
        } catch (NumberFormatException ex) {
            transactionAmountInput.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(transactionAmountInput).play();
            isValid = false;
        }

        if (transactionDateInput.getValue() == null) {
            transactionDateInput.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(transactionDateInput).play();
            isValid = false;
        } else {
            transactionDateInput.setStyle(null);
        }

        if ("Transaction Type".equals(transactionMenuButton.getText())) {
            transactionMenuButton.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(transactionMenuButton).play();
            isValid = false;
        } else {
            transactionMenuButton.setStyle(null);
        }

        return isValid;
    }

    private void submitTransactionToDatabase(Transaction transaction) {
        try {
            transactionService.insertTransaction(transaction);

            if (mainController != null) {
                mainController.addNewTransactionToDisplay(transaction);
            }

            System.out.println("Transaction successfully added to the database.");
        } catch (SQLException ex) {
            System.out.println("Error inserting new transaction: " + ex.getMessage());
        }
    }

    private void clearFormFields() {
        transactionTitleInput.clear();
        transactionAmountInput.clear();
        transactionMenuButton.setText("Transaction Type");
        transactionDateInput.setValue(null);
    }
}
