//Nicholas Koeppen
//Used as Tab-remake for One Stroke at a Time
package com.nakoeppen.onestrokeatatime;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;

/**
 * @author Nicholas Alexander Koeppen
 * @since 2021-10-06
 */

public class PaintTab extends Tab {

    private String title;
    private Paint paint;

    public PaintTab() {
        this(new Paint(), 0);
    }

    public PaintTab(Paint paint, int index) {
        super("Canvas #" + (index + 1), new ScrollPane(new Group(paint)));
        this.paint = paint;
        this.title = "Canvas #" + index;
    }

    public Paint getPaint() {
        return this.paint;
    } 

    public String getTitle() {
        return this.title;
    }
}
