import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ExportBatchDialog {
    public static String analystName = "";
    public static String companyName = "";

    private Stage popup;

    public String openWindow() throws IOException {
        analystName = "";
        companyName = "";
        popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Save Batch File");
        popup.getIcons().add(FXBatchmakerApplication.icon);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ExportBatchDialog.fxml"));
        Scene scene = new Scene(root);
        popup.setScene(scene);
        popup.setOnCloseRequest(event -> {
            analystName="";
            companyName="";
        });
        popup.showAndWait();
        return analystName;
    }

    @FXML private TextField analystTextField;
    @FXML private TextField companyTextField;

    @FXML private void cancel() {
        analystName=null;
        companyName=null;
        ((Stage)companyTextField.getScene().getWindow()).close();
    }

    @FXML private void save() {
        analystName = analystTextField.getText();
        companyName = companyTextField.getText();
        ((Stage)companyTextField.getScene().getWindow()).close();
    }
}
