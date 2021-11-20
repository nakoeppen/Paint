//Nicholas Koeppen
//Used to draw on canvas for One Stroke at a Time
package com.nakoeppen.onestrokeatatime;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * @author Nicholas Alexander Koeppen
 * @since 2021-10-06
 */
public class Draw {

    private int startX, startY, endX, endY;
    private Image pasteImage = null;

    /**
     * @param paint The Paint object for which this class is drawing
     */
    public Draw(Paint paint) {

        //Sets Mouse Listener for Drawing
        paint.setOnMousePressed((event) -> {
            if (paint.getLineType() != Paint.COPYANDPASTE && paint.getLineType() != Paint.CUTANDPASTE) {
                pasteImage = null;
            }
            if (paint.getLineType() != Paint.NODRAW) { //Does not redraw
                startX = (int) event.getX();
                startY = (int) event.getY();
                paint.getGraphicsContext2D().beginPath();
                paint.getGraphicsContext2D().moveTo(startX, startY);
                if (paint.getLineType() <= 4) { //If it is not a shape
                    if (paint.getLineType() == Paint.COLORATPOINT) { //If
                        paint.setColor(paint.getColor(startX, startY));
                    } else {
                        paint.getGraphicsContext2D().stroke();
                    }
                }
                paint.addToStack();
            }

        });
        paint.setOnMouseDragged((event) -> {
            if (paint.getLineType() == Paint.FREEHAND) { //For Freehand drawing
                paint.getGraphicsContext2D().lineTo(event.getX(), event.getY());
                paint.getGraphicsContext2D().stroke();
            } else if (paint.getLineType() == Paint.ERASER) { //For Freehand drawing
                Color temp = paint.getColor();
                paint.setColor(Color.WHITE);
                paint.getGraphicsContext2D().lineTo(event.getX(), event.getY());
                paint.getGraphicsContext2D().stroke();
                paint.setColor(temp);
            }
        });
        paint.setOnMouseReleased((event) -> {
            endX = (int) event.getX();
            endY = (int) event.getY();
            checkAndSwitchX();
            checkAndSwitchY();
            if (paint.getLineType() > 1) { //If it is not 0 (NODRAW) or 1 (COLORCHOOSER)
                if (paint.getLineType() < 4) { //If it is a line of some sort (STRIAGHT = 2 and FREEHAND = 3)
                    paint.getGraphicsContext2D().lineTo(event.getX(), event.getY());
                    paint.getGraphicsContext2D().stroke();
                } else if (paint.getLineType() == Paint.ERASER) {
                    Color temp = paint.getColor();
                    paint.setColor(Color.WHITE);
                    paint.getGraphicsContext2D().lineTo(endX, endY);
                    paint.getGraphicsContext2D().stroke();
                    paint.setColor(temp);
                } else if (paint.getLineType() == Paint.SQUARE) { //For Square
                    if (paint.getFill()) { //If fill is true
                        paint.getGraphicsContext2D().fillRect(startX, startY, getWidth(), getWidth());
                    } else {
                        paint.getGraphicsContext2D().strokeRect(startX, startY, getWidth(), getWidth());
                    }
                } else if (paint.getLineType() == Paint.RECTANGLE) { //For Rectangle
                    if (paint.getFill()) { //If fill is true
                        paint.getGraphicsContext2D().fillRect(startX, startY, getWidth(), getHeight());
                    } else {
                        paint.getGraphicsContext2D().strokeRect(startX, startY, getWidth(), getHeight());
                    }
                } else if (paint.getLineType() == Paint.ROUNDEDRECTANGLE) { //For Rectangle
                    if (paint.getFill()) { //If fill is true
                        paint.getGraphicsContext2D().fillRoundRect(startX, startY, getWidth(), getHeight(), 20, 20);
                    } else {
                        paint.getGraphicsContext2D().strokeRoundRect(startX, startY, getWidth(), getHeight(), 20, 20);
                    }
                } else if (paint.getLineType() == Paint.POLYGON) { //For Polygon
                    if (paint.getFill()) { //If fill is true
                        paint.getGraphicsContext2D().fillPolygon(paint.getXForPolygon(startX, startY, endX, endY),
                                paint.getYForPolygon(startX, startY, endX, endY),
                                paint.getNumberOfPolygonSides());
                    } else {
                        paint.getGraphicsContext2D().strokePolygon(paint.getXForPolygon(startX, startY, endX, endY),
                                paint.getYForPolygon(startX, startY, endX, endY),
                                paint.getNumberOfPolygonSides());
                    }
                } else if (paint.getLineType() == Paint.ELLIPSE) { //For Ellipse
                    if (paint.getFill()) { //If fill is true
                        paint.getGraphicsContext2D().fillOval(startX, startY, getWidth(), getHeight());
                    } else {
                        paint.getGraphicsContext2D().strokeOval(startX, startY, getWidth(), getHeight());
                    }
                } else if (paint.getLineType() == Paint.CIRCLE) { //For Circle
                    if (paint.getFill()) { //If fill is true
                        paint.getGraphicsContext2D().fillOval(startX, startY, getWidth(), getWidth());
                    } else {
                        paint.getGraphicsContext2D().strokeOval(startX, startY, getWidth(), getWidth());
                    }
                } else if (paint.getLineType() == Paint.TEXT) { //For Text
                    String text = Popup.textPopup("Text Tool", "Please enter what you would like the Canvas text to be... ", null);

                    if (paint.getFill()) { //If fill is true
                        paint.getGraphicsContext2D().fillText(text, endX, endY);
                    } else {
                        paint.getGraphicsContext2D().strokeText(text, endX, endY);
                    }
                } else if (paint.getLineType() == Paint.COPYANDPASTE && pasteImage == null) { //For Copy and Paste (when copy part hasn't been done yet)
                    pasteImage = paint.getImageFromBounds(startX, startY, this.getWidth(), this.getHeight());
//                    paint.setOnMouseReleased((imageDrop) -> {
//                        paint.getGraphicsContext2D().drawImage(image, imageDrop.getX(), imageDrop.getY());
//                    });
                } else if (paint.getLineType() == Paint.CUTANDPASTE && pasteImage == null) { //For Cut and Paste (when cut part hasn't been done yet)
                    pasteImage = paint.getImageFromBounds(startX, startY, this.getWidth(), this.getHeight());
                    Color temp = paint.getColor();
                    paint.setColor(Color.WHITE);
                    paint.getGraphicsContext2D().fillRect(startX, startY, getWidth(), getHeight());
                    paint.setColor(temp);
//                    paint.setOnMouseReleased((imageDrop) -> {
//                        paint.getGraphicsContext2D().drawImage(image, imageDrop.getX(), imageDrop.getY());
//                    });
                } else if (pasteImage != null) {
                    paint.getGraphicsContext2D().drawImage(pasteImage, endX, endY);
                }

            }
        });
    }

    /**
     * Gets width of drawing area
     * @return Width of the drawing area after a click/drag event
     */
    public int getWidth() {
        return endX - startX;
    }

    /**
     * Gets height of drawing area
     * @return Height of the drawing area after a click/drag event
     */
    public int getHeight() {
        return endY - startY;
    }

    /**
     * Switches X if X2 is before X1
     */
    public void checkAndSwitchX() {
        if (this.endX < this.startX) {
            int temp = this.endX;
            this.endX = this.startX;
            this.startX = temp;
        }
    }

    /**
     * Switches Y if Y2 is before Y1
     */
    public void checkAndSwitchY() {
        if (this.endY < this.startY) {
            int temp = this.endY;
            this.endY = this.startY;
            this.startY = temp;
        }
    }
}
