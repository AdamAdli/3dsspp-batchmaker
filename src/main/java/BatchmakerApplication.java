public class BatchmakerApplication {

    public static void main(String[] args) {
        if (args.length > 2) {
            System.out.println("USAGE: batchmaker EXCEL_FILE_PATH BATCH_DIRECTORY");
        }

        BatchmakerApplicationWindow window = new BatchmakerApplicationWindow();
    }
}
