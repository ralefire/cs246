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
  private int count = 0;
    
  @Override
    public void update(Entry message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                text.appendText(message.getDate() + "\n");
                text.appendText(message.getContent() + "\n\n");
                count++;
                if (terminal != null)
                   terminal.appendText("\t" + count + " entries loaded\n");
            }
        });
    }
    
    public void setTextArea(TextArea text) {
        this.text = text;
    }

    public void setTerminal(TextArea terminal) {
        this.terminal = terminal;
    }
    
    public void countReset() {
        count = 0;
    }
}
