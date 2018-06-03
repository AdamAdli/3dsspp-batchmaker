import com.sun.scenario.effect.impl.sw.java.JSWBlend_COLOR_BURNPeer;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import ui.LoadingJButton;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class BatchmakerApplication extends BatchmakerApplicationBase {

    LoadingJButton loadExcelButton;
    LoadingJButton saveBatchButton;
    JLabel excelStatusLabel;
    JLabel batchStatusLabel;

    JMenuItem saveBatchMenuItem;

    public BatchmakerApplication() {
        super();
    }

    @Override
    protected void initializeComponents() {
        setSize(580, 650);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        JPanel borderPanel = new JPanel(new BorderLayout());
        borderPanel .setBorder(BorderFactory.createEmptyBorder(16,16,16,16));

        JPanel rootPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 8, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        rootPanel.add(new JLabel("1. Load an excel file: "), gbc);
        gbc.gridx = 1;
        rootPanel.add(loadExcelButton = new LoadingJButton(loadExcelAction), gbc);
        gbc.insets = new Insets(4, 0, 8, 0);
        gbc.gridy += 1;
        gbc.gridx = 0;
        rootPanel.add(excelStatusLabel = new JLabel(StatusNoExcelLoaded), gbc);
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.gridy += 1;
        gbc.gridx = 0;
        rootPanel.add(new JSeparator());
        gbc.gridy += 1;
        gbc.gridx = 0;
        rootPanel.add(new JLabel("2. Export a 3DSSPP batch file: "), gbc);
        gbc.gridx = 1;
        rootPanel.add(saveBatchButton = new LoadingJButton(saveBatchAction), gbc);
        gbc.insets = new Insets(4, 0, 8, 0);
        gbc.gridy += 1;
        gbc.gridx = 0;
        rootPanel.add(batchStatusLabel = new JLabel(StatusNoBatchSaved), gbc);
        gbc.insets = new Insets(8, 0, 0, 0);
        gbc.gridy += 1;
        gbc.gridx = 0;
        rootPanel.add(new JLabel("3. Manually run your batch file in 3DSSPP."), gbc);

        borderPanel.add(rootPanel);
        add(borderPanel);

        batchStatusLabel.setEnabled(false);
        saveBatchButton.setEnabled(false);
        setJMenuBar(makeMenuBar());
    }

    private JMenuBar makeMenuBar() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem(loadExcelAction));
        fileMenu.add(saveBatchMenuItem = new JMenuItem(saveBatchAction));
        saveBatchMenuItem.setEnabled(false);

        JMenu helpMenu = new JMenu("Help");

        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.add(fileMenu);
        jMenuBar.add(helpMenu);
        return jMenuBar;
    }

    @Override
    protected void onLoadingExcel(File file) {
        loadExcelButton.showProgress();
        excelStatusLabel.setText(StatusLoadingExcelFile(file.getName()));
    }

    @Override
    protected void onSavingBatch(String file) {
        saveBatchButton.showProgress();
        batchStatusLabel.setText(StatusSavingBatchFile(file));
    }

    @Override
    protected void onLoadedExcel(String name) {
        loadExcelButton.hideProgress();
        excelStatusLabel.setText(StatusExcelLoaded(name));
        saveBatchButton.setEnabled(true);
        batchStatusLabel.setEnabled(true);
    }

    @Override
    protected void onSavedBatch(String name) {
        saveBatchButton.hideProgress();
        batchStatusLabel.setText(StatusBatchFileSaved(name));
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
