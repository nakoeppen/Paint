//Nicholas Koeppen
//Canvas-remake for One Stroke at a Time
package com.nakoeppen.onestrokeatatime;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javax.imageio.ImageIO;

/**
 * @author Nicholas Alexander Koeppen
 * @since 2021-10-07
 */
public class Paint extends Canvas {
    
    /**
     * These are constant variables to indicate the line type for the Paint object
     */
    public final static int NODRAW = 0, COLORATPOINT = 1, STRAIGHT = 2,
            FREEHAND = 3, ERASER = 4, SQUARE = 5, RECTANGLE = 6,
            ROUNDEDRECTANGLE = 7, POLYGON = 8, ELLIPSE = 9, CIRCLE = 10,
            TEXT = 11, COPYANDPASTE = 12, CUTANDPASTE = 13;
    private int lineType, zoom, numberOfPolygonSides;
    private boolean fill, unsavedChanges;
    private Color color;
    private Image image;
    private File saveFile;
    private Stack<Image> undo, redo;
    private GraphicsContext gc;

    /**
     * Default Constructor
     */
    public Paint() {
        this(Screen.getPrimary().getVisualBounds().getWidth() - 75,
                Screen.getPrimary().getVisualBounds().getHeight() - 75,
                new Image(new File("src/main/java/files/javafunny.png").toURI().toString()));
    }

    /**
     * Constructor which build Paint object ready for immediate use
     *
     * @param width Width of canvas
     * @param height Height of canvas
     * @param image Image that will be drawn on canvas
     */
    public Paint(double width, double height, Image image) {
        super(width, height);
        this.image = image;
        this.lineType = 0;
        this.zoom = 1;
        this.numberOfPolygonSides = 6;
        this.fill = false;
        this.color = Color.BLACK;
        this.redo = new Stack<>();
        this.undo = new Stack<>();
        this.gc = this.getGraphicsContext2D();

        //Draws Default Image on Canvas
        importImage(image);

        //Autosaves image
        TimerTask autosave = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (saveFile != null && saveFile.exists() && unsavedChanges) {
                        saveImage();
                        System.out.println("Image saved");
                    }
                });
            }
        };
        Timer timer = new Timer(); //Timer for autosave
        timer.scheduleAtFixedRate(autosave, 0, ConfigProperties.getAutosaveInterval()); //Autosaves every 1min

        //Draws lines and shapes on canvas
        Draw draw = new Draw(this); //Used to draw
    }

    /**
     * Uses resetCanvas() and then Draws image on Canvas
     */
    private void drawImageOnCanvas() {
        resetCanvas();
        //Checks to see if image is greater than max canvas bounds
        if (this.image.getWidth() > this.getWidth() || this.image.getHeight() > this.getHeight()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to have the image fit to your screen?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                fitToScreen();
            } else {
                this.setWidth(image.getWidth());
                this.setHeight(image.getHeight());
            }
        } else {
            this.setWidth(image.getWidth());
            this.setHeight(image.getHeight());
        }
        this.gc.drawImage(this.image, 0, 0, this.getWidth(), this.getHeight()); //Draws Image
        this.addToStack();
    }

    /**
     * Imports Image from filepath and then draws image on canvas using
     * drawImageOnCanvas()
     *
     * @param filepath location of image to be imported
     */
    public void importImage(String filepath) {
        this.image = new Image(filepath);
        if (image != null) {
            this.drawImageOnCanvas();
        } else {
            System.out.println("Image is null");
        }
    }

    /**
     * Imports image from Image object and then draws image on canvas using
     * drawImageOnCanvas()
     *
     * @param image Image object to be imported
     */
    private void importImage(Image image) {
        this.image = image;
        if (image != null) {
            this.drawImageOnCanvas();
        } else {
            System.out.println("Image is null");
        }
    }

    /**
     * Saves image to a previously specified File, stored as a class variable
     */
    public void saveImage() {
        WritableImage writableImage = new WritableImage((int) this.getWidth(), (int) this.getHeight());
        this.snapshot(null, writableImage); //Takes snapshot of canvas
        BufferedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, new BufferedImage((int) this.getWidth(), (int) this.getHeight(), BufferedImage.TYPE_INT_RGB));

        //Gets File Extension (portable, if future revisions let you save as JPG)
        String fileType = saveFile.getName().substring(saveFile.getName().lastIndexOf('.') + 1);

        //Writes Image to File
        try {
            ImageIO.write(renderedImage, fileType, saveFile);
            this.unsavedChanges = false;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Resets the canvas
     */
    public void resetCanvas() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        this.setWidth(screenBounds.getWidth() - 75);
        this.setHeight(screenBounds.getHeight() - 75);
        this.gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        //this.addToStack();
    }

    /**
     * Fits the image to screen and retains aspect ratio of image
     */
    public void fitToScreen() {
        resetCanvas();
        double widthRatio = this.getWidth() / this.image.getWidth();
        double heightRatio = this.getHeight() / this.image.getHeight();
        double minRatio = Math.min(widthRatio, heightRatio);

        this.setWidth(this.image.getWidth() * minRatio);
        this.setHeight(this.image.getHeight() * minRatio);

        this.gc.drawImage(this.getImage(), 0, 0, this.getWidth(), this.getHeight());
        //this.addToStack();
    }

    /**
     * Returns a portion of the canvas stored as an Image
     *
     * @param startX starting x-coordinate
     * @param startY starting y-coordinate
     * @param width width of bound area
     * @param height height of bound area
     * @return Image Object of specified area
     */
    public Image getImageFromBounds(int startX, int startY, int width, int height) {
        WritableImage croppedImage = new WritableImage(this.snapshot(null, null).getPixelReader(), startX, startY, width, height);
        return croppedImage;
    }

    /**
     * Adds revisions to the canvas to an undo stack
     */
    public void addToStack() {
        WritableImage prev = new WritableImage((int)this.getWidth(), (int)this.getHeight());
        this.snapshot(null, prev);
        this.undo.push(prev);
        //redo.clear();
    }

    /**
     * Undo the previous revision
     */
    public void undo() {
        if (!this.undo.empty()) { //If undo stack is not empty
            WritableImage snapshot = new WritableImage((int)this.getHeight(), (int)this.getWidth());
            this.snapshot(null, snapshot);
            Image lastEditImage = undo.pop();
            this.gc.drawImage(lastEditImage, 0, 0); //Draws Image
            redo.push(snapshot);
            this.unsavedChanges = true;
        }
    }

    /**
     * Redo the previous undone revision
     */
    public void redo() {
        if (!this.redo.empty()) { //If redo stack is not empty
            WritableImage snapshot = new WritableImage((int)this.getHeight(), (int)this.getWidth());
            this.snapshot(null, snapshot);
            Image lastEditImage = redo.pop();
            this.gc.drawImage(lastEditImage, 0, 0); //Draws Image
            undo.push(snapshot);
            this.unsavedChanges = true;
        }
    }

    /**
     * Changes zoom via positive or negative increment
     *
     * @param increment if true, then +1, else -1
     */
    public void adjustZoom(boolean increment) {
        if (increment) {
            this.setScaleX(++zoom);
            this.setScaleY(zoom);
        } else {
            this.setScaleX(--zoom);
            this.setScaleY(zoom);
        }
    }

    /**
     * Returns the Image (without revisions)
     *
     * @return Image Object of the imported image
     */
    public Image getImage() {
        return this.image;
    }

    /**
     * Returns the drawing type as constant of Paint class (line, shape, etc.)
     *
     * @return current selected drawing type as a constant int of Paint class
     */
    public int getLineType() {
        return this.lineType;
    }

    /**
     * Sets the drawing type from Paint class static constants
     * @param lineType Paint class constant indicating what drawing type
     */
    public void setLineType(int lineType) {
        this.lineType = lineType;
    }

    /**
     * Returns Color at given point
     * @param x x-coordinate of point
     * @param y y-coordinate of point
     * @return color at given point
     */
    public Color getColor(double x, double y) {
        WritableImage writableImage = new WritableImage((int) this.getWidth(), (int) this.getHeight());
        this.snapshot(null, writableImage); //Takes snapshot of canvas
        return (writableImage.getPixelReader().getColor((int) x, (int) y));
    }

    /**
     * Returns the currently selected drawing color
     * @return currently selected color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Sets the drawing color
     * @param color new color to draw with
     */
    public void setColor(Color color) {
        this.color = color;
        this.gc.setStroke(this.color);
        this.gc.setFill(this.color);
    }

    /**
     * Returns line width as integer
     * @return line width as integer
     */
    public int getLineWidth() {
        return (int) this.gc.getLineWidth();
    }

    /**
     * Sets the line width
     * @param width new line width as double
     */
    public void setLineWidth(double width) {
        this.gc.setLineWidth(width / 10);
    }

    /**
     * Returns the save file and its location as a File object
     * @return File object to which the canvas is saved
     */
    public File getSaveFile() {
        return saveFile;
    }

    /**
     * Sets the save file and its location
     * @param saveFile new save file for canvas
     */
    public void setSaveFile(File saveFile) {
        this.saveFile = saveFile;
    }

    /**
     * Returns true if there are unsavedChanges
     * @return true if there are unsavedChanges
     */
    public boolean getUnsavedChanges() {
        return unsavedChanges;
    }

    /**
     * Sets unsavedChanges for Draw class
     * @param unsavedChanges new boolean for unsaved changes (true if present)
     */
    public void setUnsavedChanges(boolean unsavedChanges) {
        this.unsavedChanges = unsavedChanges;
    }

    /**
     * Returns number of polygon sides
     * @return number of polygon sides
     */
    public int getNumberOfPolygonSides() {
        return this.numberOfPolygonSides;
    }

    /**
     * Sets the number of polygon sides
     * @param numberOfPolygonSides new number of polygon sides
     */
    public void setNumberOfPolygonSides(int numberOfPolygonSides) {
        this.numberOfPolygonSides = numberOfPolygonSides;
    }

    /**
     * Returns a x-coordinate array for polygon
     * @param centerX Center x-coordinate
     * @param centerY Center y-coordinate
     * @param endX End x-coordinate
     * @param endY End y-coordinate
     * @return x-coordinate array for polygon
     */
    public double[] getXForPolygon(double centerX, double centerY, double endX, double endY) {
        double radius = Math.sqrt(Math.pow((endX - centerX), 2) + Math.pow((endY - centerY), 2));
        double[] xarray = new double[this.numberOfPolygonSides];
        for (int i = 0; i < xarray.length; i++) {
            xarray[i] = centerX + radius * Math.cos((2 * Math.PI * i) / this.numberOfPolygonSides
                    + Math.atan2(endY - centerY, endX - centerX));
        }
        return xarray;
    }

    /**
     * Returns a y-coordinate array for polygon
     * @param centerX Center x-coordinate
     * @param centerY Center y-coordinate
     * @param endX End x-coordinate
     * @param endY End y-coordinate
     * @return y-coordinate array for polygon
     */
    public double[] getYForPolygon(double centerX, double centerY, double endX, double endY) {
        double radius = Math.sqrt(Math.pow((endX - centerX), 2) + Math.pow((endY - centerY), 2));
        double[] yarray = new double[this.numberOfPolygonSides];
        for (int i = 0; i < yarray.length; i++) {
            yarray[i] = centerY + radius * Math.sin((2 * Math.PI * i) / this.numberOfPolygonSides
                    + Math.atan2(endY - centerY, endX - centerX));
        }
        return yarray;
    }

    /**
     * Returns true if fill is true
     * @return true if fill is true
     */
    public boolean getFill() {
        return this.fill;
    }

    /**
     * Toggles fill boolean (if true, false, etc.)
     */
    public void toggleFill() {
        this.fill = !fill;
    }
}
