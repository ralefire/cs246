package scripturefinder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ui.Updater;
 
/**
 * This holds a list of entries and can display and sort the entry items
 * Displays to the GUI through the updater class
 * @author Cameron Lilly
 */
public class Journal implements Runnable {

    private List<Entry> entries = new ArrayList(); // list of entries
    String filePath = ""; // filePath used for exporting journal to file
    private Updater updater; // used to update the GUI 
    
    /**
     * constructor with updater
     * @param updater 
     */
    public Journal(Updater updater) {
        this.updater = updater;
    }
    
    /**
     * get updater
     * @return updater
     */
    public Updater getUpdater() {
        return updater;
    }
    
    /**
     * set updater to input
     * @param updater 
     */
    public void setUpdater(Updater updater) {
        this.updater = updater;
    }
    
    /**
     * export the journal in plain text format
     * @param fileName
     * @return true if export successful, false otherwise
     */
    public boolean exportTXTFile(String fileName) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileName, "UTF-8");
            for (Entry entry : entries) {
                writer.println("-----");
                writer.println(entry.getDate().toString());
                writer.println(entry.getContent());
                }
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(Journal.class.getName()).log(Level.SEVERE, null, ex);
            writer.close();
            return false;
        } finally {
            writer.close();
        }
        return true;
    }
    
    /**
     * display function outputs the dates of entries that have similar scriptures
     * and topics without duplication
     * @param input 
     */
    public void displayBooksAndTopics(List<Entry> input) {
        entries = input;
        List<Scripture> sList = new ArrayList(); //list of scripture objects
        List<String> books = new ArrayList(); //list of books
        List<String> tList = new ArrayList(); //List of topics
        Map<String, List<Entry>> sMap = new TreeMap(); // map of form <book, entry list>
        Map<String, List<Entry>> tMap = new TreeMap(); // map of form <topic, entry list>

        //Iterate through entries list
        for (Entry entry : entries) {

            sList = entry.getScriptures();
            tList = entry.getTopics();
            List<Entry> entryList = new ArrayList();
            entryList.add(entry);

            for (Scripture s : sList) {
                String book = s.getBook();
                List<Entry> tempEList = sMap.putIfAbsent(book, entryList); //add if book absent

                //if book isn't absent, check if the entry is already added
                if (tempEList != null) {
                    Boolean exists = false;
                    for (Entry e : tempEList) {
                        if (e.equals(entry)) {
                            exists = true;
                            break;
                        } 
                    }

                    // if entry not yet added, add it
                    if (!exists) {
                        tempEList.add(entry);
                        sMap.put(book, tempEList);
                    }
                }
            }

            for (String t : tList) {
                List<Entry> tempEList = tMap.putIfAbsent(t, entryList); // add topic if absent

                //if topic isn't absent, check if entry is added yet
                if (tempEList != null) {
                    Boolean exists = false;
                    for (Entry e : tempEList) {
                        if (e.equals(entry)) {
                            exists = true;
                            break;
                        } 
                    }

                    // if entry isn't already added, add it
                    if (!exists) {
                        tempEList.add(entry);
                        tMap.put(t, tempEList);
                    }
                }
            }
        }

        // display books and matching entry dates without duplicates
        System.out.println("Scripture References:");
        for(String key : sMap.keySet()) {
            List<Entry> tempEList = sMap.get(key);
            System.out.println(key);

            for (Entry e : tempEList) {
                System.out.println("\t" + e.getDate().toString());
            }
        }          

        // display topics and matching entry dates without duplicates
        System.out.println("\nTopic References:");
        for(String key : tMap.keySet()) {
            List<Entry> tempEList = tMap.get(key);
            System.out.println(key);

            for (Entry e : tempEList) {
                System.out.println("\t" + e.getDate().toString());
            }
        }
    }

    /**
     * sorts the entries by scriptures displaying the entries that contain the given books of scriptures
     * @return sorted list of strings
     */
    public String sortByScriptures() {
        List<Scripture> sList = new ArrayList(); //list of scripture objects
        List<String> books = new ArrayList(); //list of books
        Map<String, List<Entry>> sMap = new TreeMap(); // map of form <book, entry list>
        String result = "";

        //Iterate through entries list
        for (Entry entry : entries) {
            sList = entry.getScriptures();
            List<Entry> entryList = new ArrayList();
            entryList.add(entry);

            for (Scripture s : sList) {
                String book = s.getBook();
                List<Entry> tempEList = sMap.putIfAbsent(book, entryList); //add if book absent

                //if book isn't absent, check if the entry is already added
                if (tempEList != null) {
                    Boolean exists = false;
                    for (Entry e : tempEList) {
                        if (e.equals(entry)) {
                            exists = true;
                            break;
                        } 
                    }

                    // if entry not yet added, add it
                    if (!exists) {
                        tempEList.add(entry);
                        sMap.put(book, tempEList);
                    }
                }
            }
        }

        // display books and matching entry dates without duplicates
        for(String key : sMap.keySet()) {
            List<Entry> tempEList = sMap.get(key);
            result += (key + "\n");

            for (Entry e : tempEList) {
                result += ("\t" + e.getDateAsString() + "\n");
            }
        }
        return result;
    }
    
    /**
     * sorts the entries by topics displaying the entry dates that contain topic keys 
     * from the config file
     * @return sorted list of strings
     */
    public String sortByTopics() {
        List<String> books = new ArrayList(); //list of books
        List<String> tList = new ArrayList(); //List of topics
        Map<String, List<Entry>> tMap = new TreeMap(); // map of form <topic, entry list>
        String result = "";
        //Iterate through entries list
        for (Entry entry : entries) {
            tList = entry.getTopics();
            List<Entry> entryList = new ArrayList();
            entryList.add(entry);

            for (String t : tList) {
                List<Entry> tempEList = tMap.putIfAbsent(t, entryList); // add topic if absent

                //if topic isn't absent, check if entry is added yet
                if (tempEList != null) {
                    Boolean exists = false;
                    for (Entry e : tempEList) {
                        if (e.equals(entry)) {
                            exists = true;
                            break;
                        } 
                    }

                    // if entry isn't already added, add it
                    if (!exists) {
                        tempEList.add(entry);
                        tMap.put(t, tempEList);
                    }
                }
            }
        }       

        // display topics and matching entry dates without duplicates
        for(String key : tMap.keySet()) {
            List<Entry> tempEList = tMap.get(key);
            result += (key + "\n");

            for (Entry e : tempEList) {
                result += ("\t" + e.getDateAsString() + "\n");
            }
        }
        return result;
    }
    
    /**
     * get entries 
     * @return entries
     */
    public List<Entry> getEntries() {
        return entries;
    }

    /**
     * Builds an entry from the given content String by calling the parse class
     * @param content 
     */
    public void addEntry(String content) {
        try {
            Parser parser = new Parser(); //create parser object
            List<String> topics = parser.parseTopics(content);
            List<Scripture> scriptures = parser.parseScripture(content);
            Entry entry = new Entry(content, topics, scriptures);
            entries.add(entry);
        } catch (IOException ex) {
            System.out.println("Adding entry failed");
        }
    }
    
    /**
     * loads entries from a file
     */
    public void loadFromFile() {
        Parser tempParser = new Parser(); //create parser object
        tempParser.setFilePath(filePath);
        entries = tempParser.parseFile(updater); //parse input file and collect entries
    }
    
    /**
     * exports journal as an XML file
     * @param fileName
     * @return true if export successful, false otherwise
     */
    public boolean exportXMLFile(String fileName) {
        //export entries into output xml file
        if (entries == null) {
            return false;
        }
        XMLparser xmlParser = new XMLparser();
        try {
            Document doc = xmlParser.buildXMLDocument(entries);
            xmlParser.saveXML(doc, fileName);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Journal.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * loads journal from an XML file
     * @param fileName
     * @return true if load successful, false otherwise
     */
    public boolean loadFromXMLFile(String fileName) {
        //Create XMLparser reader object
        XMLparser xmlParser = new XMLparser();
        //read the xml file
        try {
            entries = xmlParser.parseXmlFile(fileName);
        } catch (SAXException ex) {
            Logger.getLogger(Journal.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Implements the run function to allow loading the journal in a thread 
     * from a plain text file. 
     */
    @Override
    public void run() {
        loadFromFile();
        updater.displayDone();
    }
    
    /**
     * setter method for filePath
     * @param filePath 
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }    
    
}
