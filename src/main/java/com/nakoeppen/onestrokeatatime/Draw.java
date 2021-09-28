//Nicholas Koeppen
//Used to draw on canvas for One Stroke at a Time
package com.nakoeppen.onestrokeatatime;

import java.util.Optional;
import javafx.scene.control.TextInputDialog;

public class Draw {

    private double startX, startY;

    public Draw(Paint paint) {
        //Sets Mouse Listener for Drawing
        paint.setOnMousePressed((event) -> {
            if (paint.getLineType() != Paint.NODRAW) {
                paint.getGraphicsContext2D().beginPath();
                paint.getGraphicsContext2D().moveTo(event.getX(), event.getY());
                if (paint.getLineType() <= 3) { //If it is not a shape
                    if (paint.getLineType() == Paint.COLORATPOINT) { //If
                        paint.setLineColor(paint.getColor(event.getX(), event.getY()));
                    } else {
                        paint.getGraphicsContext2D().stroke();
                    }
                } else { //If it is a shape
                    startX = event.getX();
                    startY = event.getY();
                }
            }
        });
        paint.setOnMouseDragged((event) -> {
            if (paint.getLineType() == Paint.FREEHAND) { //For Freehand drawing
                paint.getGraphicsContext2D().lineTo(event.getX(), event.getY());
                paint.getGraphicsContext2D().stroke();
            }
        });
        paint.setOnMouseReleased((event) -> {
            if (paint.getLineType() != Paint.NODRAW) {
                if (paint.getLineType() <= 2) { //If it is not a line of some sort
                    paint.getGraphicsContext2D().lineTo(event.getX(), event.getY());
                    paint.getGraphicsContext2D().stroke();
                } else if (paint.getLineType() == Paint.SQUARE) { //For Square
                    if (paint.getFill()) { //If fill is true
                        paint.getGraphicsContext2D().fillRect(startX, startY, event.getX() - startX, event.getX() - startX);
                    } else {
                        paint.getGraphicsContext2D().strokeRect(startX, startY, event.getX() - startX, event.getX() - startX);
                    }
                } else if (paint.getLineType() == Paint.RECTANGLE) { //For Rectangle
                    if (paint.getFill()) { //If fill is true
                        paint.getGraphicsContext2D().fillRect(startX, startY, event.getX() - startX, event.getY() - startY);
                    } else {
                        paint.getGraphicsContext2D().strokeRect(startX, startY, event.getX() - startX, event.getY() - startY);
                    }
                } else if (paint.getLineType() == Paint.ELLIPSE) { //For Ellipse
                    if (paint.getFill()) { //If fill is true
                        paint.getGraphicsContext2D().fillOval(startX, startY, event.getX() - startX, event.getY() - startY);
                    } else {
                        paint.getGraphicsContext2D().strokeOval(startX, startY, event.getX() - startX, event.getY() - startY);
                    }
                } else if (paint.getLineType() == Paint.CIRCLE) { //For Circle
                    if (paint.getFill()) { //If fill is true
                        paint.getGraphicsContext2D().fillOval(startX, startY, event.getX() - startX, event.getX() - startX);
                    } else {
                        paint.getGraphicsContext2D().strokeOval(startX, startY, event.getX() - startX, event.getX() - startX);
                    }
                } else if (paint.getLineType() == Paint.TEXT) { //For Text
                    String text = Popup.textPopup("Text Tool", "Please enter what you would like the Canvas text to be... ", null);
                    
                    if (paint.getFill()) { //If fill is true
                        paint.getGraphicsContext2D().fillText(text, event.getX(), event.getY());
                    } else {
                        paint.getGraphicsContext2D().strokeText(text, event.getX(), event.getY());
                    }
                } 
                paint.setUnsavedChanges(true);
            }
        });
    }
}
