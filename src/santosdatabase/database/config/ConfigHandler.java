/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package santosdatabase.database.config;

import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import santosconfig.config.Config;
import static santosdatabase.SantosDatabase.log;
import static santosdatabase.SantosDatabase.opt;
import santosdatabase.database.gui.SanFileChooser;
import santosdatabase.database.gui.SantosWindow;
import santosutils.SantosUtils;
import santosutils.file.FileManager;

/**
 *
 * @author justdasc
 */
public class ConfigHandler {

    public static ConfigHandler def;
    SantosUtils u = new SantosUtils();
    Config cf;

    public ConfigHandler(String name) {
        cf = new Config(name);
    }

    public Config getConfig() {
        return cf;
    }

    private void loadSystem() {
        if (def.cf.loadValue("systemDefault") != null) {
            String path = def.cf.loadValue("systemDefault");
            File f = new File(path + "\\" + "System.sanfig");
            System.out.println("Loading from default system configuration file.");
            System.out.println("The system default file path is: " + path + "\\" + "System.sanfig");
            if (f.exists()) {
                System.out.println("Config System File does exist.");
            }
            if (f.exists() && !f.isDirectory()) {
                System.out.println("Found the file! Loading system defaults.");
                Config sys = new Config("System", path);
                //def.cf.safeWrite("allRadio", sys.safeLoad("allRadio"));
                System.out.println("System-previewRadio: " + sys.safeLoad("previewRadio"));
                def.cf.safeWrite("previewRadio", sys.safeLoad("previewRadio"));
                def.cf.safeWrite("selectDriverIndex", sys.safeLoad("selectDriverIndex"));
                def.cf.safeWrite("lastHost", sys.safeLoad("lastHost"));
                def.cf.safeWrite("lastDatabase", sys.safeLoad("lastDatabase"));
                def.cf.safeWrite("lastUsername", sys.safeLoad("lastUsername"));
                def.cf.safeWrite("lastPassword", sys.safeLoad("lastPassword"));
                def.cf.safeWrite("lastQuery", sys.safeLoad("lastQuery"));
                def.cf.safeWrite("previewField", sys.safeLoad("previewField"));
                def.cf.safeWrite("fileDefaultName", sys.safeLoad("fileDefaultName"));
            }

        }
    }

    public void loadFromDefault() {
        //SantosWindow.selectRadio.setSelected(!Boolean.valueOf(def.cf.loadValue("allRadio")));
        SantosWindow.previewRadio.setSelected(Boolean.valueOf(def.cf.loadValue("previewRadio")));
        SantosWindow.excelRadio.setSelected(!Boolean.valueOf(def.cf.loadValue("previewRadio")));
        SantosWindow.driverBox.setSelectedIndex(Integer.valueOf(def.cf.loadValue("selectDriverIndex")));
        SantosWindow.hostField.setText(def.cf.loadValue("lastHost"));
        SantosWindow.databaseField.setText(def.cf.loadValue("lastDatabase"));
        SantosWindow.userField.setText(def.cf.loadValue("lastUsername"));
        SantosWindow.passField.setText(def.cf.loadValue("lastPassword"));
        SantosWindow.queryField.setText(def.cf.loadValue("lastQuery"));
        SantosWindow.previewField.setText(def.cf.loadValue("previewField"));
//        SanFileChooser.lastPath = def.cf.loadValue("lastPath");
//        SantosWindow.outputFileLocation.setText(def.cf.loadValue("lastPath"));
        SantosWindow.outputFileName.setText(def.cf.loadValue("fileDefaultName"));
        infoBox("Finished refreshing all options to defaults.", "Default Configuration");
    }

    public void loadFromSystemDefault() {
        if (def.cf.loadValue("systemDefault") != null) {
            System.out.println("Key exists, time to get the path...");
            System.out.println("Loaded value is: " + def.cf.loadValue("systemDefault"));
            String path = def.cf.loadValue("systemDefault");
            File f = new File(path + "\\" + "System.sanfig");
            System.out.println("Loading from default system configuration file.");
            System.out.println("The system default file path is: " + path + "\\" + "System.sanfig");
            if (f.exists()) {
                System.out.println("Config System File does exist.");
            } else {
                infoBox("Default system file is missing.", "System Default Configuration");
            }
            if (f.exists() && !f.isDirectory()) {
                System.out.println("Found the file! Loading system defaults.");
                Config sys = new Config("System", path);
                //SantosWindow.selectRadio.setSelected(!Boolean.valueOf(sys.loadValue("allRadio")));
                SantosWindow.previewRadio.setSelected(Boolean.valueOf(sys.loadValue("previewRadio")));
                SantosWindow.excelRadio.setSelected(!Boolean.valueOf(sys.loadValue("previewRadio")));
                SantosWindow.driverBox.setSelectedIndex(Integer.valueOf(sys.loadValue("selectDriverIndex")));
                SantosWindow.hostField.setText(sys.loadValue("lastHost"));
                SantosWindow.databaseField.setText(sys.loadValue("lastDatabase"));
                SantosWindow.userField.setText(sys.loadValue("lastUsername"));
                SantosWindow.passField.setText(sys.loadValue("lastPassword"));
                SantosWindow.queryField.setText(sys.loadValue("lastQuery"));
                SantosWindow.previewField.setText(sys.loadValue("previewField"));
//        SanFileChooser.lastPath = def.cf.loadValue("lastPath");
//        SantosWindow.outputFileLocation.setText(def.cf.loadValue("lastPath"));
                SantosWindow.outputFileName.setText(sys.loadValue("fileDefaultName"));
                infoBox("Finished refreshing all options to system defaults.", "System Default Configuration");
            } else {
                infoBox("Default system file is missing.", "System Default Configuration");
            }
        } else {
            infoBox("There is no default configuration file configured.", "System Default Configuration");
        }

    }

    public void loadOptions() {

        //SantosWindow.allRadio.setSelected(Boolean.valueOf(cf.loadValue("allRadio")));
        //SantosWindow.selectRadio.setSelected(!Boolean.valueOf(cf.loadValue("allRadio")));
        SantosWindow.previewRadio.setSelected(Boolean.valueOf(cf.loadValue("previewRadio")));
        SantosWindow.excelRadio.setSelected(!Boolean.valueOf(cf.loadValue("previewRadio")));
        SantosWindow.driverBox.setSelectedIndex(Integer.valueOf(cf.loadValue("selectDriverIndex")));
        SantosWindow.hostField.setText(cf.loadValue("lastHost"));
        SantosWindow.databaseField.setText(cf.loadValue("lastDatabase"));
        SantosWindow.userField.setText(cf.loadValue("lastUsername"));
        SantosWindow.passField.setText(cf.loadValue("lastPassword"));
        SantosWindow.queryField.setText(cf.loadValue("lastQuery"));
        SantosWindow.previewField.setText(cf.loadValue("previewField"));
        SanFileChooser.lastPath = cf.loadValue("lastPath");
        SantosWindow.outputFileLocation.setText(cf.loadValue("lastPath"));
        SantosWindow.outputFileName.setText(cf.loadValue("fileDefaultName"));

        if (!SantosWindow.outputSeparateWorkbooks.isSelected()) {
            SantosWindow.outputFileName.setEnabled(false);
        }
    }

    public void writeDefaultOptions() {
        loadSystem();
        //def.cf.safeWrite("allRadio", "false");
        def.cf.safeWrite("previewRadio", "true");
        def.cf.safeWrite("selectDriverIndex", "0");
        def.cf.safeWrite("lastHost", "Host");
        def.cf.safeWrite("lastDatabase", "Test");
        def.cf.safeWrite("lastUsername", "Username");
        def.cf.safeWrite("lastPassword", "Password");
        def.cf.safeWrite("lastQuery", "");
        def.cf.safeWrite("previewField", "25");
        def.cf.safeWrite("fileDefaultName", "OPCO-Worbook-");
        def.cf.safeWrite("lastPath", new FileManager().GetExecutionPath());

        //cf.safeWrite("allRadio", def.cf.safeLoad("allRadio"));
        cf.safeWrite("previewRadio", def.cf.safeLoad("previewRadio"));
        cf.safeWrite("selectDriverIndex", def.cf.safeLoad("selectDriverIndex"));
        cf.safeWrite("lastHost", def.cf.safeLoad("lastHost"));
        cf.safeWrite("lastDatabase", def.cf.safeLoad("lastDatabase"));
        cf.safeWrite("lastUsername", def.cf.safeLoad("lastUsername"));
        cf.safeWrite("lastPassword", def.cf.safeLoad("lastPassword"));
        cf.safeWrite("lastQuery", def.cf.safeLoad("lastQuery"));
        cf.safeWrite("previewField", def.cf.safeLoad("previewField"));
        cf.safeWrite("lastPath", def.cf.safeLoad("lastPath"));
        cf.safeWrite("fileDefaultName", def.cf.safeLoad("fileDefaultName"));
    }

    public void writeDefaultDataOptions() {
        def.cf.safeWrite("driverNames", "AS 400");
        def.cf.safeWrite("driverPaths", "com.ibm.as400.access.AS400JDBCDriver");

        cf.safeWrite("driverNames", def.cf.safeLoad("driverNames"));
        cf.safeWrite("driverPaths", def.cf.safeLoad("driverPaths"));
    }

    public void closingOperations() {
        //cf.writeValue("allRadio", String.valueOf(SantosWindow.allRadio.isSelected()));
        cf.writeValue("previewRadio", String.valueOf(SantosWindow.previewRadio.isSelected()));
        cf.writeValue("selectDriverIndex", String.valueOf(SantosWindow.driverBox.getSelectedIndex()));
        cf.writeValue("lastHost", SantosWindow.hostField.getText());
        cf.writeValue("lastDatabase", SantosWindow.databaseField.getText());
        cf.writeValue("lastUsername", SantosWindow.userField.getText());
        cf.writeValue("lastPassword", "");
        cf.writeValue("lastQuery", SantosWindow.queryField.getText());
        cf.writeValue("previewField", SantosWindow.previewField.getText());
        cf.writeValue("lastPath", SantosWindow.outputFileLocation.getText());
        cf.writeValue("fileDefaultName", SantosWindow.outputFileName.getText());

    }

    public ArrayList<String> loadDriverNames() {
        return cf.loadValueList("driverNames");
    }

    public ArrayList<String> loadDriverPaths() {
        return cf.loadValueList("driverPaths");
    }

    public void saveDriverNames(ArrayList<String> driverNames) {
        cf.writeValueList("driverNames", driverNames);
    }

    public void saveDriverPaths(ArrayList<String> driverPaths) {
        cf.writeValueList("driverPaths", driverPaths);
    }

    public ArrayList<String[]> loadOPCO(String driverName) {
        ArrayList<String[]> list = new ArrayList<>();
        ArrayList<String> load = cf.loadValueList(driverName);
        for (String s : load) {
            list.add(s.split("%-%"));
        }
        return list;
    }

    public void saveOPCO(String driverName, ArrayList<String[]> list) {
        log.logTo("Saving opco list with driverName " + driverName + " and list size of " + list.size(), SantosWindow.logField);
        log.logTo("Saving opco list with driverName " + driverName + " and list size of " + list.size(), SantosWindow.outputLabel);
        ArrayList<String> save = new ArrayList<>();
        for (String[] a : list) {
            if (a.length == 3) {
                save.add(a[0] + "%-%" + a[1] + "%-%" + a[2]);
            }
        }
        cf.writeValueList(driverName, save);
    }

    public void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    public void loadServerList() {
        if (def.cf.loadValue("serverList") != null) {
            System.out.println("Key exists, time to get the path...");
            System.out.println("Loaded value is: " + def.cf.loadValue("serverList"));
            String path = def.cf.loadValue("serverList");
            File f = new File(path + "\\" + "System.sanfig");
            System.out.println("Loading from default system configuration file.");
            System.out.println("The system default file path is: " + path + "\\" + "System.sanfig");
            if (f.exists()) {
                System.out.println("Config System File does exist.");
            } else {
                infoBox("Default system file is missing.", "System Default Configuration");
            }
            if (f.exists() && !f.isDirectory()) {
                System.out.println("Found the file! Loading system defaults.");
                Config sys = new Config("System", path);
                
                opt.clearTable();

                String list = sys.loadValue("dataList");
                String[] spl = list.split("%-%");
                for (String s : spl) {
                    String[] split = s.split("%+%");
                    if (opt.checkUniqueEntry(split[0], split[1], split[2])) {
                        opt.addEntry(split);
                    }

                }
            } else {
                infoBox("Default system file is missing.", "System Default Configuration");
            }
        } else {
            infoBox("There is no default configuration file configured.", "System Default Configuration");
        }

    }

}
