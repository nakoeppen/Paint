//Nicholas Koeppen
//TabPane-remake for One Stroke at a Time
package com.nakoeppen.onestrokeatatime;

import javafx.scene.control.TabPane;

/**
 * @author Nicholas Alexander Koeppen
 * @since 2021-10-06
 */

public class PaintTabPane extends TabPane {

    public PaintTabPane(PaintTab paintTab) {
        this.getTabs().add(paintTab);
    }

    //Adds Tab to TabPane
    public void addPaintTab(Paint paint) {
        PaintTab tab = new PaintTab(paint, this.getSelectionModel().getSelectedIndex()+1);
        this.getTabs().add(tab);
        this.getSelectionModel().select(tab);
    }

    //Removes Tab from TabPane
    public void removePaintTab() {
        this.getTabs().remove(this.getSelectionModel().getSelectedIndex());
    }

    //Returns Paint object in Tab
    public PaintTab getPaintTab() {
        return (PaintTab)this.getTabs().get(this.getSelectionModel().getSelectedIndex());
    }
    
    //Returns Paint object in given index
    public PaintTab getPaintTab(int index) {
        return (PaintTab)this.getTabs().get(index);
    }

    public int getSize() {
        return this.getTabs().size();
    }
}
