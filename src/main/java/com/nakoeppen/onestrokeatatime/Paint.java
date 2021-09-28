//Nicholas Koeppen
//Canvas-remake for One Stroke at a Time
package com.nakoeppen.onestrokeatatime;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    public final static int NODRAW = 0, STRAIGHT = 1, FREEHAND = 2,
            COLORATPOINT = 3, SQUARE = 4, RECTANGLE = 5, ROUNDEDRECTANGLE = 6,
            ELLIPSE = 7, CIRCLE = 8, TEXT = 9;
    private boolean fill, unsavedChanges;
    private int lineType, zoom;
    private File saveFile;
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
        this.fill = false;
        this.gc = this.getGraphicsContext2D();

        //Draws Default Image on Canvas
        this.image = image;
        this.drawImageOnCanvas();

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
        this.unsavedChanges = true;
    }

    public void importImage(String filepath) {
        this.image = new Image(filepath);
        if (image != null) {
            drawImageOnCanvas();
        }
    }

    //Resets Canvas
    public void resetCanvas() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        this.setWidth(screenBounds.getWidth() - 75);
        this.setHeight(screenBounds.getHeight() - 75);
        this.gc.clearRect(0, 0, this.getWidth(), this.getHeight());
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
        this.unsavedChanges = true;
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

    //Sets Line Color
    public void setLineColor(Color color) {
        this.gc.setStroke(color);
        this.gc.setFill(color);
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

    //Returns fill boolean
    public boolean getFill() {
        return this.fill;
    }

    //Sets fill to true or false
    public void toggleFill() {
        this.fill = !fill;
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
