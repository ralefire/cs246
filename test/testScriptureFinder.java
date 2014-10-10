/*
 * Scripture Test Suite
 */
import java.util.ArrayList;
import java.util.Date;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import scripturefinder.Entry;
import scripturefinder.Parser;
import scripturefinder.Scripture;
import scripturefinder.ScriptureFinder;
import scripturefinder.UserInterface;
import scripturefinder.XML;
import scripturefinder.Report;

/**
 *
 * @author Cameron Lilly
 */
public class testScriptureFinder {
    
    public testScriptureFinder() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    
    @Test
    public void scriptureTest() {
            Scripture s = new Scripture("Moses", 1, 39, 0);
            Assert.assertEquals(s.getBook(), "Moses");
            Assert.assertEquals(s.getChapter(), 1);
            Assert.assertEquals(s.getVerseStart(), 39);
            Assert.assertEquals(s.getVerseStart(), 0);
    }
    
    @Test
    public void scriptureGetSetTest() {
            Scripture s = new Scripture();
            s.setBook("Moses");
            s.setChapter(1);
            s.setVerses(39, 0);
            
            Assert.assertEquals(s.getBook(), "Moses");
            Assert.assertEquals(s.getChapter(), 1);
            Assert.assertEquals(s.getVerseStart(), 39);
            Assert.assertEquals(s.getVerseStart(), 0);
    }
    
    @Test
    public void entryTest() {
 /*       Date date = new Date(2014, 5, 29);
        Entry e = new Entry("This is content 2 Nephi 3:7 \n", date, "topic", "The Title");
        
        Assert.assertEquals(e.getDate(), date);
        Assert.assertEquals(e.getContent(), "This is content 2 Nephi 3:7 \n");
        Assert.assertEquals(e.getTopic(), "topic");
        Assert.assertEquals(e.getTitle(), "The Title");
   */ }
   
    @Test
    public void entryGetSetTest() {
        Date date = new Date(2014, 5, 29);
        Entry e = new Entry();
        
        e.setDate(date);
        e.setTitle("The Title");
       // e.setTopic("topic");
        e.setContent("This is content 2 Nephi 3:7 \n\n");
        
        Assert.assertEquals(e.getDate(), date);
        Assert.assertEquals(e.getContent(), "This is content 2 Nephi 3:7 \n\n");
        Assert.assertEquals(e.getTopic(), "topic");
        Assert.assertEquals(e.getTitle(), "The Title");
    }
    
    
    
    
    @Test
    public void parseScriptureTest() {
        Parser e = new Parser();
        ArrayList<Scripture> s = e.parseScripture("I had an amazing thought from Abraham 1:2");
    
        Assert.assertEquals(s.get(0).getBook(), "Abraham");
        Assert.assertEquals(s.get(0).getChapter(), 1);
        Assert.assertEquals(s.get(0).getVerseStart(), 2);
    }
    
    @Test
    public void parseScriptureExceptionTest() {
        Parser e = new Parser();
        ArrayList<Scripture> s = e.parseScripture("I had an amazing thought "
                + "from Abra7ham 1@:2");
    
        Assert.assertEquals(s.get(0).getBook(), null);
    } 
    
    @Test
    public void parseTopicTest() {
        Parser e = new Parser();
       // ArrayList<String> s = e.parseTopics("I had an amazing thought from"
       //         + " Abraham 1:2 about the Holy Ghost @'Holy Ghost'");
    
       // Assert.assertEquals(s.get(0), "Holy Ghost");
    }
    
    @Test
    public void parserValidFileTest() {
        Parser e = new Parser();
        e.setFileName("C:validFile.txt"); //valid file includes scripture
        ArrayList<Scripture> s = e.parseScripture("I had an amazing thought"
                + " from Abraham 1:2");
    
        Assert.assertEquals(s.get(0).getBook(), "Abraham");
    }
    
    @Test
    public void xmlFileReadTest() {
        XML reader = new XML();
        boolean success = reader.importXML("C:myDoc");
        Assert.assertEquals(success, true);
    }
    
    @Test
    public void xmlFileCreateTest() {
        XML reader = new XML();
        boolean success = reader.exportDocx();
        Assert.assertEquals(success, true);
    }
    
    @Test
    public void reportSetGetScriptTest() {
        Report r = Report.getInstance();
        r.setNumScriptures(3);
        Assert.assertEquals(r.getNumScriptures(), 3);
    }
    
    @Test
    public void reportIncTest() {
        Report r = Report.getInstance();
        r.setNumScriptures(3);
        r.incNumScriptures();
        Assert.assertEquals(r.getNumScriptures(), 4);
    }
    
    @Test
    public void reportSetGetNumEntriesTest() {
        Report r = Report.getInstance();
        r.setNumEntries(3);
        Assert.assertEquals(r.getNumEntries(), 3);
    }
    
    @Test
    public void reportIncNumEntriesTest() {
        Report r = Report.getInstance();
        r.setNumEntries(3);
        r.incNumEntries();
        Assert.assertEquals(r.getNumScriptures(), 4);
    }
    
    @Test
    public void userInterfaceSearchTest() {
        UserInterface u = new UserInterface();
        
        Date date = new Date(2014, 5, 29);
      //  Entry e = new Entry("This is content 2 Nephi 3:7 \n", date, "topic", "The Title");
        ArrayList<Entry> list = new ArrayList<>();
    //    list.add(e);
        u.setEntries(list);
        
        Assert.assertEquals(u.searchEntries(date), list);
     //   Assert.assertEquals(u.searchEntries(e.getScriptures().get(0)), list);
        Assert.assertEquals(u.searchEntries("topic"), list);
    }
    
//    Tests that expect exceptions
//    Tests that exercise parsing / searching for scriptures and keywords
//    Tests that check to see that files were created
//    Tests that check to see that a file was read in correctly (for example, test to see that you read in a list of valid topics correctly)

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }
}
