package scripturefinder;

import java.util.ArrayList;
import java.util.Date;

public class UserInterface {
    String fileName;
    ArrayList<Entry> entries;
    Date date = new Date();
    
    
    public void createGUI() {

    }
    
    public void setEntries(ArrayList<Entry> input) {
        entries = input;
    }
    
    public void setDate(Date input) {
        date = input;
    }

    public ArrayList<Entry> searchEntries(Date date) {
        ArrayList<Entry> results = new ArrayList<>();
        
        return results;
    }

    public ArrayList<Entry> searchEntries(Scripture scripture) {
        ArrayList<Entry> results = new ArrayList<>();

        return results;
    }

    public ArrayList<Entry> searchEntries(String title) {
        ArrayList<Entry> results = new ArrayList<>();

        return results;
    }
}

