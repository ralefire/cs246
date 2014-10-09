/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripturefinder;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class Report {
    
    private int numScriptures;
    private Date lastUpdated;
    private Date currentDate;
    private  int numEntries;
    private int numConsecDays;
    private static Report instance = null;
    
    protected Report() {
    
    }
    
    public static Report getInstance() {
      if(instance == null) {
         instance = new Report();
      }
      return instance;
    }
    
    public void reminder() {
    
    }
    
    public void display() {
    
    }
    
    public int getNumScriptures() {
        return numScriptures;
    }
    
    public void setNumScriptures(int input) {
        numScriptures = input;
    }
    
    public void incNumScriptures() {
        numScriptures++;
    }
    
    public int getNumEntries() {
        return numEntries;
    }
    
    public void setNumEntries(int input) {
       numEntries = input;
    }
    
    public void incNumEntries() {
        numEntries++;
    }
    
    public void setLastUpdated(Date input) {
        lastUpdated = input;
    }
    
}
