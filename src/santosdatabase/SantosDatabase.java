/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package santosdatabase;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import javax.swing.JFrame;
import santosconfig.SantosConfig;
import santosconfig.config.Config;
import santosdatabase.database.config.ConfigHandler;
import santosdatabase.database.gui.OptionsWindow;
import santosdatabase.database.gui.SQLParser;
import santosdatabase.database.gui.SantosWindow;
import santoslogger.gui.Log;

/**
 *
 * @author justdasc
 */
public class SantosDatabase {


    public static Log log = new Log("database_log");
    public static ConfigHandler cf = new ConfigHandler("options");
    public static ConfigHandler data = new ConfigHandler("data");
    public static ConfigHandler def;
    public static Config systemConfig;
    public static SantosConfig cfg = new SantosConfig();
    
    public static OptionsWindow opt;
    
//    private static String driver = "com.ibm.as400.access.AS400JDBCDriver";
//    private static String host = "RICWC01i";
//    private static String user = "JustDasc";
//    private static String pass = "buddy123";
//    private static String query = "SELECT * FROM fsdata.ititmf";
    //private static String database = "B680";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SQLParser test = new SQLParser();
        //new QueryHandler();
        final JFrame mainWindow = new SantosWindow();
        mainWindow.setVisible(true);
        def = new ConfigHandler("defaults");
        ConfigHandler.def = def;
        data.writeDefaultDataOptions();
        cf.writeDefaultOptions();
        cf.loadOptions();
        opt = new OptionsWindow();
        mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainWindow.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent event) {
                System.out.println("Closing!");
                cf.closingOperations();
                mainWindow.setVisible(false);
                mainWindow.dispose();
                System.exit(0);
            }
        });
        log.updateLog(SantosWindow.logField);
        log.logTo(Calendar.getInstance().getTime().toString(), SantosWindow.logField);
        log.logTo(Calendar.getInstance().getTime().toString(), SantosWindow.outputLabel);
        
        //log.showLog();

//        SanConnection sc;
//        sc = new SanConnection(driver, host, user, pass, query, database);
//        long startTime = System.nanoTime();
//        
//        Fetcher f = new Fetcher(sc.rs, 1000);
//        System.out.println(f.getRowSet().size());
//
//        SanExcel se = new SanExcel(sc.rs);
//        se.createWorkbook("Test", "C:\\Users\\justdasc\\Desktop\\Workbooks\\");
//        try {
//            se.writeResultSet("C:\\Users\\justdasc\\Desktop\\Workbooks\\Test.xls");
//        } catch (SQLException ex) {
//            Logger.getLogger(SantosDatabase.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        long endTime = System.nanoTime();
//        long duration = (endTime - startTime);
//        System.out.println("Time in Milliseconds: " + duration/1000000);
//        System.out.println("Done");
//        System.exit(0);
        //log.addFine(String.valueOf(sc.rs.getString("CSCUS")));
    }
    
}
