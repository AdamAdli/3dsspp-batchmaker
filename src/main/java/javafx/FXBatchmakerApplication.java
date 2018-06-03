package javafx;

import conversion.BatchExporter;
import conversion.ExcelImporter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Trial;
import ui.LoadingJFXButton;
import util.FileDialogUtil;
import util.JFXFileExtFilter;

import javax.swing.*;
import java.io.File;
import java.lang.management.PlatformManagedObject;
import java.util.logging.Logger;

public class FXBatchmakerApplication extends Application {

    public static final String WINDOW_TITLE = "3DSSPP Batchmaker";

    public final Logger logger = Logger.getLogger("BatchmakerApplicationBase");

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(WINDOW_TITLE);
        /*GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label stepOne = new Label("1. Load an excel file:");
        grid.add(stepOne, 0, 1);

        LoadingJFXButton btn = new LoadingJFXButton("Load Excel File");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);

        btn.setOnAction(e ->{
            btn.showProgress();
        });

        grid.add(hbBtn, 1, 1);*/
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("FXBatchmakerApplication.fxml"));
        Scene scene = new Scene(root, 400, 275);

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    private Trial[] loadedExcelTrials;

    @FXML private LoadingJFXButton loadExcelBtn;
    @FXML private LoadingJFXButton saveBatchBtn;

    @FXML private void initialize()
    {

    }

    void onLoadingExcel(File file) {
        loadExcelBtn.showProgress();
    }

    void onLoadedExcel(String name) {
        loadExcelBtn.hideProgress();
        saveBatchBtn.setDisable(false);
    }

    protected void onSavingBatch(String file) {
        saveBatchBtn.showProgress();
        //batchStatusLabel.setText(StatusSavingBatchFile(file));
    }

    protected void onSavedBatch(String name) {
        saveBatchBtn.hideProgress();
        //batchStatusLabel.setText(StatusBatchFileSaved(name));
    }

    @FXML private void onExcelButtonClick()
    {
        try {
            File file = FileDialogUtil.openFileJFX(loadExcelBtn.getScene().getWindow(), "Open Excel File", JFXFileExtFilter.EXCEL_XLSX);
            if (file != null) {
                onLoadingExcel(file);
                (new Thread(ExcelImporter.openFile2(file, trials -> {
                    Platform.runLater(() -> {
                        loadedExcelTrials = trials;
                        onLoadedExcel(file.getName());
                    });
                }))).start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML private void onBatchButtonClick()
    {
        try {
            String filePath = FileDialogUtil.saveFileJFX(loadExcelBtn.getScene().getWindow(), "Save Batch File", JFXFileExtFilter.BATCH_TXT);
            if (filePath != null) {
                onSavingBatch(filePath);
                (new Thread(BatchExporter.saveToBatchFile(filePath, loadedExcelTrials, batch -> {
                    Platform.runLater(() -> {
                        logInfo(batch);
                        onSavedBatch("");
                    });
                }))).start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    public void quitApplication() {
        Platform.exit();
        System.exit(0);
    }

    public void logInfo(String string) {
        logger.info(string);
    }

    public void logWarn(String string) {
        logger.warning(string);
    }

    public void logErr(String string) {
        logger.severe(string);
    }

}
