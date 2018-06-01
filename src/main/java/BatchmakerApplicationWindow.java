import conversion.BatchExporter;
import conversion.ExcelImporter;
import javafx.application.Application;
import javafx.application.Platform;
import model.Trial;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sun.text.resources.iw.FormatData_iw_IL;
import util.FileDialogUtil;
import util.FileExtFilter;
import util.JFXFileExtFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

public class BatchmakerApplicationWindow extends JFrame  {
    public static final String WINDOW_TITLE = "3DSSPP Batchmaker";

    public final Logger logger = Logger.getLogger("BatchmakerApplicationWindow");

    /**
     * Filename of current simulation config
     */
    protected String currentFile;

    public BatchmakerApplicationWindow() {
        super(WINDOW_TITLE);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quitApplication();
            }
        });
        setSize(580, 650);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel(new GridLayout(1,1));
        JButton openButton = new JButton("Convert Excel File.");
        jPanel.add(openButton);
        add(jPanel);

        openButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File file = FileDialogUtil.openFileJFX(BatchmakerApplicationWindow.this, "Open Excel File", JFXFileExtFilter.EXCEL_XLSX);
                    if (file != null) System.out.println(BatchExporter.createBatch(ExcelImporter.openFile(file)));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(BatchmakerApplicationWindow.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        com.sun.javafx.application.PlatformImpl.startup(()->{});
        setVisible(true);
    }


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
}
