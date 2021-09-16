//Nicholas Koeppen
//For Paint Program
package com.nakoeppen.onestrokeatatime;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Screen;

public class BestCanvas extends Canvas {

    private Image image;

    public BestCanvas(double width, double height) {
        super(width, height);
    }

    //Paints Image on Canvas with no Aspect Ratio
    public void paintCanvas(Image image) {
        resetCanvas();
        this.image = image;
        //Checks to see if image is greater than max canvas bounds
        if (image.getWidth() > this.getWidth() || image.getHeight() > this.getHeight()) {
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
        this.getGraphicsContext2D().drawImage(image, 0, 0, this.getWidth(), this.getHeight()); //Draws Image
    }

    //Resets Canvas
    public void resetCanvas() {
        image = null;
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        this.setWidth(screenBounds.getWidth() - 75);
        this.setHeight(screenBounds.getHeight() - 75);
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
    }

    //Fits the Canvas and Image to Screen (DOES NOT DRAW IMAGE, JUST ADJUSTS DIMENSIONS!)
    public void fitToScreen() {
        double widthRatio = this.getWidth() / image.getWidth();
        double heightRatio = this.getHeight() / image.getHeight();
        double minRatio = Math.min(widthRatio, heightRatio);

        this.setWidth(image.getWidth() * minRatio);
        this.setHeight(image.getHeight() * minRatio);
    }

    //Returns Image
    public Image getImage() {
        return this.image;
    }
}
