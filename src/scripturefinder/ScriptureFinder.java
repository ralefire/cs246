/**
 * Milestone 3b
 * @author Cameron Lilly
 * Collaboration: Bryce Call helped explain what static meant in Java
 */
package scripturefinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.SAXException;
 
/**
 * Main Class.
 * This program accepts a command line argument filename then parses out
 * and displays the scripture and topic references
 */
public class ScriptureFinder {

    public static void main(String[] args) throws IOException {
        String fileName;

        // default to XML path if no command line arguments are present
        if (args.length == 0) {
                fileName = "C:/Users/Admin/Documents/NetBeansProjects/ScriptureFinder/src/scripturefinder/XML.xml"; 
        } else {
                fileName = args[0];
        }

        //Create XML reader object
        XML parser = new XML();
        List<Entry> entries = new ArrayList();

        //read the xml file
        try {
            entries = parser.parseXmlFile(fileName);
        } catch (SAXException ex) {
            Logger.getLogger(ScriptureFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ScriptureFinder journal = new ScriptureFinder();
        if (!entries.isEmpty())
           journal.displayBooksAndTopics(entries);
        
/*Tests that the validity file is working by reading the file and displaying the results*/
//            Parser parseEntry = new Parser();
//            parseEntry.parseTopics("Is there a Faith topic in here?");
//            parseEntry.parseScripture();
            
            
/*Tests that the properties file is working by displaying the values in the properties file*/
//            Properties props = new ReadValidFiles().getProps();
//            String termsPath = props.getProperty("validTopicsPath");
//            String scripturesPath = props.getProperty("validScripturesPath");
//            
//            BufferedReader in = new BufferedReader(new FileReader(termsPath));
//            
//            System.out.println(termsPath + "\n" + scripturesPath);
            
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
