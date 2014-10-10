/*
 */
package scripturefinder;
import java.io.File;
import java.io.IOException;
import java.lang.Runnable;
import java.util.ArrayList;
//import java.util.Date;
import java.sql.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import jdk.internal.org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 *
 * @author Admin
 */
public class XML implements Runnable {

    public Boolean saveXML() {
        return null;
    }
    
    public Boolean importXML(String input) {
       return null;
    }
    
    public List<Entry> parseXmlFile(String fileName) throws org.xml.sax.SAXException {
        
        List<Entry> entryList = new ArrayList(); 
        List<String> topicList = new ArrayList();
        List<Scripture> scriptureList = new ArrayList();
        Document dom = null;
        
        //set default xml file location
        if (fileName.equals("")) {
            fileName = "C:\\Users\\Admin\\Documents\\NetBeansProjects\\ScriptureFinder\\src\\scripturefinder";
        }
        
        //get the document builder factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
                //Using factory get an instance of document builder
                DocumentBuilder db = dbf.newDocumentBuilder();
               
               //parse using builder to get DOM representation of the XML file
               //dom = db.parse(ClassLoader.getSystemResourceAsStream(fileName));
                File file = new File(fileName); //open file
                dom = db.parse(file);

            } catch(ParserConfigurationException | IOException pce) {
                System.out.println("Parsing error");
            }
        
        //get the root element
        Element docEle;
        docEle = dom.getDocumentElement();

        //get a nodelist of elements
        NodeList eList = null;
        
        //if the root element was successful, find the entry elements
        if (docEle != null) {
            eList = docEle.getElementsByTagName("entry");
        }
        
        //If entry elements are found start loop
        if (eList != null && eList.getLength() > 0) {

            //Loop and build Entry objects
            for (int i = 0;  i < eList.getLength(); i++) {

                Element entryElem = (Element)eList.item(i); //Get entry elements
                Entry entry = new Entry(); //Create entry to add to entry list
                NodeList topics = entryElem.getElementsByTagName("topic"); //get topic nodes

                //add topic node content to topic list
                for (int j = 0; j < topics.getLength(); j++) {
                   String topic = ((Element)topics.item(j)).getTextContent();
                   topicList.add(topic);
                }
                
                //set the entry topic list
                entry.setTopics(topicList);

                //get scripture nodes
                NodeList scriptures = entryElem.getElementsByTagName("scripture");

                //build scripture object from node values
                for (int j = 0; j < scriptures.getLength(); j++) {
                   Scripture scripture = new Scripture();
                   String sBook = ((Element)scriptures.item(j)).getAttribute("book");
                   String chapter = ((Element)scriptures.item(j)).getAttribute("chapter");
                   String sVerse = ((Element)scriptures.item(j)).getAttribute("startverse");
                   String eVerse = ((Element)scriptures.item(j)).getAttribute("endverse");
                   
                   if (eVerse.equals("")) {
                       eVerse = sVerse;
                   }
                   
                   if (!sVerse.equals("")) {
                        scripture.setBook(sBook);
                        scripture.setChapter(Integer.parseInt(chapter));
                        int startVerse = Integer.parseInt(sVerse.trim());
                        int endVerse = Integer.parseInt(eVerse.trim());
                        scripture.setVerses(startVerse, endVerse);
                        scriptureList.add(scripture);
                   }

                }
                
                //set entry scripture list
                entry.setScriptures(scriptureList);

                //get entry date attribute
                String sDate = entryElem.getAttribute("date");
                Date date = new Date(123456); //construct sql Date
                date = Date.valueOf(sDate); //parse string into sql Date object format YYYY-MM-DD
                entry.setDate(date); //set entry date

                //get content node values
                String content = entryElem.getElementsByTagName("content").item(0).getTextContent();

                //format content by removing newlines and extra spacing
                if (content != null && !content.equals("")) {
                content = content.trim();
                content = content.replaceAll("\\n\\s+", "\n");
                }
                
                //Add content to entry
                entry.setContent(content);
                //Add entry to entry list for return statement
                entryList.add(entry);
            }
	}
        return entryList;
    }
    
    @Override
    public void run() {
    
    }
    
    public Boolean exportDocx() {
       return null;
    }
}
