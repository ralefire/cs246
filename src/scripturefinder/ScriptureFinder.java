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
import java.util.Properties;
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

            // default to money.txt if no command line arguments are present
            if (args.length == 0) {
                    fileName = "C:\\Users\\Admin\\Documents\\NetBeansProjects\\ScriptureFinder\\src\\scripturefinder\\XML.xml"; 
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
            
            for (Entry entry : entries) {
                entry.display();
            }
            
            Parser parseEntry = new Parser();
            parseEntry.parseTopics("Is there a Faith topic in here?");
//            Properties props = new ReadValidFiles().getProps();
//            String termsPath = props.getProperty("validTopicsPath");
//            String scripturesPath = props.getProperty("validScripturesPath");
//            
//            BufferedReader in = new BufferedReader(new FileReader(termsPath));
//            
//            System.out.println(termsPath + "\n" + scripturesPath);
    }
}
