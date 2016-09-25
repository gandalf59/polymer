package de.lis.fx;

/**
 * Created by dletsch on 08.08.16.
 */

import de.lis.gpx.GpxCreator;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;


public class GpxCreatorUI extends Application {

    public void startOff(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello World");

        GridPane gridPane = new GridPane();
        Scene sceene = new Scene(gridPane, 600, 550);

        ToggleGroup toggleGroup = new ToggleGroup();

        Button btnOutput = new Button();
        RadioButton btnGpx = new RadioButton("GPX");
        btnGpx.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                event.getSource();
            }
        });
        btnGpx.setToggleGroup(toggleGroup);
        btnGpx.setUserData(GpxCreator.OutputType.GPX_ROUTE);
        gridPane.add(btnGpx, 1, 1);
        RadioButton btnKml = new RadioButton("KML");
        btnKml.setToggleGroup(toggleGroup);
        btnKml.setUserData(GpxCreator.OutputType.KML_POLYGON);
        gridPane.add(btnKml, 2, 1);

        toggleGroup.selectToggle(btnGpx);

        Label labelTitle = new Label("Titel");
        gridPane.add(labelTitle, 1, 2);

        TextField textFieldTitle = new TextField();
        gridPane.add(textFieldTitle, 2, 2);

        Button btnInput = new Button("CSV Input");
        gridPane.add(btnInput, 1, 3);
        TextField textFieldInput = new TextField();
        textFieldInput.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                FileChooser inFileChooser = new FileChooser();
                inFileChooser.setTitle("Grundstück Datei (.csv) auswählen.");
                inFileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                        new FileChooser.ExtensionFilter("All Files", "*.*"));
                File selectedFile = inFileChooser.showOpenDialog(primaryStage);
                TextField orig = (TextField) event.getSource();
                orig.setUserData(selectedFile);
            }
        });
        gridPane.add(textFieldInput, 2, 3);


        gridPane.add(btnOutput, 1, 4);
        Button btnGenerate = new Button("Generiere GPX");
        gridPane.add(btnGenerate, 1, 5);

        primaryStage.setTitle("Titel");
        primaryStage.setScene(sceene);

        primaryStage.show();

    }

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
                if (header == null || header.isEmpty()) {
                    header = "Generierte GPX Koordinaten";
                    TextInputDialog textInputDialog = new TextInputDialog();
                    textInputDialog.setTitle("Überschrift");
                    textInputDialog.setContentText("Titel");
                    textInputDialog.setHeaderText("Wähle einen Titel");
                    Optional<String> inputHeader = textInputDialog.showAndWait();
                    if (inputHeader.isPresent()) {
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

