package de.lis.fx;

/**
 * Created by dletsch on 08.08.16.
 */

import de.lis.gpx.GpxCreator;
import javafx.application.Application;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;


public class GpxCreatorUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Grundstück Datei (.csv) auswählen.");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            GpxCreator creator = new GpxCreator();
            String inputSource = selectedFile.getAbsolutePath();
            String outputSource = null;

            fileChooser.setTitle("Ausgabedatei wählen oder anlegen (Endung: .gpx)" );
            fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("GPX Files", "*.gpx"));
            selectedFile = fileChooser.showSaveDialog(null);
            if (selectedFile != null) {

                String header = System.getProperty("header");
                if ( header == null || header.isEmpty() ) {
                    header = "Generierte GPX Koordinaten";
                    TextInputDialog textInputDialog = new TextInputDialog();
                    textInputDialog.setTitle("Überschrift");
                    textInputDialog.setContentText("Titel");
                    textInputDialog.setHeaderText("Wähle einen Titel");
                    Optional<String> inputHeader = textInputDialog.showAndWait();
                    if ( inputHeader.isPresent() ) {
                        header = inputHeader.get();
                    }
                    System.setProperty("header", header);
                }
                outputSource = selectedFile.getAbsolutePath();
                creator.createGpx(inputSource, outputSource);
            }

            System.exit(0);


            // System.out.println("File " + selectedFile + " selected");
            // primaryStage.(selectedFile);
        }
    }

    public static void main(String[] args) {
        launch(args);


    }
}

