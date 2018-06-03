package conversion;

import model.Trial;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExcelImporter {

    public static Runnable openFile2(File file, ExcelLoadedCallback loadedCallback)  {
        return new Runnable() {
            @Override
            public void run() {
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
                    // TODO loadedCallback.onError(ex.getMessage());
                }
            }
        };
    }

    public static Trial[] openFile(File file, ExcelLoadedCallback loadedCallback) throws IOException, InvalidFormatException {
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
            return trials.toArray(new Trial[trials.size()]);
        }
        return null;
    }

    public interface ExcelLoadedCallback {
        void onLoaded(Trial[] trials);
    }
}
