package util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;

public class FileDialogUtil {

    public static File openFileJFX(javafx.stage.Window parent, String title, FileChooser.ExtensionFilter... fileExtFilters) {
        File file = launchJFXFileChooser(true, parent, title, fileExtFilters);
        if (file != null) {
            if (file.exists()) {
                return file;
            } else {
                new Alert(Alert.AlertType.ERROR, "File \"" + file.getAbsolutePath() + file + "\" could not be found!", ButtonType.OK).showAndWait();
                return null;
            }
        }
        return null;
    }

    public static String saveFileJFX(javafx.stage.Window parent, String title, FileChooser.ExtensionFilter... fileExtFilters) {
        File file = launchJFXFileChooser(false, parent, title, fileExtFilters);
        if (file != null) {
            // if (file.exists()) new Alert(Alert.AlertType.WARNING, "File \"" + file.getAbsolutePath() + file + "\" already exists, overwriting!", ButtonType.OK).showAndWait();
            return file.getAbsolutePath();
        }
        return null;
    }

    private static File launchJFXFileChooser(boolean open, javafx.stage.Window parent, String title, FileChooser.ExtensionFilter... fileExtFilters) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(fileExtFilters);
        return open ? fileChooser.showOpenDialog(parent) : fileChooser.showSaveDialog(parent);
    }
}
