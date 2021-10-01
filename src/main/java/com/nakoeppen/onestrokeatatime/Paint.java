//Nicholas Koeppen
//Canvas-remake for One Stroke at a Time
package com.nakoeppen.onestrokeatatime;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
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

public class Paint extends Canvas {

    private Image image;
    public final static int NODRAW = 0, COLORATPOINT = 1, STRAIGHT = 2,
            FREEHAND = 3, ERASER = 4, SQUARE = 5, RECTANGLE = 6,
            ROUNDEDRECTANGLE = 7, POLYGON = 8, ELLIPSE = 9, CIRCLE = 10, TEXT = 11;
    private int lineType, zoom, numberOfPolygonSides;
    private boolean fill, unsavedChanges;
    private Color color;
    private File saveFile;
    private Stack<Image> undo, redo;
    private GraphicsContext gc;

    //Easy Constructor
    public Paint() {
        this(Screen.getPrimary().getVisualBounds().getWidth() - 75,
                Screen.getPrimary().getVisualBounds().getHeight() - 75,
                new Image(new File("src/main/java/files/javafunny.png").toURI().toString()));
    }

    //Constructor
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

        Draw draw = new Draw(this); //Used to draw
    }

    //Paints Image on Canvas with no Aspect Ratio
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
        addToStack();
        this.unsavedChanges = true;
    }

    public void importImage(String filepath) {
        this.image = new Image(filepath);
        if (image != null) {
            drawImageOnCanvas();
        }
    }

    private void importImage(Image image) {
        this.image = image;
        if (image != null) {
            drawImageOnCanvas();
        }
    }

    //Saves Image when file is passed to it
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

    //Resets Canvas
    public void resetCanvas() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        this.setWidth(screenBounds.getWidth() - 75);
        this.setHeight(screenBounds.getHeight() - 75);
        this.gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        addToStack();
        this.unsavedChanges = true;
    }

    //Fits the Canvas and Image to Screen
    public void fitToScreen() {
        resetCanvas();
        double widthRatio = this.getWidth() / this.image.getWidth();
        double heightRatio = this.getHeight() / this.image.getHeight();
        double minRatio = Math.min(widthRatio, heightRatio);

        this.setWidth(this.image.getWidth() * minRatio);
        this.setHeight(this.image.getHeight() * minRatio);

        this.gc.drawImage(this.getImage(), 0, 0, this.getWidth(), this.getHeight());
        addToStack();
        this.unsavedChanges = true;
    }

    //Returns Image
    public Image getImage() {
        return this.image;
    }

    //Gets Line Type based off of Class Constants
    public int getLineType() {
        return this.lineType;
    }

    //Sets Line Type based off of Class Constants
    public void setLineType(int lineType) {
        this.lineType = lineType;
    }

    //Returns Color at Point
    public Color getColor(double x, double y) {
        WritableImage writableImage = new WritableImage((int) this.getWidth(), (int) this.getHeight());
        this.snapshot(null, writableImage); //Takes snapshot of canvas
        return (writableImage.getPixelReader().getColor((int) x, (int) y));
    }

    //Gets Color
    public Color getColor() {
        return this.color;
    }

    //Sets Color
    public void setColor(Color color) {
        this.color = color;
        this.gc.setStroke(this.color);
        this.gc.setFill(this.color);
    }

    //Gets Line Width
    public int getLineWidth() {
        return (int) this.gc.getLineWidth();
    }

    //Sets Line Width
    public void setLineWidth(double width) {
        this.gc.setLineWidth(width / 10);
    }

    //Gets Save Location
    public File getSaveFile() {
        return saveFile;
    }

    //Sets Save Location
    public void setSaveFile(File saveFile) {
        this.saveFile = saveFile;
    }

    //Returns true if there are unsavedChanges
    public boolean getUnsavedChanges() {
        return unsavedChanges;
    }

    //Sets unsavedChanges for Draw class
    public void setUnsavedChanges(boolean unsavedChanges) {
        this.unsavedChanges = unsavedChanges;
    }
    
    //Returns true if there are unsavedChanges
    public int getNumberOfPolygonSides() {
        return this.numberOfPolygonSides;
    }

    //Sets unsavedChanges for Draw class
    public void setNumberOfPolygonSides(int numberOfPolygonSides) {
        this.numberOfPolygonSides = numberOfPolygonSides;
    }
    
    //Returns x array for polygon
    public double[] getXForPolygon(double startX, double startY, double radiusX, double radiusY) {
        double radius = Math.sqrt(Math.pow((startX-radiusX),2)+Math.pow((startY-radiusY),2));
        double[] xarray = new double[this.numberOfPolygonSides];
        for (int i = 0; i < this.numberOfPolygonSides; i++) {
            xarray[i] = radiusX + radius*Math.cos((2*Math.PI*i)/this.numberOfPolygonSides
            + Math.atan2(startY-radiusY, startX-radiusX));
        }
        return xarray;
    }
    
    //Returns y array for polygon
    public double[] getYForPolygon(double startX, double startY, double radiusX, double radiusY) {
        double radius = Math.sqrt(Math.pow((startX-radiusX),2)+Math.pow((startY-radiusY),2));
        double[] yarray = new double[this.numberOfPolygonSides];
        for (int i = 0; i < this.numberOfPolygonSides; i++) {
            yarray[i] = radiusY + radius*Math.sin((2*Math.PI*i)/this.numberOfPolygonSides
            + Math.atan2(startY-radiusY, startX-radiusX));
        }
        return yarray;
    }

    //Returns fill boolean
    public boolean getFill() {
        return this.fill;
    }

    //Sets fill to true or false
    public void toggleFill() {
        this.fill = !fill;
    }

    //Adds to Stack
    public void addToStack() {
        this.redo.push(this.snapshot(null, null));
        this.unsavedChanges = true;
    }

    //Undo action
    public void undo() {
        if (!this.redo.empty()) {
            this.undo.push(this.snapshot(null, null));
            this.importImage(this.redo.pop());
            this.unsavedChanges = true;
        }
    }

    //Redo action
    public void redo() {
        if (!this.undo.empty()) {
            this.redo.push(this.snapshot(null, null));
            this.importImage(this.undo.pop());
            this.unsavedChanges = true;
        }
    }
    //Changes zoom (if true, then +1. If false, then -1

    public void adjustZoom(boolean increment) {
        if (increment) {
            this.setScaleX(++zoom);
            this.setScaleY(zoom);
        } else {
            this.setScaleX(--zoom);
            this.setScaleY(zoom);
        }
    }

}
