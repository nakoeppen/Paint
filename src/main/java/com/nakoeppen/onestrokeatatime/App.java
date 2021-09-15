//Nicholas Koeppen
//Main class for pain(t) program
package com.nakoeppen.onestrokeatatime;

//JavaFX General
import javafx.application.Application; //Necessary for JavaFX
import static javafx.application.Application.launch; //Necessary for JavaFX

//JavaFX Input
import javafx.scene.control.*; //Necessary for buttons
import javafx.event.ActionEvent; //Necessary for Action for Buttons
import javafx.event.EventHandler; //Necessary for Action for Buttons

//JavaFX Scene Management/Layouts 
import javafx.scene.Scene; //Seems to be a canvas for JavaFX to 'paint on'
import javafx.scene.layout.BorderPane; //Is a Border Pane Layout for the Scene
import javafx.scene.control.Menu; //Is a Menu for each member of Menu Bar
import javafx.scene.control.MenuBar; //For the Menu Bar
import javafx.stage.Stage; //Goes with Scene?
import javafx.scene.canvas.*; //Imports Canvas to draw on image later
import javafx.stage.Screen; //To position the Canvas on Stage
import javafx.geometry.Rectangle2D; //To position the Canvas on Stage

//JavaFX Image-Related Classes
import javafx.scene.image.*; //Imports Image-related Classes

//JavaFX Drawing Classes
import javafx.scene.control.ColorPicker;

//JavaFX Saving The Image
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;

//File-Related Classes
import javafx.stage.FileChooser; //Allows to pick file from file browser
//import javafx.stage.FileChooser.ExtensionFilter; //Allows to pick ONLY image files
import java.io.File; //Imports File Class

//Exceptions and Logging-Related Classes
import java.io.IOException; //For file exceptions

//Unused imports but potentially needed for future
//import javafx.scene.paint.*;
public class App extends Application {

    protected Image image;
    protected Stage stage;
    protected Canvas canvas;
    protected String filepath;

    //Starts the program
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage other) {
        //Creates Canvas for Image Painting (in future)
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        canvas = new Canvas(screenBounds.getWidth() - 75, screenBounds.getHeight() - 75);

        //Setting the stage
        stage = other;
        stage.setTitle("One Stroke at a Time"); //Program Header
        stage.setMaximized(true);
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(canvas);
        BorderPane root = new BorderPane(scroll); //Creates layout and sets Canvas in center

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
        Menu helpMenu = new Menu("Help", null, about, help);
        MenuBar topMenu = new MenuBar(fileMenu, editMenu, helpMenu);

        //Compiles, finalizes, and shows Layout on Stage
        root.setTop(topMenu); //Sets Menu up top
        stage.setScene(new Scene(root));
        stage.show();
    }

    //Fits the Canvas and Image to Screen (DOES NOT DRAW IMAGE, JUST ADJUSTS DIMENSIONS!)
    private void fitToScreen() {
        double widthRatio = canvas.getWidth() / image.getWidth();
        double heightRatio = canvas.getHeight() / image.getHeight();
        double minRatio = Math.min(widthRatio, heightRatio);

        canvas.setWidth(image.getWidth() * minRatio);
        canvas.setHeight(image.getHeight() * minRatio);
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
    public File getFile(boolean forSave) {
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

    //Paints Image on Canvas with no Aspect Ratio
    private void paintCanvas() {
        resetCanvas();

        //Checks to see if image is greater than max canvas bounds
        if (image.getWidth() > canvas.getWidth() || image.getHeight() > canvas.getHeight()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to have the image fit to your screen?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                fitToScreen();
            } else {
                canvas.setWidth(image.getWidth());
                canvas.setHeight(image.getHeight());
            }
        } else {
            canvas.setWidth(image.getWidth());
            canvas.setHeight(image.getHeight());
        }
        canvas.getGraphicsContext2D().drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight()); //Draws Image
    }

    //Resets Canvas
    private void resetCanvas() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        canvas.setWidth(screenBounds.getWidth() - 75);
        canvas.setHeight(screenBounds.getHeight() - 75);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    //Creates action on ImportImage Button
    private class ImportImage implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            File file = getFile(false);

            if (file != null) {
                image = new Image(file.toURI().toString());
                paintCanvas();
            }

        }

    }

    //Creates action on Fit To Screen Button
    private class FitToScreen implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            resetCanvas();
            fitToScreen();
            canvas.getGraphicsContext2D().drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }

    //Creates action on Clear Canvas Button
    private class ClearCanvas implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            resetCanvas();
        }
    }

    //Creates action on Save Button
    private class SaveImage implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            if (filepath == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You need to create a save file first.", ButtonType.OK, ButtonType.CANCEL);
                alert.showAndWait();
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
