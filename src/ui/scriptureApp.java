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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import scripturefinder.Entry;
import scripturefinder.Journal;
import scripturefinder.Scripture;

/**
 *
 * @author Admin
 */
public class scriptureApp extends Application {
    
    private TextArea txtContent = new TextArea();
    private TextArea txtTerminal = new TextArea();
    private TextArea txtAddEntry = new TextArea();
    private VBox leftMenu = new VBox();
    private VBox centerMenu = new VBox();
    private VBox bottomMenu = new VBox();
    private VBox rightMenu = new VBox();
    
    @Override
    public void start(Stage primaryStage) {
        
        BorderPane root = new BorderPane();
        txtTerminal = new TextArea();
        leftMenu = new VBox();
        centerMenu = new VBox();
        bottomMenu = new VBox();
        rightMenu = new VBox();
        root.setLeft(leftMenu);
        root.setCenter(centerMenu);
        root.setBottom(bottomMenu);
        root.setRight(rightMenu);
        TextAreaUpdater updater = new TextAreaUpdater();
        updater.setTextArea(txtContent);
        updater.setTerminal(txtTerminal);
        Journal journal = new Journal(updater);
        
        /* Bottom Menu */
        
        // Terminal Label
        // Journal entry label
        Label labelTerminal = new Label("Terminal");
        bottomMenu.getChildren().add(labelTerminal);
        
        // Text Terminal
        txtTerminal.setEditable(false);
        txtTerminal.setPrefRowCount(5);
        bottomMenu.getChildren().add(txtTerminal);
        redirectSystemStreams();
        
        /* LeftMenu Items */
        leftMenu.setStyle("-fx-background-color: #666666");
        leftMenu.setMinWidth(100);
        
        //load txt file
        Button btnLoadJournal = new Button();
        btnLoadJournal.setMinWidth(leftMenu.getMinWidth());
        btnLoadJournal.setText("Load Journal");
        btnLoadJournal.setOnAction((ActionEvent event) -> {
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(primaryStage);
            
            if (file != null) {
                String fileName = file.getPath();
                System.out.println("Loading File...");
                journal.setFilePath(fileName);
                Thread loadTXTFile = new Thread(journal);
                loadTXTFile.start();
                
            }
        });
        leftMenu.getChildren().add(btnLoadJournal);
        
        //load xml file
        Button btnLoadXML = new Button();
        btnLoadXML.setMinWidth(leftMenu.getMinWidth());
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
                        text += entry.getDateAsString() + "\n";
                        text += entry.getContent() + "\n\n";
                    }
                    
                    txtContent.setText(text);
                }
            }
        });
        leftMenu.getChildren().add(btnLoadXML);
        
        // save/export xml file
        Button btnSaveAsXML = new Button();
        btnSaveAsXML.setMinWidth(leftMenu.getMinWidth());
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
        btnSaveAsTXT.setMinWidth(leftMenu.getMinWidth());
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
        
        // centerMenu Styles
        centerMenu.setPadding(new Insets(0, 0, 0, 10));
        
        // Journal entry label
        Label labelJournalEntries = new Label("Journal Entries");
        centerMenu.getChildren().add(labelJournalEntries);
        
        // Journal Entry Content area
        txtContent.setPrefColumnCount(40);
        txtContent.setPrefRowCount(25);
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
                    text += entry.getDateAsString() + "\n";
                    text += entry.getContent() + "\n\n";
                }
                txtContent.setText(text);
                txtAddEntry.clear();
            }
        });
        centerMenu.getChildren().add(btnNewEntry);
        
        /* Right Menu */
        
        // view by... ...Label
        Label labelViewBy= new Label("View Entries By...");
        rightMenu.getChildren().add(labelViewBy);
        
        // Sort by Date Button
        Button btnSortDate = new Button();
        btnSortDate.setText("Date");
        btnSortDate.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Sorting by date.");
                String text = "";
                for (Entry entry : journal.getEntries()) {
                    text += entry.getDateAsString() + "\n";
                    text += entry.getContent() + "\n\n";
                }
                txtContent.setText(text);
            }
        });
        rightMenu.getChildren().add(btnSortDate);
        
        // Sort by Scriptures Button
        Button btnSortScriptures = new Button();
        btnSortScriptures.setText("Scripture");
        btnSortScriptures.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Sorting by scriptures.");
                txtContent.setText(journal.sortByScriptures());
                
//                for (Entry e : journal.getEntries()) {
//                    for (Scripture s : e.getScriptures()) {
//                        System.out.println(s.getAsString());
//                    }
//                }
            }
        });
        rightMenu.getChildren().add(btnSortScriptures);
        
        // Sort by Topic Button
        Button btnSortTopic = new Button();
        btnSortTopic.setText("Topic");
        btnSortTopic.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Sorting by Topic.");
                txtContent.setText(journal.sortByTopics());
            }
        });
        rightMenu.getChildren().add(btnSortTopic);
        
        
        
        
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
