import javax.swing.*;

public class BatchmakerApplicationRunner {

    public static void main(String[] args) {
        if (args.length > 2) {
            System.out.println("USAGE: batchmaker EXCEL_FILE_PATH BATCH_DIRECTORY");
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        BatchmakerApplication window = new BatchmakerApplication();
    }
}
