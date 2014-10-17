/**
 * Milestone 3b
 * @author Cameron Lilly
 * Collaboration: Bryce Call helped explain what static meant in Java
 */
package scripturefinder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
public class ScriptureFinder {

    public static void main(String[] args) throws IOException {
        String readXMLFile;
        String iTxtFile;
        String oXMLFile;
        String oTXTFile;
        List<Scripture> scriptures = new ArrayList();
        List<String> topics = new ArrayList();
 
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

        //Create XMLparser reader object
        XMLparser xmlParser = new XMLparser();
        List<Entry> entries = new ArrayList();

        //read the xml file
        /*try {
            entries = xmlParser.parseXmlFile(readXMLFile);
        } catch (SAXException ex) {
            Logger.getLogger(ScriptureFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
         */
        ScriptureFinder journal = new ScriptureFinder();
//        if (!entries.isEmpty())
//           journal.displayBooksAndTopics(entries);
       
        Parser tempParser = new Parser();
        
        entries = tempParser.parseFile(iTxtFile); 
        journal.exportTXTFile(oTXTFile, entries);
        
        try {
            Document doc = xmlParser.buildXMLDocument(entries);
            xmlParser.saveXML(doc, oXMLFile);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ScriptureFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    
    public void exportTXTFile(String fileName, List<Entry> entries) {
        
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
            Logger.getLogger(ScriptureFinder.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
    }
    
    
    private void displayBooksAndTopics(List<Entry> entries) {
    
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
    
}
