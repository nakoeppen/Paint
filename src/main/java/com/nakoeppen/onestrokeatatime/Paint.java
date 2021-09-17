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
            SQUARE = 3, RECTANGLE = 4, ELLIPSE = 5, CIRCLE = 6;
    private boolean fill; 
    private int lineType;
    private String filepath;
    private GraphicsContext gc;

    //Easy Constructor
    public Paint () {
        this(Screen.getPrimary().getVisualBounds().getWidth() - 75, 
                Screen.getPrimary().getVisualBounds().getHeight() - 75,  
                new Image(new File("src/main/java/files/javafunny.png").toURI().toString()));

        Draw draw = new Draw(this);
    }
    
    //Constructor
    public Paint(double width, double height, Image image) {
        super(width, height);
        this.image = image;
        this.lineType = 0;
        this.fill = false;
        this.gc = this.getGraphicsContext2D();

        //Draws Default Image on Canvas
        this.image = image;
        this.drawImageOnCanvas();
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
    }

    public void importImage(String filepath) {
        this.filepath = filepath;
        this.image = new Image(this.filepath);
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
    }

    //Saves Image
    public void saveImage(File file) {
        WritableImage writableImage = new WritableImage((int) this.getWidth(), (int) this.getHeight());
        this.snapshot(null, writableImage); //Takes snapshot of canvas
        BufferedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, new BufferedImage((int) this.getWidth(), (int) this.getHeight(), BufferedImage.TYPE_INT_RGB));

        //Gets File Extension (portable, if future revisions let you save as JPG)
        String fileType = file.getName().substring(file.getName().lastIndexOf('.') + 1);

        //Writes Image to File
        try {
            ImageIO.write(renderedImage, fileType, file);
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
    
    //Returns fill boolean
    public boolean getFill() {
        return this.fill;
    }
    
    //Sets fill to true or false
    public void toggleFill() {
        this.fill = !fill;
    }
}
