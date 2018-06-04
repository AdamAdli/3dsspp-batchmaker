import static javafx.application.Application.launch;

public class BatchmakerApplicationRunner {

    public static void main(String[] args) {
        if (args.length > 2) {
            System.out.println("USAGE: batchmaker EXCEL_FILE_PATH BATCH_DIRECTORY");
        }
        launch(FXBatchmakerApplication.class, args);
    }
}
