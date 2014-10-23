/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.SwingUtilities;
import scripturefinder.Entry;
import scripturefinder.Journal;

/**
 *
 * @author Admin
 */
public class scriptureApp extends Application {
    
    private Journal journal;
    private TextArea txtContent = new TextArea();
    private TextArea txtTerminal = new TextArea();
    private TextArea txtAddEntry = new TextArea();
    private VBox leftMenu = new VBox();
    private VBox centerMenu = new VBox();
    private VBox rightMenu = new VBox();
    
    @Override
    public void start(Stage primaryStage) {
        journal = new Journal();
        BorderPane root = new BorderPane();
        txtTerminal = new TextArea();
        leftMenu = new VBox();
        centerMenu = new VBox();
        rightMenu = new VBox();
        root.setLeft(leftMenu);
        root.setCenter(centerMenu);
        root.setBottom(txtTerminal);
        root.setRight(rightMenu);
        
        
        /* txtTerminal/Bottom Menu */
        txtTerminal.setEditable(false);
        redirectSystemStreams();
        
        /* LeftMenu Items */
        
        //load txt file
        Button btnLoadJournal = new Button();
        btnLoadJournal.setText("Load Journal");
        btnLoadJournal.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                FileChooser chooser = new FileChooser();
                File file = chooser.showOpenDialog(primaryStage);              
                
                if (file != null) {
                    String fileName = file.getPath();
                    journal.loadFromFile(fileName);

                    String text = "";
                    for (Entry entry : journal.getEntries()) {
                        text += entry.getDate() + "\n";
                        text += entry.getContent() + "\n\n";
                    }
                    
                    txtContent.setText(text);
                }
            }
        });
        leftMenu.getChildren().add(btnLoadJournal);
        
        //load xml file
        Button btnLoadXML = new Button();
        btnLoadXML.setText("Load XML File");
        btnLoadXML.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                FileChooser chooser = new FileChooser();
                File file = chooser.showOpenDialog(primaryStage);              
                
                if (file != null) {
                    String fileName = file.getPath();
                    
                    if (journal.loadFromXMLFile(fileName)) {
                        System.out.println("Loading XML File... success!\n");
                    } else {
                        System.out.println("Error loading XML file \n");
                    }

                    String text = "";
                    for (Entry entry : journal.getEntries()) {
                        text += entry.getDate() + "\n";
                        text += entry.getContent() + "\n\n";
                    }
                    
                    txtContent.setText(text);
                }
            }
        });
        leftMenu.getChildren().add(btnLoadXML);
        
        // save/export xml file
        Button btnSaveAsXML = new Button();
        btnSaveAsXML.setText("SaveAs XML");
        btnSaveAsXML.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                FileChooser chooser = new FileChooser();
                File file = chooser.showSaveDialog(primaryStage);     
                
                if (file != null) {
                    String fileName = file.getPath();
                    
                    if (journal.exportXMLFile(fileName)) {
                        System.out.println("SaveAs XML File success!\n");
                    } else {
                        System.out.println("Error saving XML file.\n");
                    }
                }
            }
        });
        leftMenu.getChildren().add(btnSaveAsXML);
        
        // save/export txt file
        Button btnSaveAsTXT = new Button();
        btnSaveAsTXT.setText("SaveAs TXT");
        btnSaveAsTXT.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                FileChooser chooser = new FileChooser();
                File file = chooser.showSaveDialog(primaryStage);     
                
                if (file != null) {
                    String fileName = file.getPath();
                    
                    if (journal.exportTXTFile(fileName)) {
                        System.out.println("SaveAs TXT File success!\n");
                    } else {
                        System.out.println("Error saving XML file.\n");
                    }
                }
            }
        });
        leftMenu.getChildren().add(btnSaveAsTXT);
        
        /* Center Content */
                
        // Journal entry label
        Label labelJournalEntries = new Label("Journal Entries");
        centerMenu.getChildren().add(labelJournalEntries);
        
        // Journal Entry Content area
        txtContent = new TextArea();
        txtContent.setPrefColumnCount(40);
        txtContent.setPrefRowCount(10);
        centerMenu.getChildren().add(txtContent);
        
        // Create new Entry Label
        Label labelAddEntry = new Label("New Entry");
        centerMenu.getChildren().add(labelAddEntry);
        
        // Create new Entry
        txtAddEntry = new TextArea();
        txtAddEntry.setPrefColumnCount(40);
        txtAddEntry.setPrefRowCount(10);
        centerMenu.getChildren().add(txtAddEntry);
        
        // New Entry Button
        Button btnNewEntry = new Button();
        btnNewEntry.setText("Add Entry");
        btnNewEntry.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Adding Entry");
                String content = txtAddEntry.getText();
                journal.addEntry(content);
                String text = "";
                for (Entry entry : journal.getEntries()) {
                    text += entry.getDate() + "\n";
                    text += entry.getContent() + "\n\n";
                }
                txtContent.setText(text);
                txtAddEntry.clear();
            }
        });
        centerMenu.getChildren().add(btnNewEntry);
        
        
        /* Scene Section */
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Journal App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private void updateTextArea(final String text) {
            txtTerminal.appendText(text);
    }
 
    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
        @Override
        public void write(int b) throws IOException {
          updateTextArea(String.valueOf((char) b));
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
          updateTextArea(new String(b, off, len));
        }

        @Override
        public void write(byte[] b) throws IOException {
          write(b, 0, b.length);
        }
      };

      System.setOut(new PrintStream(out, true));
    }
    
}
