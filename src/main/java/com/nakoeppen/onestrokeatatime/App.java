//Nicholas Koeppen
//Main class for One Stroke at a Time program
package com.nakoeppen.onestrokeatatime;

import java.io.File;
import java.io.FileNotFoundException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.logging.Logger;

/**
 * @author Nicholas Alexander Koeppen
 * @since 2021-10-06
 */
public class App extends Application {

    private Stage stage;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    //Starts the program
    public static void main(String[] args) {
        //Reads Preferences from Config File
        ConfigProperties.read();

        //Launches Application
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        //Creates TabPane
        PaintTabPane tabPane = new PaintTabPane(new PaintTab());

        //Setting the stage
        this.stage = stage;
        stage.setTitle("One Stroke at a Time"); //Program Header
        stage.setFullScreen(ConfigProperties.getStartFullscreen());
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
        MenuItem preferences = new MenuItem("Preferences");
        MenuItem about = new MenuItem("About");

        //Sets action of Top MenuItems
        //File
        importImage.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().importImage(FileTools.getFile(false, stage).toURI().toString());
            logger.info("Image imported");
        });
        save.setOnAction((ActionEvent e) -> {
            if (tabPane.getPaintTab().getPaint().getSaveFile() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You need to create a save file first.", ButtonType.OK, ButtonType.CANCEL);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    tabPane.getPaintTab().getPaint().setSaveFile(FileTools.getFile(true, stage));
                    tabPane.getPaintTab().getPaint().saveImage();
                    logger.info("Image saved manually");
                }
            } else {
                tabPane.getPaintTab().getPaint().saveImage();
                logger.info("Image saved manually");
            }
        });
        saveAs.setOnAction((ActionEvent e) -> {
            File file = FileTools.getFile(true, stage);
            if (file != null) {
                tabPane.getPaintTab().getPaint().setSaveFile(file);
                tabPane.getPaintTab().getPaint().saveImage();
                logger.info("Image saved manually");
            } else {
                System.out.println("File is null"); //Displays Error
            }
        });
        quit.setOnAction((ActionEvent e) -> {
            logger.info("Quiting program...");
            quitPaint(tabPane);
        });
        //Edit
        fitToScreen.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().fitToScreen();
            logger.info("Image fit to screen");
        });
        clearCanvas.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().importImage(null);
            tabPane.getPaintTab().getPaint().resetCanvas();
            logger.info("Reset canvas");
        });
        addTab.setOnAction((ActionEvent e) -> {
            tabPane.addPaintTab(new Paint());
            logger.info("Added Paint Tab");
        });
        removeTab.setOnAction((ActionEvent e) -> {
            tabPane.removePaintTab();
            logger.info("Removed Paint Tab");
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
        preferences.setOnAction((ActionEvent e) -> {
            Popup.preferencesPopup();
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
        Menu helpMenu = new Menu("Help", null, help, releaseNotes, preferences, about);
        MenuBar topMenu = new MenuBar(fileMenu, editMenu, helpMenu);

        //Width Constant for Sidebar
        final int SIDEBAR_WIDTH = 150;

        //Change Color Feature
        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.getStyleClass().add("button");
        colorPicker.setMaxWidth(SIDEBAR_WIDTH);
        colorPicker.setOnAction((event) -> {
            tabPane.getPaintTab().getPaint().setColor(colorPicker.getValue());
            logger.info("Color changed");
        });

        //Selected Tool Indicator
        Label selectedTool = new Label("No Draw");
        selectedTool.setMaxWidth(SIDEBAR_WIDTH);

        //Creates Buttons for sidebar
        Button chooseColorAtPoint = new Button("Color from Point");
        chooseColorAtPoint.setTooltip(new Tooltip("Choose Color at Point"));
        chooseColorAtPoint.setMaxWidth(SIDEBAR_WIDTH);
        Button noDraw = new Button("No Drawing");
        noDraw.setTooltip(new Tooltip("No Drawing"));
        noDraw.setMaxWidth(SIDEBAR_WIDTH);
        Button drawStraight = new Button();
        drawStraight.setTooltip(new Tooltip("Draw Straight Line"));
        drawStraight.setGraphic(new ImageView(new File("src/main/java/files/icons/straightline.png").toURI().toString()));
        drawStraight.setMaxWidth(SIDEBAR_WIDTH);
        Button drawFreehand = new Button();
        drawFreehand.setTooltip(new Tooltip("Draw Freehand"));
        drawFreehand.setGraphic(new ImageView(new File("src/main/java/files/icons/freehand.png").toURI().toString()));
        drawFreehand.setMaxWidth(SIDEBAR_WIDTH);
        Button eraser = new Button();
        eraser.setTooltip(new Tooltip("Eraser"));
        eraser.setGraphic(new ImageView(new File("src/main/java/files/icons/eraser.png").toURI().toString()));
        eraser.setMaxWidth(SIDEBAR_WIDTH);
        Button drawSquare = new Button();
        drawSquare.setTooltip(new Tooltip("Draw Square"));
        drawSquare.setGraphic(new ImageView(new File("src/main/java/files/icons/square.png").toURI().toString()));
        drawSquare.setMaxWidth(SIDEBAR_WIDTH);
        Button drawRectangle = new Button();
        drawRectangle.setTooltip(new Tooltip("Draw Rectangle"));
        drawRectangle.setGraphic(new ImageView(new File("src/main/java/files/icons/rectangle.png").toURI().toString()));
        drawRectangle.setMaxWidth(SIDEBAR_WIDTH);
        Button drawRoundedRectangle = new Button();
        drawRoundedRectangle.setTooltip(new Tooltip("Draw Rounded Rectangle"));
        drawRoundedRectangle.setGraphic(new ImageView(new File("src/main/java/files/icons/roundedrectangle.png").toURI().toString()));
        drawRoundedRectangle.setMaxWidth(SIDEBAR_WIDTH);
        Button drawPolygon = new Button();
        drawPolygon.setTooltip(new Tooltip("Draw Polygon"));
        drawPolygon.setGraphic(new ImageView(new File("src/main/java/files/icons/polygon.png").toURI().toString()));
        drawPolygon.setMaxWidth(SIDEBAR_WIDTH);
        Button drawEllipse = new Button();
        drawEllipse.setTooltip(new Tooltip("Draw Ellipse"));
        drawEllipse.setGraphic(new ImageView(new File("src/main/java/files/icons/ellipse.png").toURI().toString()));
        drawEllipse.setMaxWidth(SIDEBAR_WIDTH);
        Button drawCircle = new Button();
        drawCircle.setTooltip(new Tooltip("Draw Circle"));
        drawCircle.setGraphic(new ImageView(new File("src/main/java/files/icons/circle.png").toURI().toString()));
        drawCircle.setMaxWidth(SIDEBAR_WIDTH);
        Button drawText = new Button("Text");
        drawText.setTooltip(new Tooltip("Draw Text"));
        drawText.setMaxWidth(SIDEBAR_WIDTH);
        Button copyAndPaste = new Button("Copy and Paste");
        copyAndPaste.setTooltip(new Tooltip("Copy Image and Paste"));
        copyAndPaste.setMaxWidth(SIDEBAR_WIDTH);
        Button cutAndPaste = new Button("Cut and Paste");
        cutAndPaste.setTooltip(new Tooltip("Cut Image and Paste"));
        cutAndPaste.setMaxWidth(SIDEBAR_WIDTH);
        Button fill = new Button("Fill");
        fill.setTooltip(new Tooltip("Toggle Fill"));
        fill.setMaxWidth(SIDEBAR_WIDTH);
        Button zoomIn = new Button("Zoom In");
        zoomIn.setTooltip(new Tooltip("Zoom In"));
        zoomIn.setMaxWidth(SIDEBAR_WIDTH);
        Button zoomOut = new Button("Zoom Out");
        zoomOut.setTooltip(new Tooltip("Zoom Out"));
        zoomOut.setMaxWidth(SIDEBAR_WIDTH);
        Button undo = new Button("Undo");
        undo.setTooltip(new Tooltip("Undo"));
        undo.setMaxWidth(SIDEBAR_WIDTH);
        Button redo = new Button("Redo");
        redo.setTooltip(new Tooltip("Redo"));
        redo.setMaxWidth(SIDEBAR_WIDTH);

        //Sets Action for Buttons on Sidebar
        chooseColorAtPoint.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.COLORATPOINT);
            selectedTool.setText("Choose Color at Point");
            logger.info("Color at point tool selected");
        });
        noDraw.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.NODRAW);
            selectedTool.setText("No Draw");
            logger.info("No draw (Select) tool selected");
        });
        drawStraight.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.STRAIGHT);
            selectedTool.setText("Straight Line");
            logger.info("Straight line tool selected");
        });
        drawFreehand.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.FREEHAND);
            selectedTool.setText("Freehand/Scribble");
            logger.info("Freehand/Scribble line tool selected");
        });
        eraser.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.ERASER);
            selectedTool.setText("Eraser");
            logger.info("Eraser tool selected");
        });
        drawSquare.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.SQUARE);
            selectedTool.setText("Square");
            logger.info("Square tool selected");
        });
        drawRectangle.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.RECTANGLE);
            selectedTool.setText("Rectangle");
            logger.info("Rectangle tool selected");
        });
        drawRoundedRectangle.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.ROUNDEDRECTANGLE);
            selectedTool.setText("Rounded Rectangle");
            logger.info("Rounded Rectangle tool selected");
        });
        drawPolygon.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.POLYGON);
            selectedTool.setText("Polygon");
            logger.info("Polygon tool selected");
        });
        drawEllipse.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.ELLIPSE);
            selectedTool.setText("Ellipse");
            logger.info("Ellipse tool selected");
        });
        drawCircle.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.CIRCLE);
            selectedTool.setText("Circle");
            logger.info("Circle tool selected");
        });
        drawText.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.TEXT);
            selectedTool.setText("Text");
            logger.info("Text tool selected");
        });
        copyAndPaste.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.COPYANDPASTE);
            selectedTool.setText("Copy and Paste");
            logger.info("Copy and Paste tool selected");
        });
        cutAndPaste.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.CUTANDPASTE);
            selectedTool.setText("Cut and Paste");
            logger.info("Cut and Paste tool selected");
        });
        fill.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().toggleFill();
            logger.info("Fill boolean toggled");
        });
        zoomIn.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().adjustZoom(true);
            logger.info("Zoomed in");
        });
        zoomOut.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().adjustZoom(false);
            logger.info("Zoomed out");
        });
        undo.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().undo();
            logger.info("Action undone");
        });
        redo.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().redo();
            logger.info("Action redone");
        });

        //Uses Slider to change Line Width
        Label lineWidthLabel = new Label("Change Line Width");
        lineWidthLabel.setMaxWidth(SIDEBAR_WIDTH);
        Slider lineWidthSlider = new Slider(10, 300, 10);
        lineWidthSlider.setMaxWidth(SIDEBAR_WIDTH);

        //Adds action to slider value change
        lineWidthSlider.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            tabPane.getPaintTab().getPaint().setLineWidth((double) newValue);
            logger.info("Line width adjusted");
        });

        //Creates a Vertical Toolbar
        ToolBar sidebar = new ToolBar(selectedTool, colorPicker, lineWidthLabel,
                lineWidthSlider, chooseColorAtPoint, noDraw, drawStraight,
                drawFreehand, eraser, drawSquare, drawRectangle,
                drawRoundedRectangle, drawPolygon, drawEllipse, drawCircle,
                drawText, copyAndPaste, cutAndPaste, fill, zoomIn, zoomOut,
                undo, redo);
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

    /**
     * Quits the paint program and asks if user would like to save each one
     * before close
     *
     * @param tabPane a PaintTabPane object used to cycle through Paint objects
     * and save each one before close
     */
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
