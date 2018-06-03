package javafx;

import conversion.BatchExporter;
import conversion.ExcelImporter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Trial;
import ui.LoadingJFXButton;
import util.FileDialogUtil;
import util.JFXFileExtFilter;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.PlatformManagedObject;
import java.util.logging.Logger;

public class FXBatchmakerApplication extends Application {

    public static final String WINDOW_TITLE = "3DSSPP BatchMaker";

    public final Logger logger = Logger.getLogger("BatchmakerApplicationBase");

    @Override
    public void start(Stage primaryStage) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(FXBatchmakerApplication::showError);
        primaryStage.setTitle(WINDOW_TITLE);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("FXBatchmakerApplication.fxml"));
        Scene scene = new Scene(root, 369, 228);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    private static void showError(Thread t, Throwable e) {
        System.err.println("***Default exception handler***");
        if (Platform.isFxApplicationThread()) {
            showErrorDialog(e);
        } else {
            //System.err.println(e.getStackTrace());
            e.printStackTrace();
            System.err.println("An unexpected error occurred in "+t);
        }
    }

    private static void showErrorDialog(Throwable e) {
        StringWriter errorMsg = new StringWriter();
        e.printStackTrace(new PrintWriter(errorMsg));
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        new Alert(Alert.AlertType.ERROR, errorMsg.toString(), ButtonType.OK).showAndWait();
    }

    private Trial[] loadedExcelTrials;

    @FXML private LoadingJFXButton loadExcelBtn;
    @FXML private LoadingJFXButton saveBatchBtn;
    @FXML private Label excelStatusLabel;
    @FXML private Label batchStatusLabel;
    @FXML private Label step2Label;
    @FXML private Label step3Label;

    @FXML private MenuItem saveBatchMenuItem;

    @FXML private void initialize() {
        excelStatusLabel.setText(StatusNoExcelLoaded);
        batchStatusLabel.setText(StatusNoBatchSaved);
        saveBatchBtn.setDisable(true);
        batchStatusLabel.setDisable(true);
        saveBatchBtn.setDisable(true);
    }

    @FXML private void onAboutClick() {
        new Alert(Alert.AlertType.NONE, "Version 0.1", ButtonType.OK).showAndWait();
    }

    void onLoadingExcel(File file) {
        loadExcelBtn.showProgress();
        excelStatusLabel.setText(StatusLoadingExcelFile(file.getPath()));
    }

    void onLoadedExcel(File file) {
        loadExcelBtn.hideProgress();
        excelStatusLabel.setText(StatusExcelLoaded(file.getPath()));
        saveBatchBtn.setDisable(false);
        batchStatusLabel.setDisable(false);
        step2Label.setDisable(false);
        saveBatchMenuItem.setDisable(false);
        step3Label.setDisable(true);
    }

    protected void onSavingBatch(String fileName) {
        saveBatchBtn.showProgress();
        batchStatusLabel.setText(StatusSavingBatchFile(fileName));
    }

    protected void onSavedBatch(String fileName) {
        saveBatchBtn.hideProgress();
        batchStatusLabel.setText(StatusBatchFileSaved(fileName));
        step3Label.setDisable(false);
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
                        onLoadedExcel(file);
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
                        onSavedBatch(filePath);
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

    private static String StatusNoExcelLoaded = "Status: No Excel file loaded.";
    private static String StatusLoadingExcelFile(String fileName) {
        return "Status: Loading Excel file " + fileName + "...";
    }
    private static String StatusExcelLoaded(String fileName) {
        return "Status: Loaded Excel file " + fileName;
    }

    private static String StatusNoBatchSaved = "Status: Convert and save to a Batch file!";
    private static String StatusSavingBatchFile(String fileName) {
        return "Status: Saving Batch file " + fileName + "...";
    }
    private static String StatusBatchFileSaved(String fileName) {
        return "Status: Saved Batch file " + fileName;
    }
}
