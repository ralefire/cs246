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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser class uses regular expressions to search for scripture references
 * from a file, then displays the results found to the user
 */
public class Parser {

    private String fileName = "C:\\money.txt";

    public void setFileName(String input)
    {
        fileName = input;
    }

    public ArrayList<Scripture> parseScripture() throws IOException {

        /*
        System.out.println("Opening file: " + fileName);

        try {

            //open the file from fileName
            BufferedReader in = new BufferedReader(new FileReader(fileName));

            String line; //used to read the file line by line

            while ((line = in.readLine()) != null) {

                //contains the valid inputs for scripture books
                String book = "((Nephi|Jacob|Enos|Jarom|Omni|Words of Mormon|Mosiah|"
                                + "Alma|Helaman|Mormon|Ether|Moroni|Matthew|Mark|Luke|John|"
                                + "Acts|Romans|Corinthians|Galatians|Ephesians|Philippians"
                                + "|Colossians|Thessalonians|Timothy|Titus|Philemon|Hebrews|"
                                + "James|Peter|Jude|Revelation|Genesis|Exodus|Leviticus|Number"
                                + "|Deuteronomy|Joshua|Judges|Ruth|Samuel|Kings|Chronicles|"
                                + "Ezra|Nehamiah|Esther|Job|Psalms|Proverbs|Ecclesiastes"
                                + "|Song of Solomon|Isaiah|Jeremiah|Lamentations|Ezekiel|Daniel|"
                                + "Hosea|Joel|Amos|Obadiah|Jonah|Micah|Nahum|Habukkuk|"
                                + "Zephaniah|Haggai|Zechariah|Malachi|Moses|Abraham"
                                + "|Joseph Smith History|D\\&C|Doctrine And Covenants)"; 

                  //contains the valid inputs for scriptures that have more than one book
                  //ex: 1 Nephi, 2 Nephi
              String dupBooks = "(Nephi|Corinthians|Thessalonians|Timothy|"
                        + "Peter|John|Samuel|Kings|Chronicles)";

              //contains the regular expression to find scriptures in the form [book] [number]:[number]
              //and [number] [book]
              String regExBookNumNum = book + "( \\d{1,3}:\\d{1,3}))|(\\d " + dupBooks + " \\d{1,3}:\\d{1,3})";

              //contains the regular expression for scriptures of the form [book] chapter [number]
              String regExBookChapterBook = book + " chapter( \\d))";

              //create pattern objects from the regular expressions
              Pattern pNum = Pattern.compile(regExBookNumNum);
              Pattern pChap = Pattern.compile(regExBookChapterBook);

              //create matcher object.
              Matcher mNum = pNum.matcher(line);
              Matcher mChap = pChap.matcher(line);

              //display the patterns found
              while (mNum.find( )) {
                 System.out.println("Found: " + mNum.group() );
              }


              //display the patterns found excluding the word "chapter"
              while (mChap.find( )) {
                     System.out.println("Found: " + mChap.group(2) + mChap.group(3) );
                  }
              
             
        
        
            }
        } catch (IOException e) {
                System.out.println("Error opening file: " + fileName);
        }
                */ 
        
            Properties props = new ReadValidFiles().getProps();
            String scripturesPath = props.getProperty("validScripturesPath");
            
            BufferedReader in = new BufferedReader(new FileReader(scripturesPath));
            String line; //used to read the file line by line
            List<String> validScriptures = new ArrayList<>();

            while ((line = in.readLine()) != null) {
                validScriptures.add(line);
            }

            System.out.println("Checking for valid scriptures file...");
            for (String script : validScriptures) {
                System.out.println(script);
            }
     
        
        return null;
    }
	
        public List<String> parseTopics(String input) throws IOException {
            Properties props = new ReadValidFiles().getProps();
            String termsPath = props.getProperty("validTopicsPath");
            //String scripturesPath = props.getProperty("validScripturesPath");
            
            BufferedReader in = new BufferedReader(new FileReader(termsPath));
            String line; //used to read the file line by line
            List<String> validTopics = new ArrayList<>();
            
            while ((line = in.readLine()) != null) {
                validTopics.add(line);
            }
            
            System.out.println("Checking for valid topics file...");
            for (String topic : validTopics) {
                System.out.println(topic);
            }
            
            return validTopics;
        }
    
}

