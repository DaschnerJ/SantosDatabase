/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package santosdatabase.database.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import santosdatabase.database.SanConnection;

/**
 *
 * @author justdasc
 */
public class ExportHandler {

    public boolean done = false;

    Boolean headers = false;
    public static Boolean setLocation = false;
    public static String loc = "";
    public static boolean request = true;
    public static String oneFileName = null;
    public static boolean cancelOverride = false;
    public static boolean doNotSingleOverride = false;
    //Random r = new Random();

    SanConnection sc;
    String location = null;
    String filename = null;
    Boolean newSheet = null;

    String prefix = "";
    String suffix = "";
    
    public static long rows = 0;
    public static long max = 1000000;
    public static int sheet = 0;
    public static int currentSheet = (int) (rows / max);
    public static boolean override = false;
    public static boolean doNotDelete = false;
    public static String outSpot = "";

    public static SXSSFWorkbook swb = null;
    public static ArrayList<Sheet> sheets = new ArrayList<Sheet>();
    
     public static void resetExport() {
        setLocation = false;
        loc = "";
        request = true;
        oneFileName = null;
        cancelOverride = false;
        doNotSingleOverride = false;
        
        rows = 0;
        max = 1000000;
        sheet = 0;
        currentSheet = (int) (rows / max);
        override = false;
        doNotDelete = false;
        outSpot = "";
        swb = null;
        sheets.clear();
         
        SantosWindow.completeLabel.setText("Clearing stored temporary data.");
        System.out.println("Reseting export variables...");
        SantosWindow.completeLabel.setText("Finished");
    }

    public ExportHandler(SanConnection sc) {
        this.prefix = SantosWindow.outputPrefix.getText();
        this.suffix = SantosWindow.outputSuffix.getText();
        this.headers = SantosWindow.outputCreateColumnHeaders.isSelected();

        System.out.println(filename);
        this.sc = sc;

        try {
            runExport(this.sc);
        } catch (SQLException ex) {
            Logger.getLogger(ExportHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        done = true;

    }

    //=============================New Writing Logic============================//
    

   

    private void runExport(SanConnection sc) throws SQLException {
        SantosWindow.completeLabel.setText("Running the export.");
        //String tag = sc.machine.tag;
        ResultSet rs = sc.rs;
        SantosWindow.completeLabel.setText("Getting location...");
        System.out.println("Getting location...");
        if (loc.equals("")) {
            SantosWindow.completeLabel.setText("Location is blank!");
            System.out.println("Location is blank!");
            getLocation();
        } else {
            SantosWindow.completeLabel.setText("Location exists!");
            System.out.println("Location exists!");
            if (!setLocation) {
                SantosWindow.completeLabel.setText("Location is not set, so setting location..");
                System.out.println("Location is not set so setting location...");
                try {
                    getLocation();
                } catch (NullPointerException e) {
                    SantosWindow.completeLabel.setText("Null pointer exception error [01x].");
                    System.out.println("Threw null pointer!");
                }
            }
        }
        SantosWindow.completeLabel.setText("Getting the file name...");
        System.out.println("Getting file name...");
        try {
            if (!WindowHandler.stop) {
                getFileName(sc.machine.tag);
            }
        } catch (NullPointerException e) {
            System.out.println("Invalid file name.");
            WindowHandler.stop = true;
            SantosWindow.completeLabel.setText("Invalid file name.");
        }
        String out;
        if (SantosWindow.outputSeparateWorkbooks.isSelected()) {

            out = loc + "\\" + prefix + this.filename + /*sc.machine.tag +*/ suffix + ".xlsx";
            System.out.println("Out location is: " + out);
        } else {
            out = loc + "\\" + prefix + this.filename + suffix + ".xlsx";
        }
        System.out.println("Output spot for file: " + out);
        if (new File(out).exists() && !doNotDelete) {
            if (!SantosWindow.outputOverwriteExistingFiles.isSelected() && !override) {
                int dialogResult = JOptionPane.showConfirmDialog(null, "Overwrite is not selected, would you like to overwrite the file?", "Warning - Duplicate Files", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    new File(out).delete();
                    if (!SantosWindow.outputSeparateWorkbooks.isSelected()) {
                        doNotDelete = true;
                    }
                    override = true;
                } else {
                    WindowHandler.stop = true;
                }
            } else {
                if (!doNotDelete) {
                    new File(out).delete();
                    if (!SantosWindow.outputSeparateWorkbooks.isSelected()) {
                        doNotDelete = true;
                    }
                }

            }
        }
        if (WindowHandler.stop) {
            return;
        }
        if (SantosWindow.outputSeparateSheets.isSelected()) {

            doNotDelete = true;
            SantosWindow.completeLabel.setText("Running separate sheets workbook.");
            System.out.println("Running separate sheets workbook.");
            //Declare the WB
            SXSSFWorkbook wb = null;
            //Check if the workbook already exists
            if (new File(out).exists()) {
                //If it does, try creating and loading it.
                try {
                    XSSFWorkbook wbb = new XSSFWorkbook(new File(out));
                    wb = new SXSSFWorkbook(wbb, 100);
                } catch (IOException | InvalidFormatException ex) {
                    Logger.getLogger(ExportHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            } //If it doesn't, just create a new one.
            else {
                //Create a new workbook?...
                wb = new SXSSFWorkbook();
                if (swb == null) {
                    swb = wb;
                }
            }
            rows = 0;
            if (sheet != 0) {
                sheet++;
            }
            Sheet sh = null;

            while (rs.next() && !WindowHandler.stop) {
                SantosWindow.completeLabel.setText("Writing row " + (rows % max) + " to memory address.");
                System.out.println("Running on row: " + (rows % max));
                if (rows == 6 || sheet == 6) {
                    SantosWindow.completeLabel.setText("Go bananas!");
                }
                //Create a new sheet...
                if (!SantosWindow.outputSeparateWorkbooks.isSelected()) {
                    wb = swb;
                }
                if (wb == null) {
                    System.out.println("Error is from workbook being null!");
                }
                //If the sheet doesn't exists, create it.
                if ((rows % max) == 0) {
                    if (sh != null) {
                        sheets.add(sh);
                    }
                    System.out.println("New sheet!");
                    sh = wb.createSheet(getUniqueSheetName(wb, sc.machine.tag));
                }
                //System.out.println("Adding headers...");
                if ((rows % max) == 0) {
                    addHeaders(sh, sc);
                }
                //System.out.println("Writing and incrementing the next line...");
                try {
                    if (sh == null) {
                        System.out.println("Error is from sheet being null!");
                    }
                    writeLine(0, (int) (rows % max), sh, sc);
                } catch (IOException ex) {
                    Logger.getLogger(ExportHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                rows++;
            }
            outSpot = out;

            //same wb output....
        } else if (SantosWindow.outputSeparateWorkbooks.isSelected()) {
            SantosWindow.completeLabel.setText("Running for separate workbooks.");
            System.out.println("Running separate workbooks.");
            //Declare the WB
            SXSSFWorkbook wb = null;
            //Check if the workbook already exists
            if (new File(out).exists()) {
                //If it does, try creating and loading it.
                try {
                    XSSFWorkbook wbb = new XSSFWorkbook(new File(out));
                    wb = new SXSSFWorkbook(wbb, 100);
                } catch (IOException | InvalidFormatException ex) {
                    Logger.getLogger(ExportHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            } //If it doesn't, just create a new one.
            else {
                //Create a new workbook?...
                wb = new SXSSFWorkbook();
            }
            rows = 0;
            sheet = 0;
            Sheet sh = null;
            while (rs.next()) {
                SantosWindow.completeLabel.setText("Writing row " + (rows % max) + " to memory address.");
                System.out.println("Running on row: " + (rows % max));
                //Create a new sheet...
                if (rows == 6 || sheet == 6) {
                    SantosWindow.completeLabel.setText("Go bananas!");
                }
                //If the sheet doesn't exists, create it.
                if ((rows % max) == 0) {
                    if (sh != null) {
                        sheets.add(sh);
                    }
                    System.out.println("New sheet!");
                    sh = wb.createSheet(getUniqueSheetName(wb, sc.machine.tag));
                }
                //System.out.println("Adding headers...");
                if ((rows % max) == 0) {
                    addHeaders(sh, sc);
                }
                //System.out.println("Writing and incrementing the next line...");
                try {
                    writeLine(0, (int) (rows % max), sh, sc);
                } catch (IOException ex) {
                    Logger.getLogger(ExportHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                rows++;
            }
            try {
                writeToFile(wb, out);
            } catch (IOException ex) {
                Logger.getLogger(ExportHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } //Same workbook, link pages if possible.
        else {
            doNotDelete = true;
            SantosWindow.completeLabel.setText("Running for combined sheets.");
            System.out.println("Running one sheet workbook.");
            //Declare the WB
            SXSSFWorkbook wb = null;
            //Check if the workbook already exists (This should never really be true since the file is never written.)
            if (new File(out).exists()) {
                //If it does, try creating and loading it.
                try {
                    XSSFWorkbook wbb = new XSSFWorkbook(new File(out));
                    wb = new SXSSFWorkbook(wbb, 100);
                } catch (IOException | InvalidFormatException ex) {
                    Logger.getLogger(ExportHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            } //If it doesn't, just create a new one.
            else {
                //Create a new workbook?...
                wb = new SXSSFWorkbook();
                if (swb == null) {
                    swb = wb;
                }
            }
            Sheet sh = null;
            if (!sheets.isEmpty()) {
                sh = sheets.get(sheets.size() - 1);
                sheets.remove(sh);
            }
            while (rs.next()) {
                SantosWindow.completeLabel.setText("Writing row " + (rows % max) + " to memory address.");
                if (rows == 6 || sheet == 6) {
                    SantosWindow.completeLabel.setText("Go bananas!");
                }
                System.out.println("Running on row: " + (rows % max));
                //Create a new sheet...

                //If the sheet doesn't exists, create it.
                if ((rows % max) == 0) {
                    if (sh != null) {
                        sheets.add(sh);
                    }
                    System.out.println("New sheet!");
                    sh = wb.createSheet(getUniqueSheetName(wb, ""));
                }
                if (wb == null) {
                    System.out.println("Workbook is null!");
                }
                if (sh == null) {
                    sh = wb.createSheet(getUniqueSheetName(wb, ""));
                }
                //System.out.println("Adding headers...");
                if ((rows % max) == 0) {
                    addHeaders(sh, sc);
                }
                //System.out.println("Writing and incrementing the next line...");
                try {
                    if (sh == null) {
                        System.out.println("Sheet is null!");
                    }
                    writeLine(0, (int) (rows % max), sh, sc);
                } catch (IOException ex) {
                    Logger.getLogger(ExportHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                rows++;
            }
            sheets.add(sh);
            outSpot = out;

        }
    }

    private void getFileName(String tag) {
        String name = "";
        if (oneFileName == null) {
            System.out.println("oneFileName is null...");
            System.out.println("Tag is: " + tag);
            if (SantosWindow.outputSeparateWorkbooks.isSelected()) {
                if (SantosWindow.outputFileName.getText().equals("")) {
                    name = JOptionPane.showInputDialog("Please input a file name to save to: ");
                    if (name == null) {
                        SantosWindow.outputLabel.setText("The filename was invalid.");
                        WindowHandler.stop = true;

                        throw new NullPointerException();
                    }
                } else {
                    name = SantosWindow.outputFileName.getText() + tag;
                    if (name == null) {
                        SantosWindow.outputLabel.setText("The filename was invalid.");
                        WindowHandler.stop = true;
                        throw new NullPointerException();
                    }
                }
            } else {
                name = JOptionPane.showInputDialog("Please input a file name to save to: ");
                System.out.println("Ran before null 1.");
                oneFileName = name;
                if (name == null) {
                    SantosWindow.outputLabel.setText("The filename was invalid.");
                    WindowHandler.stop = true;
                    throw new NullPointerException();
                }
            }
        } else {
            System.out.println("OneFileName isn't null.");
            name = oneFileName;
            if (SantosWindow.outputSeparateWorkbooks.isSelected()) {
                name = name + tag;
            }
        }
        name = name + "";
        if (name.equals("")) {
            System.out.println("Name is blank, exiting method.");
            //JOptionPane.showMessageDialog(null, "No file specified.");
            return;
        }
        this.filename = name;
        //Problem?
        if (SantosWindow.outputOneSheet.isSelected() && oneFileName == null) {
            oneFileName = name;
        }
    }

    private void getLocation() {
        if(WindowHandler.stop)
        {
            return;
        }
        try {
            SantosWindow.createFileLocationWindow(this);
        } catch (NullPointerException e) {
            System.out.println("Invalid file.");
            SantosWindow.completeLabel.setText("Invalid file location.");
            WindowHandler.stop = true;
        }

    }

    private void addHeaders(Sheet ws, SanConnection sc) throws SQLException {
        SantosWindow.completeLabel.setText("Adding headers...");
        if (SantosWindow.outputCreateColumnHeaders.isSelected()) {
            String[] v = new String[sc.rs.getMetaData().getColumnCount()];
            for (int i = 0; i < v.length; i++) {
                v[i] = sc.rs.getMetaData().getColumnName(i + 1);
            }
            try {
                writeLine(0, (int) (rows % max), ws, v, sc.machine.tag);
            } catch (IOException ex) {
                Logger.getLogger(ExportHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Increase the row since we don't want to accidentally write this row.
            rows++;
        }
        SantosWindow.completeLabel.setText("Finished adding headers...");
        //else do nothing since we don't want to do anything here.
    }

    private String getUniqueSheetName(SXSSFWorkbook wb, String tag) {
        //System.out.println("Getting unique sheet name...");
        String sh;
        if (!tag.equals("") && !SantosWindow.outputOneSheet.isSelected()) {
            sh = prefix + tag + suffix;
        } else {
            sh = prefix + "Sheet" + suffix;
        }
        int sheetNum = 0;
        if (wb.getSheet(sh) != null || listHasSheet(sh + sheetNum)) {
            while (wb.getSheet(sh + sheetNum) != null || listHasSheet(sh + sheetNum)) {
                sheetNum++;
            }
            sh = sh + sheetNum;
        }

        return sh;
    }

    private boolean listHasSheet(String sheetName) {
        if (!sheets.isEmpty()) {
            for (int i = 0; i < sheets.size(); i++) {
                System.out.println("Has sheet called: " + sheets.get(i).getSheetName());
                if (sheets.get(i).getSheetName().equals("sheetName")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void writeToFile(SXSSFWorkbook wb, String path) throws IOException {
        SantosWindow.completeLabel.setText("Writing out the file. This can take a few seconds even after this process!");
        File f = new File(path);
        //System.out.println("Path writing to is:" + path);
        if (!f.exists()) {
            f.createNewFile();
        }
        if (path == null) {
            System.out.println("Path is NULL!");
        }
        if (wb == null) {
            System.out.println("wb is null!");
        }
        FileOutputStream out = new FileOutputStream(path);
        try {
            wb.write(out);
        } catch (IOException e) {
            System.out.println("Failed to access file. Is the file already in use?");
        }
        out.close();

        // dispose of temporary files backing this workbook on disk
        wb.dispose();
        SantosWindow.completeLabel.setText("Finished.");
    }

    private static void writeLine(int colStart, int cellRow, Sheet ws, SanConnection sc) throws IOException, SQLException {
        String[] v = new String[sc.rs.getMetaData().getColumnCount()];
        //System.out.println("Column count is " + sc.rs.getMetaData().getColumnCount());
        String tag = sc.machine.tag;
        for (int i = 0; i < v.length; i++) {
            v[i] = sc.rs.getString(i + 1);
        }
        //System.out.println("Writing value: " + v[1]);
        writeLine(colStart, cellRow, ws, v, tag);
    }

    private static void writeLine(int colStart, int cellRow, Sheet ws, String[] v, String tag) throws IOException {

        String[] both;
        if (SantosWindow.outputOneSheet.isSelected()) {
            both = new String[v.length + 1];
            System.arraycopy(v, 0, both, 1, v.length);
            if (rows == 0 && SantosWindow.outputCreateColumnHeaders.isSelected()) {
                both[0] = "Name";
            } else {
                both[0] = tag;
            }
        } else {
            both = v;
        }
        Row row = ws.createRow(cellRow);
        //System.out.println("Column count for both: " + both.length);
        for (int col = 0; col < both.length; col++) {
            //System.out.println("Column: " + col);
            Cell cell = row.createCell(col + colStart);
            //System.out.println("Both value: " + both[col]);
            cell.setCellValue(both[col]);
        }

    }

}
