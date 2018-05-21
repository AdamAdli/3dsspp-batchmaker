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

    List<Trial> openFile(File file) throws IOException, InvalidFormatException {
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        if (sheet != null) {
            ArrayList<Trial> trials = new ArrayList<>();
            int rowCount = sheet.getLastRowNum() + 1;
            for (int i = 1; i < rowCount; i++) {
                XSSFRow row = sheet.getRow(i);
                int colCount = row.getLastCellNum();
                Trial trial = new Trial();
                for (int j = 0; j < colCount; j++) {
                    XSSFCell cell = row.getCell(j);
                    trial.values[j] = (float) cell.getNumericCellValue();
                }
                trials.add(trial);
                System.out.println(Arrays.toString(trial.values));
            }
            return trials;
        }
        return null;
    }
}
