import conversion.BatchExporter;
import conversion.ExcelImporter;
import javafx.application.Platform;
import model.Trial;
import util.FileDialogUtil;
import util.JFXFileExtFilter;
import util.SimpleAction;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.logging.Logger;

public abstract class BatchmakerApplicationBase extends JFrame  {
    public static final String WINDOW_TITLE = "3DSSPP Batchmaker";

    public final Logger logger = Logger.getLogger("BatchmakerApplicationBase");

    private Trial[] loadedExcelTrials;

    public BatchmakerApplicationBase() {
        super(WINDOW_TITLE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quitApplication();
            }
        });
        initializeComponents();
        com.sun.javafx.application.PlatformImpl.startup(()->{});
        setVisible(true);
        pack();
        setMinimumSize(getSize());
        setResizable(false);
    }

    abstract protected void initializeComponents();

    abstract protected void onLoadingExcel(File file);
    abstract protected void onLoadedExcel(String name);

    abstract protected void onSavingBatch(String file);
    abstract protected void onSavedBatch(String name);

    public void quitApplication() {
        Platform.exit();
        getContentPane().removeAll();
        dispose();
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

    protected Action loadExcelAction = new SimpleAction("Load Excel File", e -> {
        try {
            File file = FileDialogUtil.openFileJFXInSwing(BatchmakerApplicationBase.this, "Open Excel File", JFXFileExtFilter.EXCEL_XLSX);
            if (file != null) {
                onLoadingExcel(file);
                (new Thread(ExcelImporter.openFile2(file, trials -> {
                    loadedExcelTrials = trials;
                    onLoadedExcel(file.getName());
                }))).start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(BatchmakerApplicationBase.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    protected Action saveBatchAction = new SimpleAction("Save Batch File", e -> {
        try {
            String filePath = FileDialogUtil.saveFileJFXInSwing(BatchmakerApplicationBase.this, "Save Batch File", JFXFileExtFilter.BATCH_TXT);
            if (filePath != null) {
                onSavingBatch(filePath);
                (new Thread(BatchExporter.saveToBatchFile(filePath, loadedExcelTrials, batch -> {
                    logInfo(batch);
                    onSavedBatch("");
                }))).start();
            }
            // TODO if (file != null) System.out.println(BatchExporter.createBatch(ExcelImporter.openFile(file)));
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(BatchmakerApplicationBase.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
}
