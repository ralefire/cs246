/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripturefinder;

/**
 *
 * @author Admin
 */
public class Scripture {
    private String book;
    private int chapter;
    private int verseStart;
    private int verseEnd;

    public Scripture() {
        book = "";
        chapter = 0;
        verseStart = 0;
        verseEnd = 0;
    }
    
    public String getBook() {
        return book;
    }

    public void setBook(String input) {
        book = input;
    }
    
    public int getChapter() {
        return chapter;
    }
    
    public void setChapter(int input) {
        chapter = input;
    }
    
    public int getVerseStart() {
        return verseStart;
    }
    
     public int getVerseEnd() {
        return verseEnd;
    }
    
    public void setVerses(int start, int end) {
        verseStart = start;
        verseEnd = end;
    }
    
    public Scripture(String iBook, int iChapter, int iStart, int iEnd) {
         book = iBook;
         chapter = iChapter;
         verseStart = iStart;
         verseEnd = iEnd;
    }

    public void display() {
        System.out.print("\t" + book + " " + chapter + ":" + verseStart);
        
        if (verseStart != verseEnd) {
        System.out.print("-" + verseEnd);
        }
        
        System.out.println();
    }
}