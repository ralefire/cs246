package scripturefinder;

/**
 * This holds an individual scripture including book, chapter, verse start, and verse end
 * @author Cameron Lilly
 */
public class Scripture {
    private String book;
    private int chapter;
    private int verseStart;
    private int verseEnd;

    /**
     * Scripture default constructor
     */
    public Scripture() {
        book = "";
        chapter = 0;
        verseStart = 0;
        verseEnd = 0;
    }
    
    /**
     * get book
     * @return book
     */
    public String getBook() {
        return book;
    }

    /**
     * Set book
     * @param book 
     */
    public void setBook(String book) {
        this.book = book;
    }
    
    /**
     * get chapter
     * @return chapter
     */
    public int getChapter() {
        return chapter;
    }
    
    /**
     * set chapter
     * @param chapter 
     */
    public void setChapter(int chapter) {
        this.chapter = chapter;
    }
    
    /**
     * get verseStart
     * @return verseStart
     */
    public int getVerseStart() {
        return verseStart;
    }
    
    /**
     * verseEnd
     * @return verseEnd
     */
    public int getVerseEnd() {
        return verseEnd;
    }
    
    /**
     * sets both the start and end verses
     * @param start
     * @param end 
     */
    public void setVerses(int start, int end) {
        verseStart = start;
        verseEnd = end;
    }
    
    /**
     * Constructor method
     * @param book
     * @param chapter
     * @param verseStart
     * @param verseEnd 
     */
    public Scripture(String book, int chapter, int verseStart, int verseEnd) {
         this.book = book;
         this.chapter = chapter;
         this.verseStart = verseStart;
         this.verseEnd = verseEnd;
    }

    /**
     * displays scripture in the form Book Chap:vStart-vEnd to the console
     */
    public void display() {
        System.out.print("\t" + book + " " + chapter);
        
        if (verseStart != 0) {
            System.out.print(":" + verseStart);
        }
        
        if (verseStart != verseEnd) {
            System.out.print("-" + verseEnd);
        }
        
        System.out.println();
    }
    
    /**
     * returns a string of the scripture in the form Book Chap:vStart-vEnd to the console
     * @return string of scripture of the form Book Chap:vStart-vEnd
     */
    public String getAsString() {
        String result = (book + " " + chapter);
        
        if (verseStart != 0) {
            result += (":" + verseStart);
        }
        
        if (verseStart != verseEnd) {
            result += ("-" + verseEnd);
        }
        
        result += "\n";
        return result;
    }
}