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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser class uses regular expressions to search for scripture references
 * from a file, then displays the results found to the user
 */
public class Parser {

    //private String fileName = "C:\\money.txt";

    public void setFileName(String input)
    {
    //    fileName = input;
    }

    public List<Scripture> parseScripture(String fileName) throws IOException {
        
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
        System.out.println("Opening file: " + fileName);

        try {

            //open the file from fileName
            BufferedReader in = new BufferedReader(new FileReader(fileName));

            String line; //used to read the file line by line

            while ((line = in.readLine()) != null) {
                
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
                    i = Integer.getInteger(mNum.group(2));
                } catch (NullPointerException p) {
                    i = 0;
                }
                scripture.setChapter(i);
                try {
                    i = Integer.getInteger(mNum.group(3));
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
                        i = Integer.getInteger(mChap.group(3));
                    } catch (NullPointerException p) {
                        i = 0;
                    }
                    scripture.setChapter(i);
                    scriptureList.add(scripture);
                    scripture = new Scripture();
                  }
            }
        } catch (IOException e) {
                System.out.println("Error opening file: " + fileName);
        }   
        
        return scriptureList;
    }
	
        public List<String> parseTopics(String fileName) throws IOException {
            Properties props = new ReadValidFiles().getProps();
            String termsPath = props.getProperty("validTopicsPath");
            //String scripturesPath = props.getProperty("validScripturesPath");
            
            BufferedReader in = new BufferedReader(new FileReader(termsPath));
            String line; //used to read the file line by line
            List<String> result = new ArrayList();
            Map<String, String[]> tMap = new TreeMap();
            String topics = "";
            while ((line = in.readLine()) != null) {
                String[] lines = line.split(":");
                String temp = lines[1].replaceAll(",", "|");
                tMap.put(lines[0], lines[1].split(","));
                topics += temp;
            }
            
            try {

                //open the file from fileName
                in = new BufferedReader(new FileReader(fileName));
                line = "";
                
                while ((line = in.readLine()) != null) {
                    Scripture scripture = new Scripture();  
                    //contains the regular expression to find scriptures in the form [book] [number]:[number]
                    String regExTopics = "(" + topics + ")";
                    
                    //create pattern objects from the regular expressions
                    Pattern pTop = Pattern.compile(regExTopics);

                    //create matcher object.
                    Matcher mTop = pTop.matcher(line);

                //display the patterns found
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
                }
            } catch (IOException e) {
                    System.out.println("Error opening file: " + fileName);
            }   
         
            for (String s : result) {
                System.out.println(s);
            }
            return result;
        }
    
    public List<Entry> parseFile(String fileName) {
        
        return null;
    }
}

