//Nicholas Koeppen
//Main class for One Stroke at a Time program
package com.nakoeppen.onestrokeatatime;

//JavaFX General
import java.io.File;
import java.io.FileNotFoundException;
import java.util.function.UnaryOperator;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.BorderPane;
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
                InfoPopup helpPopup = new InfoPopup(new File("src/main/java/files/help.txt"), "Help");
                helpPopup.show();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        releaseNotes.setOnAction((ActionEvent e) -> {
            try {
                InfoPopup releaseNotesPopup = new InfoPopup(new File("src/main/java/files/RELEASE-NOTES.txt"), "Help");
                releaseNotesPopup.show();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        about.setOnAction((ActionEvent e) -> {
            try {
                InfoPopup aboutPopup = new InfoPopup(new File("src/main/java/files/about.txt"), "About");
                aboutPopup.show();
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
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.getStyleClass().add("button");
        colorPicker.setMaxWidth(150);
        colorPicker.setOnAction((event) -> {
            tabPane.getPaintTab().getPaint().setLineColor(colorPicker.getValue());
        });

        //Creates Buttons for sidebar
        Button noDraw = new Button("No Drawing");
        noDraw.setMaxWidth(150);
        Button drawStraight = new Button("Straight");
        drawStraight.setMaxWidth(150);
        Button drawFreehand = new Button("Freehand");
        drawFreehand.setMaxWidth(150);
        Button drawSquare = new Button("Square");
        drawSquare.setMaxWidth(150);
        Button drawRectangle = new Button("Rectangle");
        drawRectangle.setMaxWidth(150);
        Button drawEllipse = new Button("Ellipse");
        drawEllipse.setMaxWidth(150);
        Button drawCircle = new Button("Circle");
        drawCircle.setMaxWidth(150);
        Button fill = new Button("Fill");
        fill.setMaxWidth(150);

        //Sets Action for Buttons on Sidebar
        noDraw.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.NODRAW);
        });
        drawStraight.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.STRAIGHT);
        });
        drawFreehand.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.FREEHAND);
        });
        drawSquare.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.SQUARE);
        });
        drawRectangle.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.RECTANGLE);
        });
        drawEllipse.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.ELLIPSE);
        });
        drawCircle.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().setLineType(Paint.CIRCLE);
        });
        fill.setOnAction((ActionEvent e) -> {
            tabPane.getPaintTab().getPaint().toggleFill();
        });

        //Creates Text Field and Update Button to Change Width
        TextField lineWidthField = new TextField("Line Width");
        lineWidthField.setMaxWidth(150);
        Button updateLineWidth = new Button("Update Width");
        updateLineWidth.setMaxWidth(150);

        //Makes sure Line Width Text Field is numbers only
        UnaryOperator<Change> integerFilter = change -> {
            String input = change.getText();
            if (input.matches("[0-9]*")) {
                return change;
            }
            return null;
        };

        //Sets action for Line Width Update Button
        updateLineWidth.setOnAction((event) -> {
            lineWidthField.setTextFormatter(new TextFormatter<String>(integerFilter));
            tabPane.getPaintTab().getPaint().setLineWidth(Double.parseDouble(lineWidthField.getText()));
        });

        //Creates a Vertical Toolbar
        ToolBar sidebar = new ToolBar(colorPicker, lineWidthField,
                updateLineWidth, noDraw, drawStraight, drawFreehand,
                drawSquare, drawRectangle, drawEllipse, drawCircle, fill);
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
