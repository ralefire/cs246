/*
 * ScriptureFinder Application used to record and search scriptures in a journal
 * includes main function
 */
package ui;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
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
import scripturefinder.Parser;

/**
 * This is the GUI for the scripture finder application
 * @author Cameron Lilly
 */
public class scriptureApp extends Application {
    
    private final TextArea txtContent = new TextArea();
    private TextArea txtTerminal = new TextArea();
    private TextArea txtAddEntry = new TextArea();
    private VBox leftMenu = new VBox();
    private VBox centerMenu = new VBox();
    private VBox bottomMenu = new VBox();
    private VBox rightMenu = new VBox();
    private int sortType = 0;
    
    /**
     * Load all graphical pieces and set up event listeners
     * @param primaryStage 
     */
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
        
        // Bottom Menu Styles
        bottomMenu.setPadding(new Insets(10, 5, 5, 5));
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
        leftMenu.setStyle("-fx-background-color: #CCD8ED");
        leftMenu.setMinWidth(100);
        
        //load txt file
        Button btnLoadJournal = new Button();
        btnLoadJournal.setMinWidth(leftMenu.getMinWidth());
        btnLoadJournal.setText("Load Journal");
        btnLoadJournal.setOnAction((ActionEvent event) -> {
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(primaryStage);
            txtContent.clear();
            
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
        btnLoadXML.setOnAction((ActionEvent event) -> {
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
        });
        leftMenu.getChildren().add(btnLoadXML);
        
        // save/export xml file
        Button btnSaveAsXML = new Button();
        btnSaveAsXML.setMinWidth(leftMenu.getMinWidth());
        btnSaveAsXML.setText("SaveAs XML");
        btnSaveAsXML.setOnAction((ActionEvent event) -> {
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
        });
        leftMenu.getChildren().add(btnSaveAsXML);
        
        // save/export txt file
        Button btnSaveAsTXT = new Button();
        btnSaveAsTXT.setMinWidth(leftMenu.getMinWidth());
        btnSaveAsTXT.setText("SaveAs TXT");
        btnSaveAsTXT.setOnAction((ActionEvent event) -> {
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
        txtContent.setWrapText(true);
        txtContent.setMinWidth(200);
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
        btnNewEntry.setOnAction((ActionEvent event) -> {
            System.out.println("Adding Entry");
            String content = txtAddEntry.getText();
            journal.addEntry(content);
            
            String text = "";
            Parser parser = new Parser();
            try {
                switch(sortType) {
                    case 1:
                        txtContent.setText(journal.sortByScriptures());
                        break;
                    case 2:
                        txtContent.setText(journal.sortByTopics());
                        break;
                    default:
                        Entry entry = parser.parseContent(content);
                        text += entry.getDateAsString() + "\n";
                        text += entry.getContent() + "\n";
                        txtContent.appendText(text);
                        break;
                }
            } catch (IOException ex) {
                System.out.println("Adding new entry failed.\n");
            }
            
            txtAddEntry.clear();
        });
        centerMenu.getChildren().add(btnNewEntry);
        
        /* Right Menu */
        
        // view by... ...Label
        Label labelViewBy= new Label("View Entries By...");
        rightMenu.getChildren().add(labelViewBy);
        
        // Sort by Date Button
        Button btnSortDate = new Button();
        btnSortDate.setText("Date");
        btnSortDate.setOnAction((ActionEvent event) -> {
            System.out.println("Sorting by date.");
            sortType = 0;
            if (journal.getEntries().isEmpty()) {
                System.out.println("Nothing to sort. Try adding new entries.");
            } else {
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
        btnSortScriptures.setOnAction((ActionEvent event) -> {
            sortType = 1;
            System.out.println("Sorting by scripture");
            if (journal.getEntries().isEmpty()) {
                System.out.println("Nothing to sort. Try adding new entries.");
            } else {
                txtContent.setText(journal.sortByScriptures());
            }
        });
        rightMenu.getChildren().add(btnSortScriptures);
        
        // Sort by Topic Button
        Button btnSortTopic = new Button();
        btnSortTopic.setText("Topic");
        btnSortTopic.setOnAction((ActionEvent event) -> {
            sortType = 2;
            System.out.println("Sorting by topic");
            if (journal.getEntries().isEmpty()) {
                System.out.println("Nothing to sort. Try adding new entries.");
            } else {
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
     * main method
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Takes input text string and appends it to the txtTerminal
     * @param text 
     */
    private void updateTextArea(final String text) {
        txtTerminal.appendText(text);
    }
    
    /**
     * Redirects System streams to update the txtTerminal instead of the console
     * so that it is visible to the user
     */
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
