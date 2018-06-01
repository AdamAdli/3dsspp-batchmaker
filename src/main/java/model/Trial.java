package model;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.util.HashMap;

public class Trial {
    public static final int SEX_M = 0;
    public static final int SEX_F = 1;
    public static final int PERCENTILE_95 = 0;
    public static final int PERCENTILE_50 = 1;
    public static final int PERCENTILE_5 = 2;


    public int sex;
    public int percentile;
    public int[] seg_angles;

    static {
        int[] excelToSegArg = new int[42];
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
        excelToSegArg[30] =     31; //
        excelToSegArg[31] =     32; //
        excelToSegArg[38] =     33; // TODO
        excelToSegArg[39] =     34;
        excelToSegArg[40] =     35;
        excelToSegArg[40] =     36;
        excelToSegArg[32] =     0;
        excelToSegArg[33] =     1;
        excelToSegArg[34] =     2;
        excelToSegArg[35] =     15;
        excelToSegArg[36] =     16;
        excelToSegArg[37] =     17;

        int[] excelToHandloadsArg = new int[6];
        excelToHandloadsArg[0] = 0;
        excelToHandloadsArg[1] = 1;
        excelToHandloadsArg[2] = 2;
        excelToHandloadsArg[3] = 3;
        excelToHandloadsArg[4] = 4;
        excelToHandloadsArg[5] = 5;

        int[] excelToHCLArg = new int[6];
        excelToHandloadsArg[0] = 0;
        excelToHandloadsArg[1] = 1;
        excelToHandloadsArg[2] = 2;
        excelToHandloadsArg[3] = 3;
        excelToHandloadsArg[4] = 4;
        excelToHandloadsArg[5] = 5;

        int[] excelToHCRArg = new int[6];
        excelToHandloadsArg[0] = 0;
        excelToHandloadsArg[1] = 1;
        excelToHandloadsArg[2] = 2;
        excelToHandloadsArg[3] = 3;
        excelToHandloadsArg[4] = 4;
        excelToHandloadsArg[5] = 5;
        /*HashMap<Integer, Integer> excelToSegArgMap = new HashMap<>();
        int offset = 4;
        excelToSegArgMap.put(offset,        3);
        excelToSegArgMap.put(offset + 1,    4);
        excelToSegArgMap.put(offset + 2,    18);
        excelToSegArgMap.put(offset + 3,    19);
        excelToSegArgMap.put(offset + 4,    5);
        excelToSegArgMap.put(offset + 5,    6);
        excelToSegArgMap.put(offset + 6,    20);
        excelToSegArgMap.put(offset + 7,    21);
        excelToSegArgMap.put(offset + 8,    7);
        excelToSegArgMap.put(offset + 9,    8);
        excelToSegArgMap.put(offset + 10,   22);
        excelToSegArgMap.put(offset + 11,   23);
        excelToSegArgMap.put(offset + 12,   9);
        excelToSegArgMap.put(offset + 13,   10);
        excelToSegArgMap.put(offset + 14,   24);
        excelToSegArgMap.put(offset + 15,   25);
        excelToSegArgMap.put(offset + 16,   11);
        excelToSegArgMap.put(offset + 17,   12);
        excelToSegArgMap.put(offset + 18,   26);
        excelToSegArgMap.put(offset + 19,   27);
        excelToSegArgMap.put(offset + 20,   13);
        excelToSegArgMap.put(offset + 21,   14);
        excelToSegArgMap.put(offset + 22,   28);
        excelToSegArgMap.put(offset + 23,   29);
        excelToSegArgMap.put(offset + 24,   37);
        excelToSegArgMap.put(offset + 25,   38);
        excelToSegArgMap.put(offset + 26,   39);
        excelToSegArgMap.put(offset + 27,   41);
        excelToSegArgMap.put(offset + 28,   40);
        excelToSegArgMap.put(offset + 29,   30);
        excelToSegArgMap.put(offset + 30,   31); //
        excelToSegArgMap.put(offset + 31,   32); //
        excelToSegArgMap.put(offset + 38,   33); // TODO
        excelToSegArgMap.put(offset + 39,   34);
        excelToSegArgMap.put(offset + 40,   35);
        excelToSegArgMap.put(offset + 40,   36);
        excelToSegArgMap.put(offset + 32,   0);
        excelToSegArgMap.put(offset + 33,   1);
        excelToSegArgMap.put(offset + 34,   2);
        excelToSegArgMap.put(offset + 35,   15);
        excelToSegArgMap.put(offset + 36,   16);
        excelToSegArgMap.put(offset + 37,   17);*/
    }

    public Trial() {
        seg_angles = new int[42];
    }



    public Trial fromRow(XSSFRow row){
        int colCount = row.getLastCellNum();
        Trial trial = new Trial();
        trial.sex = isCellTrue(row.getCell(0)) ? SEX_M : SEX_F;
        trial.percentile = isCellTrue(row.getCell(2)) ? PERCENTILE_95 :
                isCellTrue(row.getCell(3)) ? PERCENTILE_50 : PERCENTILE_5;
        trial.seg_angles[3] = (int) row.getCell(offset).getNumericCellValue();
        trial.seg_angles[4] = (int) row.getCell(offset + 1).getNumericCellValue();
        trial.seg_angles[3] = (int) row.getCell(offset).getNumericCellValue();
        trial.seg_angles[3] = (int) row.getCell(offset).getNumericCellValue();
        trial.seg_angles[3] = (int) row.getCell(offset).getNumericCellValue();
    }

    private boolean isCellTrue(XSSFCell cell) {

    }

    /*
        EXCEL  -> SEG ANGLE
        5           3
        6           4
        7           1
        8           1
        9           1
        10          1
        11          1
        12           1
        13           1
        14           1
        15          1
        16           1
        17          1
        0           1
        0           1
        0           1
        0           1

      SEG  ->       EXCEL
        5           1
        6           1
        7           1
        8           1
        9           1
        10          1
        11          1
        12           1
        13           1
        14           1
        15          1
        16           1
        17          1
        0           1
        0           1
        0           1
        0           1






     */
}
