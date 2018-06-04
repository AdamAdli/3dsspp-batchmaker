package conversion;

import model.Trial;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.ArrayList;

public class ExcelImporter {

    public static Runnable openFile(File file, ExcelLoadedCallback loadedCallback, FailCallback failCallback)  {
        return () -> {
                try {
                    XSSFWorkbook workbook = new XSSFWorkbook(file);
                    XSSFSheet sheet = workbook.getSheetAt(0);
                    if (sheet != null) {
                        int rowCount = sheet.getLastRowNum() + 1;
                        ArrayList<Trial> trials = new ArrayList<>();
                        for (int i = 1; i < rowCount; i++) {
                            XSSFRow row = sheet.getRow(i);
                            if (row == null || row.getCell(0) == null || row.getCell(0).getRawValue().isEmpty()) break;
                            else trials.add(Trial.fromRow(row));
                        }
                        loadedCallback.onLoaded(trials.toArray(new Trial[trials.size()]));
                    }
                } catch (Exception ex) {
                    failCallback.onExcelFailed(ex.getMessage());
                }
            };
    }

    public interface FailCallback {
        void onExcelFailed(String message);
    }

    public interface ExcelLoadedCallback {
        void onLoaded(Trial[] trials);
    }

    public static class MalformedExcelException extends Exception {
        private String msg;
        private int rowNumber;
        private int cellNumber;

        public MalformedExcelException(String msg) {
            this(msg, -1, -1);
        }

        public MalformedExcelException(String msg, int rowNumber, int cellNumber) {
            super(msg);
        }
    }
}
