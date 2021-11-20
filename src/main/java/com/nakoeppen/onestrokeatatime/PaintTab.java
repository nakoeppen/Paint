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

    /**
     * Default Constructor
     */
    public PaintTab() {
        this(new Paint(), 0);
    }

    /**
     * Constructor with index variable
     * @param paint Paint object stored within PaintTab
     * @param index Index of tab
     */
    public PaintTab(Paint paint, int index) {
        super("Canvas #" + (index + 1), new ScrollPane(new Group(paint)));
        this.paint = paint;
        this.title = "Canvas #" + index;
    }

    /**
     * Returns the Paint object stored within PaintTab
     * @return Paint object stored within PaintTab
     */
    public Paint getPaint() {
        return this.paint;
    } 

    /**
     * Returns title of PaintTab
     * @return title of PaintTab
     */
    public String getTitle() {
        return this.title;
    }
}
