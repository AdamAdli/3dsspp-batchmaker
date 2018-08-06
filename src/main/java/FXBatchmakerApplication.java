import conversion.BatchExporter;
import conversion.ExcelImporter;
import conversion.ExcelTemplateExporter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Trial;
import ui.LoadingJFXButton;
import util.FileDialogUtil;
import util.JFXFileExtFilter;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

public class FXBatchmakerApplication extends Application implements ExcelImporter.FailCallback, BatchExporter.FailCallback {

    public static final String WINDOW_TITLE = "3DSSPP BatchMaker";

    public final Logger logger = Logger.getLogger("BatchmakerApplicationBase");

    public static Image icon = new Image(FXBatchmakerApplication.class.getClassLoader().getResourceAsStream("twotone_build_black_18dp.png"));

    @Override
    public void start(Stage primaryStage) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(FXBatchmakerApplication::showError);
        primaryStage.setTitle(WINDOW_TITLE);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("FXBatchmakerApplication.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(icon);
        primaryStage.show();
    }

    private static void showError(Thread t, Throwable e) {
        System.err.println("***Default exception handler***");
        if (Platform.isFxApplicationThread()) {
            showErrorDialog(e);
        } else {
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
        step2Label.setDisable(true);
    }

    @FXML private void onAboutClick() {
        Alert alert = new Alert(Alert.AlertType.NONE, "Version 0.1.2 developed by Adam Adli (adam@adli.ca).", ButtonType.OK);
        alert.setTitle("About");
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(icon);
        alert.showAndWait();
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
        batchStatusLabel.setText(StatusNoBatchSaved);
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
                (new Thread(ExcelImporter.openFile(file, trials -> {
                    Platform.runLater(() -> {
                        loadedExcelTrials = trials;
                        onLoadedExcel(file);
                    });
                }, this))).start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showError(ex.getMessage());
        }
    }

    @FXML private void onBatchButtonClick()
    {
        try {
            ExportBatchDialog exportBatchDialog = new ExportBatchDialog();
            exportBatchDialog.openWindow();
            if (exportBatchDialog.analystName != null && exportBatchDialog.companyName != null) {
                String filePath = FileDialogUtil.saveFileJFX(loadExcelBtn.getScene().getWindow(), "Save Batch File", JFXFileExtFilter.BATCH_TXT);
                if (filePath != null) {
                    onSavingBatch(filePath);
                    (new Thread(BatchExporter.saveToBatchFile(filePath, loadedExcelTrials, exportBatchDialog.analystName, exportBatchDialog.companyName, batch -> {
                        Platform.runLater(() -> {
                            logInfo(batch);
                            onSavedBatch(filePath);
                        });
                    }, this))).start();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showError(ex.getMessage());
        }
    }

    @Override
    public void onExcelFailed(String message) {
        Platform.runLater(() -> {
            loadedExcelTrials = null;
            loadExcelBtn.hideProgress();
            initialize();
            showError(message);
        });
    }

    @Override
    public void onBatchFailed(String message) {
        Platform.runLater(() -> {
            saveBatchBtn.hideProgress();
            batchStatusLabel.setText(StatusNoBatchSaved);
            step3Label.setDisable(true);
            showError(message);
        });
    }

    @FXML private void onExportTemplate() {
        try {
            String filePath = FileDialogUtil.saveFileJFX(loadExcelBtn.getScene().getWindow(), "Save Excel Template", JFXFileExtFilter.EXCEL_XLSX);
            if (filePath != null) {
                (new Thread(ExcelTemplateExporter.exportTemplate(filePath, savedPath -> showMessage("Saved excel template at " + filePath),this::showError))).start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    public void showMessage(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
            alert.setTitle("Message");
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(icon);
            alert.showAndWait();
        });
    }

    public void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.setTitle("Error");
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(icon);
            alert.showAndWait();
        });
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
