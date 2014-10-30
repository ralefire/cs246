package scripturefinder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
/**
 * This handles the reading and exporting of XML files
 * @author Cameron Lilly
 */
public class XMLparser {
    
    /**
     * generates an XML document 
     * @param entries
     * @return Document object set with XML elements and attributes
     * @throws ParserConfigurationException 
     */
    public Document buildXMLDocument(List<Entry> entries) throws ParserConfigurationException {
        
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // root element
        Element root = document.createElement("journal");
        document.appendChild(root);

        // children element
        for (Entry entry : entries) {
            Element elemEntry = document.createElement("entry");
            elemEntry.setAttribute("date", entry.getDateAsString());
            
                    
            for (Scripture scripture : entry.getScriptures()) {
                Element elemScripture = document.createElement("scripture");
                elemScripture.setAttribute("book", scripture.getBook());
                elemScripture.setAttribute("chapter", String.valueOf(scripture.getChapter()));
                elemScripture.setAttribute("startverse", String.valueOf(scripture.getVerseStart()));
                elemScripture.setAttribute("endverse", String.valueOf(scripture.getVerseEnd()));
                elemEntry.appendChild(elemScripture);
            }
            
            for (String topic : entry.getTopics()) {
                Element elemTopic = document.createElement("topic");
                elemTopic.setTextContent(topic);
                elemEntry.appendChild(elemTopic);
            }
            
            Element elemContent = document.createElement("content");
            elemContent.setTextContent(entry.getContent());
            elemEntry.appendChild(elemContent);
            
            root.appendChild(elemEntry);
        }
        
        return document;
    }
    
    /**
     * This saves a formatted XML document into the specified fileName
     * @param doc
     * @param fileName
     * @return true if no exceptions are thrown, false otherwise
     */
    public Boolean saveXML(Document doc, String fileName) {
        try {
            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File(fileName));
            transformer.transform(domSource, streamResult);
        } catch (TransformerConfigurationException ex ) {
            Logger.getLogger(XMLparser.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (TransformerException exc ) {
            Logger.getLogger(XMLparser.class.getName()).log(Level.SEVERE, null, exc);
            return false;
        }
    
        return true;
    }
    
    /**
     * This parses an XML file for entries and their corresponding scriptures, topics, etc.
     * then returns the entries in a list
     * @param filePath
     * @return list of entries
     * @throws org.xml.sax.SAXException 
     */
    public List<Entry> parseXmlFile(String filePath) throws org.xml.sax.SAXException {
        
        List<Entry> entryList = new ArrayList(); 

        Document dom = null;
        
        //set default xml file location
        if (filePath.equals("")) {
            filePath = "C:/Users/Admin/Documents/NetBeansProjects/ScriptureFinder/src/scripturefinder";
        }
        
        //get the document builder factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
                //Using factory get an instance of document builder
                DocumentBuilder db = dbf.newDocumentBuilder();
               
               //parse using builder to get DOM representation of the XMLparser file
               //dom = db.parse(ClassLoader.getSystemResourceAsStream(filePath));
                File file = new File(filePath); //open file
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
                List<String> topicList = new ArrayList();
                List<Scripture> scriptureList = new ArrayList();
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
}
