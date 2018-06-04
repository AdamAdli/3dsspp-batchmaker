package conversion;

import model.Trial;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExcelTemplateExporter {
    public static Runnable exportTemplate(String filePath, ExcelSavedCallback excelSavedCallback, FailCallback failCallback) {
        return () -> {
            try (InputStream is = ExcelTemplateExporter.class.getClassLoader().getResourceAsStream("Template.xlsx")) {
                Files.copy(is, Paths.get(filePath));
                excelSavedCallback.onSaved(filePath);
            } catch (IOException ex) {
                failCallback.onFailed(ex.getMessage());
            }
        };
    }

    public interface FailCallback {
        void onFailed(String message);
    }

    public interface ExcelSavedCallback {
        void onSaved(String filePath);
    }
}
