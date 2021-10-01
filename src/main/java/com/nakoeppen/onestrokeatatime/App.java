//Nicholas Koeppen
//Main class for One Stroke at a Time program
package com.nakoeppen.onestrokeatatime;

//JavaFX General
import java.io.File;
import java.io.FileNotFoundException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    private Stage stage;

    //Starts the program
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        //Creates TabPane
        PaintTabPane tabPane = new PaintTabPane(new PaintTab());

        //Setting the stage
        this.stage = stage;
        stage.setTitle("One Stroke at a Time"); //Program Header
        stage.setMaximized(true);
        BorderPane root = new BorderPane(tabPane); //Creates layout and sets paintList in center

        //Creation of Top MenuItems
        //File
        MenuItem importImage = new MenuItem("_Import");
        MenuItem save = new MenuItem("_Save");
        MenuItem saveAs = new MenuItem("Save As...");
        MenuItem quit = new MenuItem("_Quit");
        //Edit
        MenuItem fitToScreen = new MenuItem("Fit Image to Screen");
        MenuItem clearCanvas = new MenuItem("Clear Canvas");
        MenuItem addTab = new MenuItem("Add Tab");
        MenuItem removeTab = new MenuItem("Remove Tab");
        //Help
        MenuItem help = new MenuItem("Help");
        MenuItem releaseNotes = new MenuItem("Release Notes");
        MenuItem about = new MenuItem("About");

        //Sets action of Top MenuItems
        //File
        importImage.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().importImage(FileTools.getFile(false, stage).toURI().toString());
        });
        save.setOnAction((ActionEvent e) -> {
            if (tabPane.getPaintTab().getPaint().getSaveFile() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You need to create a save file first.", ButtonType.OK, ButtonType.CANCEL);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    tabPane.getPaintTab().getPaint().setSaveFile(FileTools.getFile(true, stage));
                    tabPane.getPaintTab().getPaint().saveImage();
                }
            } else {
                tabPane.getPaintTab().getPaint().saveImage();
            }
        });
        saveAs.setOnAction((ActionEvent e) -> {
            File file = FileTools.getFile(true, stage);
            if (file != null) {
                tabPane.getPaintTab().getPaint().setSaveFile(file);
                tabPane.getPaintTab().getPaint().saveImage();
            } else {
                System.out.println("File is null"); //Displays Error
            }
        });
        quit.setOnAction((ActionEvent e) -> {
            quitPaint(tabPane);
        });
        //Edit
        fitToScreen.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().fitToScreen();
        });
        clearCanvas.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().importImage(null);
            tabPane.getPaintTab().getPaint().resetCanvas();
        });
        addTab.setOnAction((ActionEvent e) -> {
            tabPane.addPaintTab(new Paint());
        });
        removeTab.setOnAction((ActionEvent e) -> {
            tabPane.removePaintTab();
        });
        //Help
        help.setOnAction((ActionEvent e) -> {
            try {
                Popup.infoPopup(new File("src/main/java/files/help.txt"), "Help");
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        releaseNotes.setOnAction((ActionEvent e) -> {
            try {
                Popup.infoPopup(new File("src/main/java/files/RELEASE-NOTES.txt"), "Help");
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        about.setOnAction((ActionEvent e) -> {
            try {
                Popup.infoPopup(new File("src/main/java/files/about.txt"), "About");
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        //Creates and compiles the Top Menu Bar
        Menu fileMenu = new Menu("_File", null, importImage, saveAs, save, quit);
        Menu editMenu = new Menu("Edit", null, fitToScreen, clearCanvas, addTab, removeTab);
        Menu helpMenu = new Menu("Help", null, help, releaseNotes, about);
        MenuBar topMenu = new MenuBar(fileMenu, editMenu, helpMenu);

        //Change Color Feature
        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.getStyleClass().add("button");
        colorPicker.setMaxWidth(150);
        colorPicker.setOnAction((event) -> {
            tabPane.getPaintTab().getPaint().setColor(colorPicker.getValue());
        });

        //Creates Buttons for sidebar
        Button chooseColorAtPoint = new Button("Color from Point");
        chooseColorAtPoint.setMaxWidth(150);
        Button noDraw = new Button("No Drawing");
        noDraw.setMaxWidth(150);
        Button drawStraight = new Button("Straight");
        drawStraight.setMaxWidth(150);
        Button drawFreehand = new Button("Freehand");
        drawFreehand.setMaxWidth(150);
        Button eraser = new Button("Eraser");
        eraser.setMaxWidth(150);
        Button drawSquare = new Button("Square");
        drawSquare.setMaxWidth(150);
        Button drawRectangle = new Button("Rectangle");
        drawRectangle.setMaxWidth(150);
        Button drawRoundedRectangle = new Button("Rounded Rectangle");
        drawRoundedRectangle.setMaxWidth(150);
        Button drawPolygon = new Button("Polygon");
        drawPolygon.setMaxWidth(150);
        Button drawEllipse = new Button("Ellipse");
        drawEllipse.setMaxWidth(150);
        Button drawCircle = new Button("Circle");
        drawCircle.setMaxWidth(150);
        Button drawText = new Button("Text");
        drawText.setMaxWidth(150);
        Button fill = new Button("Fill");
        fill.setMaxWidth(150);
        Button zoomIn = new Button("Zoom In");
        zoomIn.setMaxWidth(150);
        Button zoomOut = new Button("Zoom Out");
        zoomOut.setMaxWidth(150);
        Button undo = new Button("Undo");
        undo.setMaxWidth(150);
        Button redo = new Button("Redo");
        redo.setMaxWidth(150);

        //Sets Action for Buttons on Sidebar
        chooseColorAtPoint.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.COLORATPOINT);
        });
        noDraw.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.NODRAW);
        });
        drawStraight.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.STRAIGHT);
        });
        drawFreehand.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.FREEHAND);
        });
        eraser.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.ERASER);
        });
        drawSquare.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.SQUARE);
        });
        drawRectangle.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.RECTANGLE);
        });
        drawRoundedRectangle.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.ROUNDEDRECTANGLE);
        });
        drawPolygon.setOnAction((ActionEvent e) -> {

            tabPane.getPaintTab().getPaint().setLineType(Paint.POLYGON);
        });
        drawEllipse.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.ELLIPSE);
        });
        drawCircle.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.CIRCLE);
        });
        drawText.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.TEXT);
        });
        fill.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().toggleFill();
        });
        zoomIn.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().adjustZoom(true);
        });
        zoomOut.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().adjustZoom(false);
        });
        undo.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().undo();
        });
        redo.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().redo();
        });

        //Uses Slider to change Line Width
        Label lineWidthLabel = new Label("Change Line Width");
        lineWidthLabel.setMaxWidth(150);
        Slider lineWidthSlider = new Slider(10, 300, 10);
        lineWidthSlider.setMaxWidth(150);

        //Adds action to slider value change
        lineWidthSlider.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            tabPane.getPaintTab().getPaint().setLineWidth((double) newValue);
        });

        //Creates a Vertical Toolbar
        ToolBar sidebar = new ToolBar(colorPicker, lineWidthLabel,
                lineWidthSlider, chooseColorAtPoint, noDraw, drawStraight,
                drawFreehand, eraser, drawSquare, drawRectangle,
                drawRoundedRectangle, drawPolygon, drawEllipse, drawCircle, 
                drawText, fill, zoomIn, zoomOut, undo, redo);
        sidebar.setOrientation(Orientation.VERTICAL);

        //Compiles, finalizes, and shows Layout on Stage
        root.setTop(topMenu); //Sets Top Menu on Top
        root.setLeft(sidebar); //Sets Side Menu on Left
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest((event) -> { //Makes stage do Quit procedure on close event
            quitPaint(tabPane);
        });
        stage.show();
    }

    //Quits Paint
    public void quitPaint(PaintTabPane tabPane) {
        for (int i = 0; i < tabPane.getTabs().size(); i++) {
            if (tabPane.getPaintTab(i).getPaint().getUnsavedChanges()) {
                Alert alert = new Alert(Alert.AlertType.NONE, "Canvas #" + (i + 1) + " has unsaved changes. Would you like to save it?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    if (tabPane.getPaintTab(i).getPaint().getSaveFile() == null) {
                        tabPane.getPaintTab(i).getPaint().setSaveFile(FileTools.getFile(true, stage));
                    }
                    tabPane.getPaintTab(i).getPaint().saveImage();
                }
            }
        }
        System.exit(0);
    }
}
