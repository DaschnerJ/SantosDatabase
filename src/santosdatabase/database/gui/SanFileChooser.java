/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package santosdatabase.database.gui;

import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author justdasc
 */
public class SanFileChooser {
    
    private String location;
    public static String lastPath = null;

    public SanFileChooser() {
        JFileChooser f = new JFileChooser();
        if(lastPath != null && !lastPath.equals("") && !lastPath.equals("null"))
        {
            System.out.println("Loading from old path: " + lastPath);
            f.setCurrentDirectory(new java.io.File(lastPath + "\\."));
        }
        else
        {
            System.out.println("Loading from default path.");
            f.setCurrentDirectory(new java.io.File("."));
        }
        f.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        //f.setFileFilter(filter);
        
       
        f.showSaveDialog(null);
        f.setVisible(true);

        //System.out.println(f.getCurrentDirectory());
        if(f.getSelectedFile() != null)
        {
            this.location = f.getSelectedFile().getPath();
            System.out.println("Location Print Test:" + location);
            setLastPath(f.getCurrentDirectory());
            System.out.println(location);
        }
    }
    
    public SanFileChooser(Integer j) {
        JFileChooser f = new JFileChooser();
        if(lastPath != null && !lastPath.equals("") && !lastPath.equals("null"))
        {
            System.out.println("Loading from old path: " + lastPath);
            f.setCurrentDirectory(new java.io.File(lastPath + "."));
        }
        else
        {
            System.out.println("Loading from default path.");
            f.setCurrentDirectory(new java.io.File("."));
        }
        f.setFileSelectionMode(j);
        f.showSaveDialog(null);

        //System.out.println(f.getCurrentDirectory());
        if(f.getSelectedFile() != null)
        {
            this.location = f.getSelectedFile().getPath();
            setLastPath(f.getSelectedFile());
            System.out.println(location);
        }
    }
    
    public SanFileChooser(Integer j, String button) {
        JFileChooser f = new JFileChooser();
        if(lastPath != null && !lastPath.equals("") && !lastPath.equals("null"))
        {
            System.out.println("Loading from old path: " + lastPath);
            f.setCurrentDirectory(new java.io.File(lastPath + "\\."));
        }
        else
        {
            System.out.println("Loading from default path.");
            f.setCurrentDirectory(new java.io.File("."));
        }
        System.out.println("Setting selection mode.");
        f.setFileSelectionMode(j);
        System.out.println("Setting button.");
        f.setApproveButtonText(button);
        System.out.println("Opening dialog.");
        int result = f.showOpenDialog(null);
        
        
        
        if(result == JFileChooser.APPROVE_OPTION)
        {
        //System.out.println(f.getCurrentDirectory());
        if(f.getSelectedFile() != null)
        {
            System.out.println("Saving location...");
            this.location = f.getSelectedFile().getPath();
            setLastPath(f.getSelectedFile());
            System.out.println(location);
        }
        }
        else
        {
            throw new NullPointerException();
        }
    }
    
    public void setLastPath(File f)
    {
        String absolutePath = f.getAbsolutePath();
    	    System.out.println("File path : " + absolutePath);

    	    String filePath = f.getPath();
            System.out.println("Last path: " + filePath);
            SanFileChooser.lastPath = filePath + "\\";
    }
    
    public String getPath()
    {
        return location;
    }
    
    

}
