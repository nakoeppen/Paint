//Nicholas Koeppen
//Popup Window Class for One Stroke at a Time
package com.nakoeppen.onestrokeatatime;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Nicholas Alexander Koeppen
 * @since 2021-10-06
 */
public class Popup {

    //Used to show a .txt file as a popup
    public static void infoPopup(File file, String title) throws FileNotFoundException {
        //Does the set up for popup
        Stage popupStage = new Stage();
        BorderPane pane = new BorderPane();
        ScrollPane textScroll = new ScrollPane();
        HBox topMenu = new HBox();
        Scene scene = new Scene(pane, 500, 500);

        //Reads text
        String message = "";
        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNext()) {
                message += scan.nextLine() + "\n";
            }
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        Text text = new Text(message);
        text.setFont(new Font("Monospaced", 13));

        //Creates close button
        Button close = new Button("Close");
        close.setOnAction((ActionEvent e) -> {
            popupStage.hide();
        });

        //Compiles and finalizes layout
        topMenu.getChildren().addAll(close);

        textScroll.setContent(text);

        pane.setCenter(textScroll);
        popupStage.setScene(scene);
        popupStage.setTitle(title);
        popupStage.show();
    }

    //Returns text from dialog prompt
    public static String textPopup(String title, String contentText, String defaultText) {
        TextInputDialog textInput = new TextInputDialog(defaultText);
        textInput.setTitle(title);
        textInput.setContentText(contentText);
        Optional<String> text = textInput.showAndWait();
        return text.get();
    }

    //Makes a config popup
    public static void preferencesPopup() {
        //Setup
        Stage popupStage = new Stage();
        GridPane pane = new GridPane();
        Scene scene = new Scene(new ScrollPane(pane), 500, 500);

        //Autosave Interval variable
        Label autosaveLabel = new Label("Autosave Interval: ");
        TextField autosaveField = new TextField(ConfigProperties.getAutosaveInterval() + "");

        //Saves all variables to ConfigProperties class and writes to config file
        Button save = new Button("Save");
        save.setOnAction((event) -> {
            long autosaveInterval = Long.parseLong(autosaveField.getText());
            if (autosaveInterval < 5000 || autosaveInterval > 600000) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You can only enter a number between 5000 (5 seconds)\nand 600000 (10 minutes) for Autosave Interval...", ButtonType.OK);
                alert.showAndWait();
            } else {
                ConfigProperties.setAutosaveInterval(autosaveInterval);
                ConfigProperties.write();
                popupStage.hide();
            }
        });

        //Compiles GridPane
        pane.addRow(0, new Label("Preferences:"));
        pane.addRow(1, autosaveLabel, autosaveField);
        pane.addRow(2, save);

        //Finalizes and compiles popup
        popupStage.setScene(scene);
        popupStage.setTitle("Preferences");
        popupStage.show();
    }
}
