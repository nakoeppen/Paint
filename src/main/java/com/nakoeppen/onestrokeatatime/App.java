//Nicholas Koeppen
//Main class for pain(t) program
package com.nakoeppen.onestrokeatatime;

//JavaFX General
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

//Unused imports but potentially needed for future
//import javafx.scene.paint.*;
public class App extends Application {

    protected Stage stage;
    protected BestCanvas canvas;
    protected String filepath;

    //Starts the program
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage other) {
        //Creates Canvas for Image Painting (in future)
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        canvas = new BestCanvas(screenBounds.getWidth() - 75, screenBounds.getHeight() - 75);

        //Setting the stage
        stage = other;
        stage.setTitle("One Stroke at a Time"); //Program Header
        stage.setMaximized(true);
        ScrollPane canvasScroll = new ScrollPane();
        canvasScroll.setContent(canvas);
        BorderPane root = new BorderPane(canvasScroll); //Creates layout and sets canvas in center

        //Creation of MenuItems
        MenuItem importImage = new MenuItem("Import");
        MenuItem saveAs = new MenuItem("Save As...");
        MenuItem save = new MenuItem("Save"); //Needs Action
        MenuItem quit = new MenuItem("Quit");
        MenuItem fitToScreen = new MenuItem("Fit Image to Screen");
        MenuItem clearCanvas = new MenuItem("Clear Canvas");
        MenuItem about = new MenuItem("About"); //Needs Action
        MenuItem help = new MenuItem("Help"); //Needs Action

        //Sets action of MenuItems
        importImage.setOnAction(new ImportImage());
        save.setOnAction(new SaveImage());
        saveAs.setOnAction(new SaveAs());
        quit.setOnAction(new QuitPaint());
        fitToScreen.setOnAction(new FitToScreen());
        clearCanvas.setOnAction(new ClearCanvas());

        //Creates and compiles the menu bar
        Menu fileMenu = new Menu("File", null, importImage, saveAs, save, quit);
        Menu editMenu = new Menu("Edit", null, fitToScreen, clearCanvas);
        Menu helpMenu = new Menu("Help", null, help, about);
        MenuBar topMenu = new MenuBar(fileMenu, editMenu, helpMenu);

        //Compiles, finalizes, and shows Layout on Stage
        root.setTop(topMenu); //Sets Menu up top
        stage.setScene(new Scene(root));
        stage.show();
    }

    //Saves Image
    private void saveImage(File file) {
        WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, writableImage); //Takes snapshot of canvas
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);

        //Gets File Extension (portable, if future revisions let you save as JPG)
        String fileType = file.getName().substring(file.getName().lastIndexOf('.') + 1);

        //Writes Image to File
        try {
            ImageIO.write(renderedImage, fileType, file);
        } catch (IOException ex) {
            System.out.println("Save Image Error");
            System.exit(-1);
        }
    }

    //Gets Save File for Image
    private File getFile(boolean forSave) {
        FileChooser fileChooser = new FileChooser();
        File file;

        if (forSave) {
            fileChooser.setTitle("Save Image File");

            //Pushes user to select an image-type file
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PNG", "*.png"));

            file = fileChooser.showSaveDialog(stage);
        } else {
            fileChooser.setTitle("Open Image File");

            //Pushes user to select an image-type file
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
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

    //Creates Text Reader Windows
    public void createTextPopup(File file, String title) throws FileNotFoundException {
        //Does the set up for popup
        Stage popup = new Stage();
        BorderPane pane = new BorderPane();
        ScrollPane textScroll = new ScrollPane();
        HBox topMenu = new HBox();
        Scene scene = new Scene(pane, 600, 600);

        //Reads text
        String message = "";
        try ( BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                message += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Text text = new Text(message);

        //Creates close button
        Button close = new Button("Close");
        close.setOnAction((ActionEvent e) -> {
            popup.hide();
        });

        //Compiles and finalizes layout
        topMenu.getChildren().addAll(close);
        //topMenu.setAlignment(Pos.CENTER);
        textScroll.setContent(text);
        pane.setTop(topMenu);
        pane.setCenter(textScroll);
        popup.setScene(scene);
        popup.setTitle(title);
        popup.show();
    }

//Creates action on ImportImage Button
    private class ImportImage implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            File file = getFile(false);

            if (file != null) {
                canvas.paintCanvas(new Image(file.toURI().toString())); //Takes filepath of image, passes it to Image constructor, and paints the canvas with it
            }

        }

    }

//Creates action on Fit To Screen Button
    private class FitToScreen implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            canvas.resetCanvas();
            canvas.fitToScreen();
            canvas.getGraphicsContext2D().drawImage(canvas.getImage(), 0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }

//Creates action on Clear Canvas Button
    private class ClearCanvas implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            canvas.resetCanvas();
        }
    }

//Creates action on Save Button
    private class SaveImage implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            if (filepath == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You need to create a save file first.", ButtonType.OK, ButtonType.CANCEL);
                alert.showAndWait();
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                if (alert.getResult() == ButtonType.OK) {
                    filepath = getFile(true).getAbsolutePath();
                    saveImage(new File(filepath));
                }
            } else {
                saveImage(new File(filepath));
            }
        }
    }

//Creates action on Save As Button
    private class SaveAs implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            File file = getFile(true);
            if (file != null) {
                saveImage(file);
                filepath = file.getAbsolutePath();
            } else {
                System.out.println("File is null"); //Displays Error
            }
        }
    }

//Creates action on Quit Button, and makes sure that user wants to quit
    private class QuitPaint implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Do you want to save the image before you quit?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                if (filepath == null) {
                    filepath = getFile(true).getAbsolutePath();
                }
                saveImage(new File(filepath));
            } else {
                System.exit(0);
            }
        }
    }
}
