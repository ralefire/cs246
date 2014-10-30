/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import scripturefinder.Entry;

/**
 *
 * @author Admin
 */
public class TextAreaUpdater implements Updater{
  private TextArea text;
  private TextArea terminal;
  private int countEntries = 0;
  private int countScriptures = 0;
  private int countTopics = 0;

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
    
    public void setTextArea(TextArea text) {
        this.text = text;
    }

    public void setTerminal(TextArea terminal) {
        this.terminal = terminal;
    }
    
    public void countEntriesReset() {
        countEntries = 0;
    }
    
    public void countTopicsReset() {
        countTopics = 0;
    }
    
    public void countScripturesReset() {
        countScriptures = 0;
    }
    
  @Override
    public void resetCounts() {
        countEntries = 0;
        countTopics = 0;
        countScriptures = 0;
    }
    
    public void clear() {
        text.clear();
    }
    
  @Override
    public void displayDone() {
        Platform.runLater(() -> {
            terminal.appendText("Done.\n");
        });
        
    }
}
