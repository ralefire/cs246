/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripturefinder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Admin
 */
public class Entry {
    private String content;
    private Date date;
    private List<String> topics;
    private List<Scripture> scripture;
    private String title;
    
    public Entry(String iContent, Date iDate, List<String> iTopic, String iTitle) {
        content = iContent;
        date = iDate;
        topics = iTopic;
        title = iTitle;
        scripture = null;
    }
    
    public Entry(String iContent, List<String> iTopics, List<Scripture> iScriptures) {
        content = iContent;
        date = new Date();
        topics = iTopics;
        title = "";
        scripture = iScriptures;
    }
    
    public Entry() {
        content = "";
        date = null;
        topics = null;
        title = "";
        scripture = null;
    }
    
    public List<String> getTopics() {
    
        return topics;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String input) {
        content = input;
    }
    
    public void setTopics(List<String> input) {
        topics = input;
    }
    
    public List<Scripture> getScriptures() {
        return scripture;
    }
    
    public void setScriptures(List<Scripture> input) {
        scripture = input;
    }
    
    public String getTitle() {
       return title;
    }
    
    public void setTitle(String input) {
       title = input;
    }
    
    public void setDate(Date input) {
        date = input;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void displayContent() {
        System.out.println(content);
    }
    
    public void display() {
        for (Scripture s : scripture) {
            s.display();
        }
        System.out.println("Entry Date: " + date.toString());
        System.out.println("Scriptures: ");

        
        System.out.println("Topics:");
        for (String s : topics) {
            System.out.println("\t" + s);
        }
        System.out.println("Content:\n\t" + content);
    }
    
        public void displayScripturesWithDates() {
        for (Scripture s : scripture) {
            s.display();
        }
        System.out.println("Entry Date: " + date.toString());
        System.out.println("Scriptures: ");

        
        System.out.println("Topics:");
        for (String s : topics) {
            System.out.println("\t" + s);
        }
        System.out.println("Content:\n\t" + content);
    }
    
}
