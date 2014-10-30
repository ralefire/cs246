package ui;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import scripturefinder.Entry;

/**
 * Updates GUI TextArea object
 * @author Cameron Lilly
 */
public class TextAreaUpdater implements Updater {
  private TextArea text;
  private TextArea terminal;
  private int countEntries = 0;
  private int countScriptures = 0;
  private int countTopics = 0;

  /**
   * This takes an entry message and writes the entry data to the GUI
   * It updates the progress of loading a file
   * @param message 
   */
  @Override
    public void update(Entry message) {
        Platform.runLater(() -> {
            text.appendText(message.getDateAsString() + "\n");
            text.appendText(message.getContent() + "\n\n");
            countEntries++;
            
            countScriptures += message.getScriptures().size();
            countTopics += message.getTopics().size();
            
            if (terminal != null) {
                terminal.setText("Loading File...\n"
                        + "\t" + countEntries + " entries loaded\n"
                        + "\t" + countScriptures + " scriptures loaded\n"
                        + "\t" + countTopics + " topics loaded\n");
            }
        });
    }
    
    /**
     * set TextArea
     * @param text 
     */
    public void setTextArea(TextArea text) {
        this.text = text;
    }

    /**
     * set terminal
     * @param terminal 
     */
    public void setTerminal(TextArea terminal) {
        this.terminal = terminal;
    }
    
    /**
     * resets the count of entries loaded to 0
     */
    public void countEntriesReset() {
        countEntries = 0;
    }
    
    /**
     * resets the count of topics loaded to 0
     */
    public void countTopicsReset() {
        countTopics = 0;
    }
    
    /**
     * resets the count of scriptures loaded to 0
     */
    public void countScripturesReset() {
        countScriptures = 0;
    }
    
    /**
     * resets each counter variable to 0
     */
  @Override
    public void resetCounts() {
        countEntries = 0;
        countTopics = 0;
        countScriptures = 0;
    }
    
    /**
     * clear the text area
     */
    public void clear() {
        text.clear();
    }
    
    /**
     * update the GUI to display "Done" on the terminal
     */
  @Override
    public void displayDone() {
        Platform.runLater(() -> {
            terminal.appendText("Done.\n");
        });
        
    }
}
