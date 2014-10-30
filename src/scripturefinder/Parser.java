package scripturefinder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ui.Updater;

/**
 * Parser class uses regular expressions to search for scripture references
 * and topics from a filePath
 */
public class Parser {

    // path to file to be parsed 
    private String filePath = "C:/Users/Admin/Documents/NetBeansProjects/"
            + "ScriptureFinder/src/scripturefinder/DefaultContent.txt";
    
    /**
     * Parses the input string for scriptures and returns them in a list
     * @param input
     * @return list of scriptures
     * @throws IOException 
     */
    public List<Scripture> parseScripture(String input) throws IOException {
        List<Scripture> scriptureList = new ArrayList();
        Properties props = new ReadValidFiles().getProps();
        String scripturesPath = props.getProperty("validScripturesPath");

        BufferedReader configIn = new BufferedReader(new FileReader(scripturesPath));
        String tempLine; //used to read the file line by line
        String book = "";
        String[] books;
        while ((tempLine = configIn.readLine()) != null) {
            books = tempLine.split(":");
            book += books[0] + "|";
        }
        
        book = book.substring(0, book.length() - 1);
                                     
        Scripture scripture = new Scripture();  
        //contains the regular expression to find scriptures in the form [book] [number]:[number]
        String regExBookNumNum = "(" + book + ")( (\\d{1,3}):(\\d{1,3}))";

        //contains the regular expression for scriptures of the form [book] chapter [number]
        String regExBookChapterBook = "((" + book + ") chapter( \\d))";

        //create pattern objects from the regular expressions
        Pattern pNum = Pattern.compile(regExBookNumNum);
        Pattern pChap = Pattern.compile(regExBookChapterBook);

        //create matcher object.
        Matcher mNum = pNum.matcher(input);
        Matcher mChap = pChap.matcher(input);

        //display the patterns found
        while (mNum.find( )) {
            //System.out.println("Found: " + mNum.group(1) + mNum.group(2) + mNum.group(3));
            scripture.setBook(mNum.group(1));
            int i = 0;
            try {
                i = Integer.parseInt(mNum.group(3));
            } catch (NullPointerException p) {
                i = 0;
            }

            scripture.setChapter(i);
            try {
                i = Integer.parseInt(mNum.group(4).trim());
            } catch (NullPointerException p) {
                i = 0;
            }

            scripture.setVerses(i, i);
            scriptureList.add(scripture);
            scripture = new Scripture();
        }

        //display the patterns found excluding the word "chapter"
        while (mChap.find( )) {
            //System.out.println("Found: " + mChap.group(2) + mChap.group(3));
            int i = 0;
            scripture.setBook(mChap.group(2));
            try {
                //System.out.println(mChap.group(3));
                i = Integer.parseInt(mChap.group(3).trim());
            } catch (NullPointerException | NumberFormatException p) {
                i = 0;
            }
            scripture.setChapter(i);
            scriptureList.add(scripture);
            scripture = new Scripture();
        }     
              
        return scriptureList;
    }

    /**
     * parses a file from filePath to collect a list of topic strings
     * from the config file valid topics
     * @param input
     * @return list of topic strings
     * @throws IOException 
     */
    public List<String> parseTopics(String input) throws IOException {
        // read config.properties to obtain valid topics
        Properties props = new ReadValidFiles().getProps();
        String termsPath = props.getProperty("validTopicsPath");
        BufferedReader in = new BufferedReader(new FileReader(termsPath));
        String line; //used to read the file line by line
        List<String> result = new ArrayList(); //stores list of topics for return value
        Map<String, String[]> tMap = new TreeMap(); //maps topic with topic indicators from valid file
        String topics = "";
        while ((line = in.readLine()) != null) {
            String[] lines = line.split(":"); //separate key topic from indicator topics
            String temp = lines[1].replaceAll(",", "|"); //prep indicator topics for regEx
            tMap.put(lines[0], lines[1].split(",")); //map topic with topic indicators
            topics += temp;
        }

            //open the file from fileName
            
            Scripture scripture = new Scripture();  
            //contains the regular expression to find topics
            String regExTopics = "(" + topics + ")";

            //create pattern objects from the regular expressions
            Pattern pTop = Pattern.compile(regExTopics);

            //create matcher object.
            Matcher mTop = pTop.matcher(input);

            //collect the patterns found
            while (mTop.find()) {
                tMap.keySet().stream().forEach((key) -> {
                    String[] values = tMap.get(key);
                    for (String v : values) {
                        if (v.matches(mTop.group()))
                            result.add(key);
                    }
                });
              }  
        return result;
    }

    /**
     * This parses a file from filePath to generate a list of entries 
     * @return list of entries
     */
    public List<Entry> parseFile() {
        List<Entry> result = new ArrayList();
        Entry entry = new Entry();
        Date date = new Date(1234);
        String content = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(filePath));
            String line = ""; //used to read the file line by line
        
            Boolean startEntry = false; //Used to know when a new entry is found and if a last entry is needed
            while (true) { 
                if (line != null && line.equals("-----")) {
                    if ((line = in.readLine()) != null) {
                        date = date.valueOf(line);
                        entry.setDate(date);
                        content = "";
                        startEntry = true;
                    }

                    while ((line = in.readLine()) != null && !line.equals("-----"))
                        content += line;
                                                                       
                    if (line != null) {
                        entry.setContent(content);
                        entry.setScriptures(parseScripture(content));
                        entry.setTopics(parseTopics(content));
                        result.add(entry);
                        Thread.sleep(500);
                        entry = new Entry();
                        content = "";
                        startEntry = false;
                    } 
                } else if ((line = in.readLine()) != null) {
                } else {
                    break;
                }
            }
            
            //Add the last entry if needed
            if (startEntry == true) {
                entry.setContent(content);
                entry.setScriptures(parseScripture(content));
                entry.setTopics(parseTopics(content));
                result.add(entry);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error opening file: " + filePath);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }      
        
        return result;
    }

    /**
     * This parses a file from filePath to generate a list of entries
     * The list of entries are displayed to the user through the updater class
     * @param updater
     * @return a list of entries with their corresponding scriptures, topics, dates, etc.
     */
    public List<Entry> parseFile(Updater updater) {
        updater.resetCounts();
        List<Entry> result = new ArrayList();
        Entry entry = new Entry();
        Date date = new Date(1234);
        String content = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(filePath));
            String line = ""; //used to read the file line by line
        
            Boolean startEntry = false; //Used to know if a new entry is found and a last entry is needed
            while (true) { 
                if (line != null && line.equals("-----")) {
                    if ((line = in.readLine()) != null) {
                        date = Date.valueOf(line);
                        entry.setDate(date);
                        content = "";
                        startEntry = true;
                    }

                    while ((line = in.readLine()) != null && !line.equals("-----"))
                        content += line;
                                                                       
                    if (line != null) {
                        entry.setContent(content);
                        entry.setScriptures(parseScripture(content));
                        entry.setTopics(parseTopics(content));
                        result.add(entry);
                        Thread.sleep(500);
                        updater.update(entry);
                        entry = new Entry();
                        content = "";
                        startEntry = false;
                    } 
                } else if ((line = in.readLine()) != null) {
                } else {
                    break;
                }
            }
            
            //Add the last entry if needed
            if (startEntry == true) {
                entry.setContent(content);
                entry.setScriptures(parseScripture(content));
                entry.setTopics(parseTopics(content));
                result.add(entry);
                updater.update(entry);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error opening file: " + filePath);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }      
        
        return result;
    }
    
    /**
     * sets the filePath to be parsed 
     * @param filePath 
     */
    void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    /**
     * returns the value of the current filePath to be parsed
     * @return filePath
     */
    String getFilePath() {
        return filePath;
    }
}

