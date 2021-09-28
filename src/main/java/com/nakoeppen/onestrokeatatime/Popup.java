//Nicholas Koeppen
//Popup Window Class for One Stroke at a Time
package com.nakoeppen.onestrokeatatime;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
    public static String textPopup(String title, String contentText, String defaultText ) {
        TextInputDialog textInput = new TextInputDialog(defaultText);
        textInput.setTitle(title);
        textInput.setContentText(contentText);
        Optional<String> text = textInput.showAndWait();
        return text.get();
    }
}
