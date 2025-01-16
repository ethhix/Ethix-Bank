
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Node;

public class LoginPageController {

    private Stage stage;
    private Scene scene;

    @FXML
    private TextField usernameField, passwordTextField;

    @FXML
    PasswordField passwordField;

    @FXML
    ImageView visibleButtonIcon;

    @FXML
    private Button loginButton, passwordHideButton;

    @FXML
    private Hyperlink createAccountLink;

    @FXML
    private CheckBox rememberAccountBox;

    private BankDatabaseConnector bankDatabaseConnector = new BankDatabaseConnector();

    private Image openEye = new Image(
            "file:///C:/Users/nicho/OneDrive/Desktop/Java%20Projects/Ethix-Bank/src/images/visible-eye.png");
    private Image closedEye = new Image(
            "file:///C:/Users/nicho/OneDrive/Desktop/Java%20Projects/Ethix-Bank/src/images/eye.png");

    @FXML
    public void initialize() {
        visibleButtonIcon.setPickOnBounds(true);

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!passwordTextField.isFocused()) {
                passwordTextField.setText(newValue);
            }
        });

        passwordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!passwordField.isFocused()) {
                passwordField.setText(newValue);
            }
        });
    }

    public void switchCreateAccount(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("createAccountPage.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Create Account");
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleLoginButton(ActionEvent event) {
        try {
            if (bankDatabaseConnector.findUser(retrieveUsername(), retrievePassword())) {
                BankAccount bankAccount = bankDatabaseConnector.getUserDetails(retrieveUsername());
                if (bankAccount != null) {
                    UserSession userSession = UserSession.getInstance();
                    userSession.setUsername(retrieveUsername());
                    userSession.setfullName(bankAccount.getFullName());
                    userSession.setBalance(bankAccount.getBalance());
                    userSession.setAccountType(bankAccount.getAccountType());
                    userSession.setAccountNumber(bankAccount.getAccountNumber());

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("bankApplication.fxml"));
                    Parent root = loader.load();

                    BankApplicationController controller = loader.getController();
                    controller.updateUIWithUserData(retrieveUsername());

                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setTitle("Ethix Bank");
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    stage.setResizable(false);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText(null);
                    alert.setHeaderText(null);
                    alert.setTitle(null);
                    alert.setContentText("User details not found.");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(null);
                alert.setHeaderText(null);
                alert.setTitle("Invalid login");
                alert.setContentText("Invalid username or password. Please try again.");
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPassword(ActionEvent event) {

        if (passwordField.isVisible()) {
            passwordTextField.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            passwordTextField.setVisible(true);
            passwordTextField.setManaged(true);
            visibleButtonIcon.setImage(closedEye);
        } else {
            passwordField.setText(passwordTextField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);
            visibleButtonIcon.setImage(openEye);
        }
    }

    private String retrieveUsername() {
        return usernameField.getText();
    }

    private String retrievePassword() {
        return passwordField.getText();
    }
}
