package util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileDialogUtil {

    @Nullable
    public static File openFile(Window parent, String title, FileExtFilter... fileExtFilters) {
        JFileChooser openDialog = new JFileChooser();
        openDialog.setDialogTitle(title);
        openDialog.setDialogType(JFileChooser.OPEN_DIALOG);
        openDialog.setAcceptAllFileFilterUsed(false);
        for (FileExtFilter fileExtFilter : fileExtFilters)
            openDialog.addChoosableFileFilter(fileExtFilter);

        openDialog.setVisible(true);
        if (openDialog.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = openDialog.getSelectedFile();
            if (file.exists()) {
                return file;
            } else {
                JOptionPane.showMessageDialog(
                        parent,
                        "File \"" + file.getAbsolutePath() + file + "\" could not be found!", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        return null;
    }

    @Nullable
    public static String saveFile(Window parent, String title, FileExtFilter... fileExtFilters) {
        JFileChooser saveDialog = new JFileChooser();
        saveDialog.setDialogTitle(title);
        saveDialog.setDialogType(JFileChooser.SAVE_DIALOG);
        saveDialog.setAcceptAllFileFilterUsed(false);
        for (FileExtFilter fileExtFilter : fileExtFilters)
            saveDialog.addChoosableFileFilter(fileExtFilter);

        saveDialog.setVisible(true);
        if (saveDialog.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
            return saveDialog.getSelectedFile().getAbsolutePath();
        return null;
    }

    @Nullable
    public static File openFileJFXInSwing(Window parent, String title, FileChooser.ExtensionFilter... fileExtFilters) {
        File file = launchSyncJFXFileChooser(true, parent, title, fileExtFilters);
        if (file != null) {
            if (file.exists()) {
                return file;
            } else {
                JOptionPane.showMessageDialog(
                        parent,
                        "File \"" + file.getAbsolutePath() + file + "\" could not be found!", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return null;
            }
        }
        return null;
    }

    @Nullable
    public static String saveFileJFXInSwing(Window parent, String title, FileChooser.ExtensionFilter... fileExtFilters) {
        File file = launchSyncJFXFileChooser(false, parent, title, fileExtFilters);
        if (file != null) {
            if (file.exists())
                System.out.println("Warning: File \"" + file.getAbsolutePath() + file + "\" already exists, overwriting!");
            return file.getAbsolutePath();
        }
        return null;
    }

    private static File launchSyncJFXFileChooser(boolean open, Window parent, String title, FileChooser.ExtensionFilter... fileExtFilters) {
        SynchronousJFXFileChooser synchronousJFXFileChooser = new SynchronousJFXFileChooser(() -> {
            FileChooser jfxOpenDialog = new FileChooser();
            jfxOpenDialog.setTitle(title);
            jfxOpenDialog.getExtensionFilters().clear();
            jfxOpenDialog.getExtensionFilters().addAll(fileExtFilters);
            return jfxOpenDialog;
        });
        return open ? synchronousJFXFileChooser.showOpenDialog() : synchronousJFXFileChooser.showSaveDialog();
    }

    @Nullable
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

    @Nullable
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
