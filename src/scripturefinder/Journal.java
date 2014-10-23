/**
 * Milestone Import Export
 * @author Cameron Lilly
 * Collaboration: Bryce Call helped explain what static meant in Java
 */
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
 
/**
 * Main Class.
 * This program accepts a command line argument filename then parses out
 * and displays the scripture and topic references
 */
public class Journal {

    private List<Entry> entries = new ArrayList();
    
    public void notMainAnymore(String[] args) throws IOException {
        String readXMLFile;
        String iTxtFile;
        String oXMLFile;
        String oTXTFile;
        List<Scripture> scriptures = new ArrayList();
        List<String> topics = new ArrayList();
 
        //Get Commandline arguments and set default arguments
        
        switch (args.length) {
            case 1: 
                readXMLFile = "C:/Users/Admin/Documents/NetBeansProjects/ScriptureFinder/src/scripturefinder/XML.xml";
                iTxtFile = args[0];
                oXMLFile = "C:/Users/Admin/Documents/NetBeansProjects/ScriptureFinder/src/scripturefinder/XMLOutput.xml";
                oTXTFile = "C:/Users/Admin/Documents/NetBeansProjects/ScriptureFinder/src/scripturefinder/TXTOutput.txt";
                break;
            case 2:
                 readXMLFile = "C:/Users/Admin/Documents/NetBeansProjects/ScriptureFinder/src/scripturefinder/XML.xml";
                iTxtFile = args[0];
                oXMLFile = args[1];
                oTXTFile = "C:/Users/Admin/Documents/NetBeansProjects/ScriptureFinder/src/scripturefinder/TXTOutput.txt";
                break;
            case 3:
                readXMLFile = "C:/Users/Admin/Documents/NetBeansProjects/ScriptureFinder/src/scripturefinder/XML.xml";
                iTxtFile = args[0];
                oXMLFile = args[1];
                oTXTFile = args[2];
                break;
            default:
                readXMLFile = "C:/Users/Admin/Documents/NetBeansProjects/ScriptureFinder/src/scripturefinder/XML.xml";
                iTxtFile = "C:/Users/Admin/Documents/NetBeansProjects/ScriptureFinder/src/scripturefinder/DefaultContent.txt";
                oXMLFile = "C:/Users/Admin/Documents/NetBeansProjects/ScriptureFinder/src/scripturefinder/XMLOutput.xml";
                oTXTFile = "C:/Users/Admin/Documents/NetBeansProjects/ScriptureFinder/src/scripturefinder/TXTOutput.txt";
        }
    }
    
    
    public boolean exportTXTFile(String fileName) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileName, "UTF-8");
            for (Entry entry : entries) {
                writer.println("-----");
                writer.println(entry.getDate().toString());
                writer.println(entry.getContent());
                }
            writer.println("-----");
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(Journal.class.getName()).log(Level.SEVERE, null, ex);
            writer.close();
            return false;
        } finally {
            writer.close();
        }
        return true;
    }
    
    
    private void displayBooksAndTopics(List<Entry> input) {
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

    public List<Entry> getEntries() {
        return entries;
    }

    public void addEntry(String content) {
        try {
            Parser parser = new Parser(); //create parser object
            List<String> topics = parser.parseTopics(content);
            List<Scripture> scriptures = parser.parseScripture(content);
            Entry entry = new Entry(content, topics, scriptures);
            entries.add(entry);
        } catch (IOException ex) {
            Logger.getLogger(Journal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadFromFile(String fileName) {
        Parser tempParser = new Parser(); //create parser object
        entries = tempParser.parseFile(fileName); //parse input file and collect entries
    }
    
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
    
}
