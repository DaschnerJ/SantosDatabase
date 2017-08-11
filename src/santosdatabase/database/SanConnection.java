/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package santosdatabase.database;

import com.ibm.as400.access.AS400JDBCDataSource;
import com.ibm.as400.access.AS400JDBCDriver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import static santosdatabase.SantosDatabase.log;
import santosdatabase.database.gui.SantosWindow;
import santosdatabase.database.type.Machine;

/**
 * @author justdasc
 */
public class SanConnection {

    public Machine machine = null;

    String QUERY = null;
    
    public String tag = "";

    private Connection con;
    private PreparedStatement stmt;
    public ResultSet rs;
    private AS400JDBCDataSource ds;

    public SanConnection(String DRIVER, String HOST, String USER, String PASS, String TAG) {
        machine = new Machine(DRIVER, HOST, USER, PASS, TAG);
    }

    public SanConnection(String DRIVER, String HOST, String USER, String PASS, String QUERY, String TAG) {
        machine = new Machine(DRIVER, HOST, USER, PASS, TAG);
        this.QUERY = QUERY;
    }

    public SanConnection(String DRIVER, String HOST, String DATABASE, String USER, String PASS, String QUERY, String TAG) {
        System.out.println("Checking it three times?...");
        machine = new Machine(DRIVER, HOST, USER, PASS, TAG);
        if(machine.user.equals(""))
        {
            machine.user = System.getProperty("user.name");
        }
        this.executeDBQuery(QUERY, DATABASE);
    }

    public SanConnection(Machine m, String query) {
        System.out.println("Checking it twice...");
        if(m.user.equals(""))
        {
            m.user = System.getProperty("user.name");
        }
        machine = m;

        this.executeDBQuery(query, m.database);
    }

    public boolean createDriverConnector() {
        try {
            Class.forName(machine.driver);
            //con = DriverManager.getConnection(HOST, USER, PASS);
            DriverManager.registerDriver(new AS400JDBCDriver());
            ds = new AS400JDBCDataSource(machine.host);
        } catch (SQLException | ClassNotFoundException ex) {
            log.addSevere(ex.getMessage());
        }
        return true;
    }

    public boolean setCredentials() {
        if(!machine.user.equals(""))
        {
            ds.setUser(machine.user);
        }
        if(!machine.pass.equals(""))
            ds.setPassword(machine.pass);
        ds.setNaming("sql");
        if(!machine.database.equals(""))
            ds.setDatabaseName(machine.database);
        return true;
    }

    public void setDatabase(String DATABASE) {
        machine.database = DATABASE;
    }

    public boolean executeDBQuery(String QUERY, String DATABASE) {
        createDriverConnector();
        setQuery(QUERY);
        setDatabase(DATABASE);
        setCredentials();
        if (!createConnection()) {
            return false;
        }
        executeQuery();
        return true;
    }

    public boolean createConnection() {
        try {
            if (ds.getConnection() == null) {
                //log.logTo("Connection is null...", SantosWindow.logField);
                log.logTo("Connection is null...", SantosWindow.outputLabel);
            }
            con = ds.getConnection();
            if (con == null) {
                JOptionPane.showMessageDialog(null, "Query was invalid.");
                //log.logTo("The query " + QUERY + " was invalid. Run cancelled.", SantosWindow.logField);
                log.logTo("The query " + QUERY + " was invalid. Run cancelled.", SantosWindow.outputLabel);
                return false;
            }
        } catch (SQLException ex) {
            log.addSevere(ex.toString());
        }
        return true;
    }

    public boolean executeQuery() {
        if (QUERY != null) {
            try {
                con.setAutoCommit(false);
                try {
                    this.stmt = con.prepareStatement(QUERY + " ORDER BY 1");
                    this.stmt.setFetchSize(50);
                    rs = this.stmt.executeQuery();    
                }
                catch(Exception e)
                {
                    //log.logTo("Table was unable to be preformatted or it may already be included in the statement!", SantosWindow.logField);
                    log.logTo("Table was unable to be preformatted or it may already be included in the statement!", SantosWindow.outputLabel);
                }
                finally
                {
                    if(rs != null)
                        rs.close();
                    if(stmt != null)
                        stmt.close();
                }
                this.stmt = con.prepareStatement(QUERY);
                this.stmt.setFetchSize(50);
                rs = stmt.executeQuery();
            } catch (SQLException | NullPointerException ex) {
                JOptionPane.showMessageDialog(null, "Query was invalid.");
                //log.logTo("The query " + QUERY + " or connection was invalid. Run cancelled.", SantosWindow.logField);
                log.logTo("The query " + QUERY + " or connection was invalid. Run cancelled.", SantosWindow.outputLabel);
            }
        }
        return true;
    }

    public boolean setQuery(String QUERY) {
        this.QUERY = QUERY;
        return true;
    }

    public boolean closeConnection() {
        try {
            if (!con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            log.addSevere(ex.getMessage());
        }
        return true;
    }

}
