package de.lis.fx;

/**
 * Created by dletsch on 08.08.16.
 */

import de.lis.gpx.GpxCreator;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;


public class GpxCreatorUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
/*
        final ToggleGroup group = new ToggleGroup();
        RadioButton rb1 = new RadioButton("GPX Route");
        rb1.setToggleGroup(group);
        rb1.setUserData(GpxCreator.OutputType.GPX_ROUTE);
        rb1.setSelected(true);
        RadioButton rb2 = new RadioButton("GPX Track");
        rb2.setUserData(GpxCreator.OutputType.GPX_TRACK);
        rb2.setToggleGroup(group);
        RadioButton rb3 = new RadioButton("GPX Plain");
        rb3.setToggleGroup(group);
        rb3.setUserData(GpxCreator.OutputType.GPX_PLAIN);
        RadioButton rb4 = new RadioButton("KML Polygon");
        rb4.setUserData(GpxCreator.OutputType.KML_POLYGON);
        rb4.setToggleGroup(group);
*/
        final GpxCreator.OutputType[] outputType = {GpxCreator.OutputType.GPX_ROUTE};
        /*
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                outputType[0] = (GpxCreator.OutputType) newValue.getUserData();
            }
        });
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.getChildren().addAll(rb1, rb2, rb3);
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("Selected item:" + outputType[0].name());
        */

        FileChooser inFileChooser = new FileChooser();
        inFileChooser.setTitle("Grundstück Datei (.csv) auswählen.");
        inFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = inFileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            GpxCreator creator = new GpxCreator();
            String inputSource = selectedFile.getAbsolutePath();
            String outputSource = null;

            FileChooser outFileChooser = new FileChooser();
            outFileChooser.setTitle("Ausgabedatei wählen oder anlegen.  Endung:" + (outputType[0].equals(GpxCreator.OutputType.KML_POLYGON) ? ".kml" : "'.gpx'"));
            outFileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("GPX Files", "*.gpx"),
                    new FileChooser.ExtensionFilter("KML Files", "*.kml"));
            selectedFile = outFileChooser.showSaveDialog(null);
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
                creator.createGpx(inputSource, outputSource, outputType[0]);
            }
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

