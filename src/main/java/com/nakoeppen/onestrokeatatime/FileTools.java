//Nicholas Koeppen
//Used to get files for One Stroke at a Time
package com.nakoeppen.onestrokeatatime;

import java.io.File;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author Nicholas Alexander Koeppen
 * @since 2021-10-06
 */

public class FileTools {

    //Gets File for Image
    public static File getFile(boolean forSave, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png*", "*.jpg", "*.jpeg", "*.bmp", "*.gif"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("GIF", "*.gif")
        );

        File file;

        if (forSave) {
            fileChooser.setTitle("Save Image File");
            file = fileChooser.showSaveDialog(stage);
        } else {
            fileChooser.setTitle("Open Image File");
            file = fileChooser.showOpenDialog(stage);
        }

        //Makes sure that user input an image file, and if no file then verifies that they wanted to do that
        if (file == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No file was input. Would you like to try again?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES && forSave) {
                file = fileChooser.showSaveDialog(stage);
            } else if (alert.getResult() == ButtonType.YES && forSave) {
                file = fileChooser.showOpenDialog(stage);
            }
        }

        return file;
    }
}
