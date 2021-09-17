//Nicholas Koeppen
//Used as Tab-remake for One Stroke at a Time
package com.nakoeppen.onestrokeatatime;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;

public class PaintTab extends Tab {

    private Paint paint;
    private String title;
    
    public PaintTab() {
        this(new Paint(), 0);
    }
    
    public PaintTab(Paint paint, int index) {
        super("Canvas #" + (index+1), new ScrollPane(paint));
        this.paint = paint;
        this.title = "Canvas #" + index;
    }
    
    public PaintTab(Paint paint, String title) {
        super(title, new ScrollPane(paint));
        this.paint = paint;
        this.title = title;
    }
    
    public Paint getPaint() {
        return this.paint;
    }
    
    public String getTitle() {
        return this.title;
    }
}