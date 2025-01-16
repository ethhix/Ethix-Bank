import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class settingsSceneController {

    private UserSession session = UserSession.getInstance();
    private BankAccount bankAccount = new BankAccount();
    private BankDatabaseConnector bankDatabaseConnector = new BankDatabaseConnector();
    private Stage stage;
    private Scene scene;

    @FXML
    private Label accountNumberField, accountTypeField, welcomeLabel;

    @FXML
    private Button dashboardButton, logoutButton, manageAccountsButton, settingsButton, updateButton;

    @FXML
    private TextField emailEditableField, passwordEditableField, phoneNumberEditableField;

    public void initialize() {
        emailEditableField.setText(session.getEmail());
        passwordEditableField.setPromptText("Enter updated password");
        phoneNumberEditableField.setText(session.getPhoneNumber());
    }

    public void switchToBankDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("bankApplication.fxml"));
            Parent root = loader.load();

            BankApplicationController controller = loader.getController();
            controller.updateUIWithUserData(session.getUsername());

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Dashboard");
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout(ActionEvent event) {
        session.resetSession();
        bankAccount.clearUserDetails();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginPage.fxml"));
            Parent root = loader.load();

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Login");
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleUpdateButton(ActionEvent event) {
        String email = emailEditableField.getText();
        String password = passwordEditableField.getText();
        String phoneNumber = phoneNumberEditableField.getText();

        boolean isUpdated = false;

        if (!email.equals(session.getEmail())) {
            bankDatabaseConnector.updateCustomerDetails(session.getUsername(), email, null, null);
            session.setEmail(email);
            isUpdated = true;
        }

        if (!password.isEmpty() && !password.equals(session.getPassword())) {
            bankDatabaseConnector.updateCustomerDetails(session.getUsername(), null, password, null);
            session.setPassword(password);
            isUpdated = true;
        }

        if (!phoneNumber.equals(session.getPhoneNumber())) {
            bankDatabaseConnector.updateCustomerDetails(session.getUsername(), null, null, phoneNumber);
            session.setPhoneNumber(phoneNumber);
            isUpdated = true;
        }

        if (isUpdated) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Details Updated");
            alert.setContentText("Your details have been updated.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Changes Detected");
            alert.setContentText("No updates were made as no changes were detected.");
            alert.showAndWait();
        }
    }
}
