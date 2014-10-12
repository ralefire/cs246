/**
 * Milestone 1 - basic File I/O (1_FileIO)
 * @author Cameron Lilly
 * Collaboration: Bryce Call helped explain what static meant in Java
 */
package scripturefinder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.SAXException;

/**
 * Main Class.
 * This program accepts a command line argument filename then parses out
 * and displays the scripture references
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
            
            XML parser = new XML();
            List<Entry> entries = new ArrayList();
            
            try {
                entries = parser.parseXmlFile(fileName);
            } catch (SAXException ex) {
                Logger.getLogger(ScriptureFinder.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            List<Scripture> sList = new ArrayList();
            List<String> books = new ArrayList();
            List<String> tList = new ArrayList();
            //List<String> displayedBooks = new ArrayList();
            Map<String, List<Entry>> sMap = new TreeMap();
            Map<String, List<Entry>> tMap = new TreeMap();
            
//            for (Entry entry : entries) {
//                entry.display();
//            }
            
            for (Entry entry : entries) {

                sList = entry.getScriptures();
                tList = entry.getTopics();
                List<Entry> entryList = new ArrayList();
                entryList.add(entry);
                
//                System.out.println(entry.getDate().toString());
//                for (Scripture s : sList) {
//                    System.out.println(s.getBook());
//                }
                
                for (Scripture s : sList) {
                    String book = s.getBook();
                    List<Entry> tempEList = sMap.putIfAbsent(book, entryList);
                    
                    if (tempEList != null) {
                        Boolean exists = false;
                        for (Entry e : tempEList) {
                            if (e.equals(entry)) {
                                exists = true;
                                break;
                            } 
                        }
                        
                        if (!exists) {
                            tempEList.add(entry);
                            sMap.put(book, tempEList);
                        }
                    }
                }
                
//                for (String t : tList) {
//                    List<Entry> tempEList = tMap.putIfAbsent(t, entryList);
//                    
//                    if (tempEList != null) {
//                        Boolean exists = false;
//                        for (Entry e : tempEList) {
//                            if (e.equals(entry)) {
//                                exists = true;
//                                break;
//                            } 
//                        }
//                        
//                        if (!exists) {
//                            tempEList.add(entry);
//                            sMap.put(t, tempEList);
//                        }
//                    }
//                }
            }
            
            for(String key : sMap.keySet()) {
                List<Entry> tempEList = sMap.get(key);
                System.out.println(key);
                
                for (Entry e : tempEList) {
                    System.out.println("\t" + e.getDate().toString());
                }
            }          
          
            
//            for(String key : tMap.keySet()) {
//                List<Entry> tempEList = tMap.get(key);
//                System.out.println(key);
//                
//                for (Entry e : tempEList) {
//                    System.out.println("\t" + e.getDate().toString());
//                }
//            }
            
            Parser parseEntry = new Parser();
           // parseEntry.parseTopics("Is there a Faith topic in here?");
            
//            Properties props = new ReadValidFiles().getProps();
//            String termsPath = props.getProperty("validTopicsPath");
//            String scripturesPath = props.getProperty("validScripturesPath");
//            
//            BufferedReader in = new BufferedReader(new FileReader(termsPath));
//            
//            System.out.println(termsPath + "\n" + scripturesPath);
    }
}
