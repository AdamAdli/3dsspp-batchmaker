package model;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

public class Trial {
    public static final int SEX_M = 0;
    public static final int SEX_F = 1;
    public static final int PERCENTILE_95 = 0;
    public static final int PERCENTILE_50 = 1;
    public static final int PERCENTILE_5 = 2;
    public static final int NO_PERCENTILE = 3;


    public int sex = SEX_M;
    public int percentile = PERCENTILE_50;
    public float height = 0;
    public float weight = 0;

    public int[] segAngles = new int[42];
    public float[] handLoads = new float[6];
    public float[] handCompLeft = new float[6];
    public float[] handCompRight = new float[6];

    public static int[] excelToSegArg = new int[42];
    public static int[] excelToHandloadsArg = new int[6];
    public static int[] excelToHCLArg = new int[6];
    public static int[] excelToHCRArg = new int[6];

    static {
        excelToSegArg[0] =      3;
        excelToSegArg[1] =      4;
        excelToSegArg[2] =      18;
        excelToSegArg[3] =      19;
        excelToSegArg[4] =      5;
        excelToSegArg[5] =      6;
        excelToSegArg[6] =      20;
        excelToSegArg[7] =      21;
        excelToSegArg[8] =      7;
        excelToSegArg[9] =      8;
        excelToSegArg[10] =     22;
        excelToSegArg[11] =     23;
        excelToSegArg[12] =     9;
        excelToSegArg[13] =     10;
        excelToSegArg[14] =     24;
        excelToSegArg[15] =     25;
        excelToSegArg[16] =     11;
        excelToSegArg[17] =     12;
        excelToSegArg[18] =     26;
        excelToSegArg[19] =     27;
        excelToSegArg[20] =     13;
        excelToSegArg[21] =     14;
        excelToSegArg[22] =     28;
        excelToSegArg[23] =     29;
        excelToSegArg[24] =     37;
        excelToSegArg[25] =     38;
        excelToSegArg[26] =     39;
        excelToSegArg[27] =     41;
        excelToSegArg[28] =     40;
        excelToSegArg[29] =     30;
        excelToSegArg[30] =     31;
        excelToSegArg[31] =     32;
        excelToSegArg[32] =     33;
        excelToSegArg[33] =     34;
        excelToSegArg[34] =     35;
        excelToSegArg[35] =     36;
        excelToSegArg[36] =     0;
        excelToSegArg[37] =     1;
        excelToSegArg[38] =     2;
        excelToSegArg[39] =     15;
        excelToSegArg[40] =     16;
        excelToSegArg[41] =     17;

        excelToHandloadsArg[0] = 0;
        excelToHandloadsArg[1] = 1;
        excelToHandloadsArg[2] = 2;
        excelToHandloadsArg[3] = 3;
        excelToHandloadsArg[4] = 4;
        excelToHandloadsArg[5] = 5;

        excelToHCLArg[0] = 0;
        excelToHCLArg[1] = 1;
        excelToHCLArg[2] = 2;
        excelToHCLArg[3] = 3;
        excelToHCLArg[4] = 4;
        excelToHCLArg[5] = 5;

        excelToHCRArg[0] = 0;
        excelToHCRArg[1] = 1;
        excelToHCRArg[2] = 2;
        excelToHCRArg[3] = 3;
        excelToHCRArg[4] = 4;
        excelToHCRArg[5] = 5;
    }

    public static Trial fromRow(XSSFRow row){
        Trial trial = new Trial();
        trial.sex = isCellMale(row.getCell(0)) ? SEX_M : SEX_F;
        trial.percentile = getCellPercentile(row.getCell(1));
        if (trial.percentile == NO_PERCENTILE) {
            trial.height = (float) row.getCell(2).getNumericCellValue();
            trial.weight = (float) row.getCell(3).getNumericCellValue();
        }
        for (int i = 0; i < excelToSegArg.length; i++) {
            trial.segAngles[excelToSegArg[i]] = (int) row.getCell(i + 4).getNumericCellValue();
        }
        for (int i = 0; i < excelToHandloadsArg.length; i++) {
            trial.handLoads[excelToHandloadsArg[i]] = (float) row.getCell(i + 4 + excelToSegArg.length).getNumericCellValue();
        }
        for (int i = 0; i < excelToHCLArg.length; i++) {
            trial.handCompLeft[excelToHCLArg[i]] = (float) row.getCell(i + 4 + excelToSegArg.length + excelToHandloadsArg.length).getNumericCellValue();
        }
        for (int i = 0; i < excelToHCRArg.length; i++) {
            trial.handCompRight[excelToHCRArg[i]] = (float) row.getCell(i + 4 + excelToSegArg.length + excelToHandloadsArg.length + excelToHCLArg.length).getNumericCellValue();
        }
        return trial;
    }

    private static boolean isCellMale(XSSFCell cell) {
        String value = cell.getRawValue();
        return value.trim().toUpperCase().equals("M") || value.trim().equals("0");
    }

    private static int getCellPercentile(XSSFCell cell) {
        int value = (int) cell.getNumericCellValue();
        if (value == 0) return NO_PERCENTILE;
        else if (value == 5) return PERCENTILE_5;
        else if (value == 50) return PERCENTILE_50;
        else if (value == 95) return PERCENTILE_95;
        else return -1;
    }
}
