/**
 * Milestone 1 - basic File I/O (1_FileIO)
 * @author Cameron Lilly
 * Collaboration: Bryce Call helped explain what static meant in Java
 */

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

/**
 * Parser class uses regular expressions to search for scripture references
 * from a file, then displays the results found to the user
 */
public class Parser {

    //private String fileName = "C:\\money.txt";

//    public void setFileName(String input)
//    {
//        fileName = input;
//    }

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
        //System.out.println("Opening file: " + fileName);

            //open the file from fileName
            
            String line = input; //used to read the file line by line
                                     
              Scripture scripture = new Scripture();  
              //contains the regular expression to find scriptures in the form [book] [number]:[number]
              String regExBookNumNum = "(" + book + ")( (\\d{1,3}):(\\d{1,3}))";
              
              //contains the regular expression for scriptures of the form [book] chapter [number]
              String regExBookChapterBook = "((" + book + ") chapter( \\d))";

              //create pattern objects from the regular expressions
              Pattern pNum = Pattern.compile(regExBookNumNum);
              Pattern pChap = Pattern.compile(regExBookChapterBook);

              //create matcher object.
              Matcher mNum = pNum.matcher(line);
              Matcher mChap = pChap.matcher(line);

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
                    } catch (NullPointerException p) {
                        i = 0;
                    } catch (NumberFormatException ex) {
                        i = 0;
                    }
                    scripture.setChapter(i);
                    scriptureList.add(scripture);
                    scripture = new Scripture();
                  }  
        
//        for (Scripture s : scriptureList) {
//            s.display();
//        }      
              
        return scriptureList;
    }
	
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
                //System.out.println("Found: " + mNum.group(1) + mNum.group(2) + mNum.group(3));
                for (String key : tMap.keySet()) {
                    String[] values = tMap.get(key);
                    for (String v : values) {
                        if (v.matches(mTop.group()))
                            result.add(key);
                    }
                }
              }  

//        for (String s : result) {
//            System.out.println(s);
//        }
        return result;
    }
    
    public List<Entry> parseFile(String fileName) {
        List<Entry> result = new ArrayList();
        Entry entry = new Entry();
        Date date = new Date(1234);
        String content = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String line; //used to read the file line by line
        
            Boolean startEntry = false; //Used to know when a new entry should be added
            while ((line = in.readLine()) != null) {
                if (line.equals("-----")) {
                        if (startEntry == false) {
                            startEntry = true;
                        } else {
                            startEntry = false;
                            entry.setContent(content);
                            entry.setScriptures(parseScripture(content));
                            entry.setTopics(parseTopics(content));
                            result.add(entry);
                            entry = new Entry();
                            content = "";
                        }
                    line = in.readLine();
                    if (line != null) {
                        date = date.valueOf(line);
                        entry.setDate(date);
                    } else {
                       break;
                    }
                } else {
                    content += line;
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
            System.out.println("Error opening file: " + fileName);
        } catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }      
        
        return result;
    }
}

