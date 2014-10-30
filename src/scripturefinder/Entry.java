package scripturefinder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * This holds an individual journal entry, along with its scriptures and topics.
 * @author Cameron Lilly
 */
public class Entry {
    private String content;
    private Date date;
    private List<String> topics;
    private List<Scripture> scriptures;
    private String title;
    
    /**
    * Entry constructor expecting content, date, list of topics, and a title
     * @param content
     * @param date
     * @param topics
     * @param title
    */
    public Entry(String content, Date date, List<String> topics, String title) {
        this.content = content;
        this.date = date;
        this.topics = topics;
        this.title = title;
        this.scriptures = null;
    }
    
    /**
     * Entry Constructor
     * @param content
     * @param topics
     * @param scriptures 
     */
    public Entry(String content, List<String> topics, List<Scripture> scriptures) {
        this.content = content;
        this.date = new Date();
        this.topics = topics;
        title = "";
        this.scriptures = scriptures;
    }
    
    /**
     * Default Constructor
     */
    public Entry() {
        content = "";
        date = null;
        topics = null;
        title = "";
        scriptures = null;
    }
    
    /**
     * Get topics
     * @return list of topics
     */
    public List<String> getTopics() {
        return topics;
    }
    
    /**
     * Get content
     * @return content
     */
    public String getContent() {
        return content;
    }
    
    /**
     * set content
     * @param content 
     */
    public void setContent(String content) {
        this.content = content;
    }
    
    /**
     * set the list of topics
     * @param topics 
     */
    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
    
    /**
     * get scriptures
     * @return list of scriptures
     */
    public List<Scripture> getScriptures() {
        return scriptures;
    }
    
    /**
     * set scriptures
     * @param scriptures 
     */
    public void setScriptures(List<Scripture> scriptures) {
        this.scriptures = scriptures;
    }
    
    /**
     * get title
     * @return title
     */
    public String getTitle() {
       return title;
    }
    
    /**
     * set title
     * @param title 
     */
    public void setTitle(String title) {
       this.title = title;
    }
    
    /**
     * set date
     * @param date 
     */
    public void setDate(Date date) {
        this.date = date;
    }
    
    public Date getDate() {
        return date;
    }
    
    /**
     * Return date object as string formated as (yyyy/mm/dd)
     * @return formated date as string (yyyy/mm/dd)
     */
    public String getDateAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(date);
    }
    
    /**
     * displays content to the console
     */
    public void displayContent() {
        System.out.println(content);
    }
    
    /**
     * displays scriptures in the form book chap:verse-verse\n
     */
    public void display() {
        for (Scripture s : scriptures) {
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
    
    /**
     * Displays entry dates with their corresponding scripture list below
     */
    public void displayScripturesWithDates() {
        for (Scripture s : scriptures) {
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
