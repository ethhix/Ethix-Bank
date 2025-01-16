import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class createAccountPageController implements Initializable {

    private Stage stage;
    private Scene scene;

    @FXML
    private ChoiceBox<String> accountType;

    @FXML
    private TextField firstNameField, lastNameField, addressField, cityField, zipCodeField, phoneNumberField,
            emailField, usernameField, passwordField;

    @FXML
    private Button createAccountButton, cancelCreateAccount;

    @FXML
    private CheckBox termsCheckbox;

    @FXML
    private ProgressBar passwordStrengthBar;

    @FXML
    private Label passwordStrengthLabel, phoneError, emailError, zipCodeError, accountTypeError, termsError;

    private String[] accountTypes = { "Checking", "Savings" };

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        accountType.getItems().addAll(accountTypes);

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            double strength = calculatePasswordStrength(newValue);

            updateProgressBar(strength);
        });
    }

    // Allows for switching to the login page scene
    public void switchToLoginPage(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This method is responsible for validing the user's input and creating a new
    // account.
    public boolean handleCreateAccount(ActionEvent event) {
        boolean isAccountCreated = false;
        Customer customer = new Customer(this);
        BankAccount bankAccount = new BankAccount();
        passwordValid();
        if (!termsCheckbox.isSelected()) {
            termsError.setText("Agree to our terms and service to continue");
            isAccountCreated = false;
        }
        if (!customer.isValid()) {
            checkAndHighlightEmptyFields();
            isAccountCreated = false;
        } else { // fix so that we dont repeatedly setup listeners more than we need to
            checkAndHighlightEmptyFields();
            bankAccount.setAccountNumber(bankAccount.createAccountNumber());
            bankAccount.setFullName(customer.retrieveFullName());
            bankAccount.setBalance(0);
            bankAccount.setAccountType(createAccountPageController.this.getAccountType());
            BankDatabaseConnector.insertCustomer(customer);
            BankDatabaseConnector.insertBankAccount(bankAccount);
            showInformationAlert("Account created successfully!");
            isAccountCreated = true;
            switchToLoginPage(event);
        }
        return isAccountCreated;
    }

    private void setupFieldListeners() {
        TextField[] fields = { firstNameField, lastNameField, addressField, cityField, usernameField, passwordField };
        for (TextField field : fields) {
            field.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.isEmpty()) {
                    field.setStyle(null);
                }
            });
        }

        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                emailField.setStyle(null);
                emailError.setVisible(false);
            }
        });

        zipCodeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                zipCodeField.setStyle(null);
                zipCodeError.setVisible(false);
            }
        });

        phoneNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                phoneNumberField.setStyle(null);
                phoneError.setVisible(false);
            }
        });

        accountType.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                accountType.setStyle(null);
                accountTypeError.setText("");
            }
        });

        termsCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                termsError.setVisible(false);
            } else {
                termsError.setVisible(true);
            }
        });
    }

    // Checks and highlights fields that have not been completed.
    private void checkAndHighlightEmptyFields() {
        setupFieldListeners();
        TextField[] fields = { firstNameField, lastNameField, addressField, cityField, zipCodeField, phoneNumberField,
                emailField, usernameField, passwordField };
        for (TextField field : fields) {
            if (field.getText().isEmpty()) {
                field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                new animatefx.animation.Shake(field).play();
            } else {
                field.setStyle(null);
            }
        }
        if (accountType.getValue() == null) {
            accountTypeError.setText("Select a valid account type");
            accountType.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(accountType).play();
        } else {
            accountType.setStyle(null);
        }

        if (!checkZipCode()) {
            zipCodeError.setText("Enter a valid Zip Code");
            zipCodeField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(zipCodeField).play();
        } else {
            zipCodeField.setStyle(null);
        }

        if (!checkPhoneNumber()) {
            phoneError.setText("Enter a valid phone number");
            phoneNumberField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(phoneNumberField).play();
        } else {
            phoneNumberField.setStyle(null);
        }

        if (!checkEmail()) {
            emailError.setText("Enter a valid email address");
            emailError.setStyle("12px");
            emailField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(emailField).play();
        } else {
            emailField.setStyle(null);
        }
    }

    // Displays an alert with the given message.
    private void showInformationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String getFirstName() {
        return firstNameField.getText();
    }

    public String getLastName() {
        return lastNameField.getText();
    }

    public String getAddress() {
        return addressField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPhone() {
        return phoneNumberField.getText();
    }

    public String getAccountType() {
        return accountType.getValue();
    }

    public String getUserName() {
        return usernameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    // Validates phones numbers
    public boolean checkPhoneNumber() {
        boolean isValid = false;
        if (phoneNumberField.getText().matches("^(\\d{3}[- .]?){2}\\d{4}$")) {
            isValid = true;
        } else {
            isValid = false;
        }
        return isValid;
    }

    // Validates zip codes
    public boolean checkZipCode() {
        boolean isValid = false;
        if (zipCodeField.getText().matches("^\\d{5}(?:[-\\s]\\d{4})?$")) {
            isValid = true;
        } else {
            isValid = false;
        }
        return isValid;
    }

    // Validates emailaddress
    public boolean checkEmail() {
        boolean isValid = false;
        if (emailField.getText().matches(
                "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$")) {
            isValid = true;
        } else {
            isValid = false;
        }
        return isValid;
    }

    private boolean passwordValid() {

        boolean isValid = false;
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String userName = usernameField.getText();
        String password = passwordField.getText();

        if (password.contains(firstName) || password.contains(lastName) || password.contains(userName)) {
            showInformationAlert("Password cannot contain any personal information.");
            isValid = false;
        } else {
            isValid = true;
        }
        return isValid;
    }

    private double calculatePasswordStrength(String password) {
        double score = 0;

        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()\\-+].*");

        if (hasUppercase)
            score += 1;
        if (hasLowercase)
            score += 1;
        if (hasDigit)
            score += 1;
        if (hasSpecialChar)
            score += 1;

        if (password.length() >= 10)
            score += 1;

        score = score / 5.0;

        return score;
    }

    private void updateProgressBar(double strength) {
        passwordStrengthBar.getStyleClass().removeAll("bar-weak", "bar-medium", "bar-strong");

        if (strength < 0.3) {
            passwordStrengthBar.getStyleClass().add("bar-weak");
            passwordStrengthLabel.setText("Weak");
        } else if (strength < 0.7) {
            passwordStrengthBar.getStyleClass().add("bar-medium");
            passwordStrengthLabel.setText("Medium");
        } else {
            passwordStrengthBar.getStyleClass().add("bar-strong");
            passwordStrengthLabel.setText("Strong");
        }
        passwordStrengthBar.setProgress(strength);
    }
}