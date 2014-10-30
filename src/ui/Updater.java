package ui;

import scripturefinder.Entry;

/**
 * interface for objects used to update the GUI
 * @author Cameron Lilly
 */
public interface Updater {
    public void update(Entry input);
    public void resetCounts();
    public void displayDone();
}
