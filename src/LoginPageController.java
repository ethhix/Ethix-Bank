import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

public class LoginPageController {

    private Stage stage;
    private Parent root;
    private Scene scene;

    @FXML
    private TextField accountNumberField;

    @FXML
    private Hyperlink createAccountLink;

    @FXML
    private TextField passwordField;

    @FXML
    private CheckBox rememberAccountBox;

    public void switchCreateAccount(ActionEvent event) {
    		
        try {
            Parent root = FXMLLoader.load(getClass().getResource("createAccountPage.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setTitle("Create Account");
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}
