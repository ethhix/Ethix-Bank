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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class createAccountPageController implements Initializable {

    private Stage stage;
    private Parent root;
    private Scene scene;
    private String name;
    private String address;
    private String email;
    private String phone;

    @FXML
    private ChoiceBox<String> accountType;

    @FXML
    private TextField firstNameField, lastNameField, addressField, cityField, zipCodeField, phoneNumberField, emailField;

    @FXML
    private Button createAccountButton;

    @FXML
    private Button cancelCreateAccount;

    @FXML
    private CheckBox termsCheckbox;

    private String[] accountTypes = {"Checking", "Savings"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountType.getItems().addAll(accountTypes);
    }

    // Allows for switching to the login page scene
    public void switchToLoginPage(ActionEvent event) {
    		
        try {
            Parent root = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    // This method is responsible for validing the user's input and creating a new account.
    public boolean handleCreateAccount(ActionEvent event) {
        boolean isAccountCreated = false;
        Customer customer = new Customer(this);
        BankAccount bankAccount = new BankAccount(this);
        if (!termsCheckbox.isSelected() && !customer.isValid()) {
            showInformationAlert("Please agree to our terms and conditions to continue.");
            checkAndHighlightEmptyFields();
            isAccountCreated = false;
        } else if (!customer.isValid()) {
            checkAndHighlightEmptyFields();
            isAccountCreated = false;
        } if (checkPhoneNumber() == false) {
            showInformationAlert("Please enter a valid phone number.");
        } if (checkZipCode() == false) {
            showInformationAlert("Please enter a valid zip code.");
        } else if (checkPhoneNumber() == false && checkZipCode() == false) {
            showInformationAlert("Please enter a valid phone number and zip code.");
        } else {
            BankDatabaseConnector.insertCustomer(customer);
            BankDatabaseConnector.insertBankAccount(bankAccount);
            showInformationAlert("Account created successfully!");
            isAccountCreated = true;
            switchToLoginPage(event);
        }
        return isAccountCreated;
    }
    
    // Checks and highlights fields that have not been completed.
    private void checkAndHighlightEmptyFields() {
        TextField[] fields = {firstNameField, lastNameField, addressField, cityField, zipCodeField, phoneNumberField, emailField};
        for (TextField field : fields) {
            if (field.getText().isEmpty()) {
                field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                new animatefx.animation.Shake(field).play();
            } else {
                field.setStyle(null);
            }
        }
        if (accountType.getValue() == null) {
            accountType.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(accountType).play();
        } else {
            accountType.setStyle(null);
        }
    }
    
    // Displays an alert with the given message.
    private void showInformationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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

    // Validates phones numbers
    public boolean checkPhoneNumber() {
        boolean isValid = false;
        if (phoneNumberField.getText().matches("^(\\d{3}[- .]?){2}\\d{4}$")) {
            isValid = true;
        } else {
            isValid = false;
        } return isValid;
    }
    
    // Validates zip codes
    public boolean checkZipCode() {
        boolean isValid = false;
        if (zipCodeField.getText().matches("^\\d{5}(?:[-\\s]\\d{4})?$")) {
            isValid = true;
        } else {
            isValid = false;
        } return isValid;
    }


}
