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

    public static Trial[] openFile(File file) throws IOException, InvalidFormatException {
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
}
