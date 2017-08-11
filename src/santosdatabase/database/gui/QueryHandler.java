/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package santosdatabase.database.gui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.ListSelectionModel;
import static santosdatabase.SantosDatabase.log;

/**
 *
 * @author justdasc
 */
public class QueryHandler {

    private ListSelectionModel sm;
    private String query;
    private String path;

    Properties prop = new Properties();

    OutputStream output;
    InputStream input;

    public QueryHandler() {
        this.input = null;
        this.output = null;
        requestLocation("Load");
        load();
    }

    public QueryHandler(ListSelectionModel sm, String query) {
        this.input = null;
        this.output = null;
        this.sm = sm;
        this.query = query;
        requestLocation();
        save();
    }

    private void save() {
        prop.put("query", query);
        prop.put("rows", getRows());
        try {
            output = new FileOutputStream(path);
            prop.store(output, null);
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            //log.logTo("The window was closed or the file was invalid.", SantosWindow.logField);
            log.logTo("The window was closed or the file was invalid.", SantosWindow.outputLabel);
        }
    }

    private void load() {
        try {
            input = new FileInputStream(path);
            // load a properties file
            prop.load(input);
            if (prop.containsKey("query")) {
                if (prop.containsKey("rows")) {
                    SantosWindow.queryField.setText(prop.getProperty("query"));
                    ArrayList<String> rowList = new ArrayList(Arrays.asList(prop.getProperty("rows").split("%-%")));
                    ArrayList<Row> rows = new ArrayList<>();
                    for (String str : rowList) {
                        rows.add(new Row(str));
                    }

                    int indexes = SantosWindow.opcoList.getRowCount();
                    for (int index = 0; index < indexes; index++) {

                        SantosWindow.opcoList.getSelectionModel().removeSelectionInterval(index, index);
                    }
                    for (Row r : rows) {
                        for (int index = 0; index < indexes; index++) {
                            if (SantosWindow.opcoList.getValueAt(index, 0).toString().equals(r.tag)) {
                                if (SantosWindow.opcoList.getValueAt(index, 1).toString().equals(r.host)) {
                                    if (SantosWindow.opcoList.getValueAt(index, 2).toString().equals(r.database)) {
                                        SantosWindow.opcoList.getSelectionModel().addSelectionInterval(index, index);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    log.logTo("Failed to load query file. Missing fields.", SantosWindow.logField);
                }
            } else {
                log.logTo("Failed to load query file. Missing fields.", SantosWindow.logField);
            }
        } catch (Exception ex) {
            log.logTo("File failed to load or the window was closed.", SantosWindow.logField);
        }

    }

    private void requestLocation() {
        SanFileChooser f = new SanFileChooser();
        path = f.getPath();
    }

    private void requestLocation(String text) {
        SanFileChooser f = new SanFileChooser(JFileChooser.FILES_ONLY, text);
        path = f.getPath();
    }

    private String requestDefaultLocation(String text) {
        SanFileChooser f = new SanFileChooser(JFileChooser.DIRECTORIES_ONLY, text);
        return f.getPath();
    }

    public QueryHandler(String location) {
        this.input = null;

    }

    public String getRows() {
        ArrayList<Row> rows = new ArrayList<>();
        int[] rowIndexes = SantosWindow.opcoList.getSelectedRows();
        for (int ri : rowIndexes) {
            String tag = String.valueOf(SantosWindow.opcoList.getValueAt(ri, 0));
            String host = String.valueOf(SantosWindow.opcoList.getValueAt(ri, 1));
            String database = String.valueOf(SantosWindow.opcoList.getValueAt(ri, 2));
            rows.add(new Row(tag, host, database));
        }

        ArrayList<String> rowList = new ArrayList<>();
        for (Row r : rows) {
            rowList.add(r.toString());
        }

        String res = "";
        for (String r : rowList) {
            if (res.equals("")) {
                res = r;
            } else {
                res = res + "%-%" + r;
            }
        }
        return res;
    }

    private class Row implements Serializable {

        String tag;
        String host;
        String database;

        public Row(String tag, String host, String database) {
            this.tag = tag;
            this.host = host;
            this.database = database;
        }

        public Row(String o) {
            Pattern spl;
            spl = Pattern.compile("%+%", Pattern.LITERAL);

            System.out.println(o);
            String[] s = spl.split(o);
            System.out.println(s.length);
            tag = s[0];
            host = s[1];
            database = s[2];
        }

        @Override
        public String toString() {
            return tag + "%+%" + host + "%+%" + database;
        }

    }

}
