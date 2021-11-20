//Nicholas Koeppen
//TabPane-remake for One Stroke at a Time
package com.nakoeppen.onestrokeatatime;

import javafx.scene.control.TabPane;

/**
 * @author Nicholas Alexander Koeppen
 * @since 2021-10-06
 */

public class PaintTabPane extends TabPane {

    /**
     * Primary Constructor (must contain one PaintTab object preemptively)
     * @param paintTab PaintTab object (first tab in PaintTabPane)
     */
    public PaintTabPane(PaintTab paintTab) {
        this.getTabs().add(paintTab);
    }

    /**
     * Adds Tab to TabPane
     * @param paint Creates a PaintTab and adds it to PaintTabPane
     */
    public void addPaintTab(Paint paint) {
        PaintTab tab = new PaintTab(paint, this.getSelectionModel().getSelectedIndex()+1);
        this.getTabs().add(tab);
        this.getSelectionModel().select(tab);
    }

    /**
     * Removes currently selected PaintTab from PaintTabPane
     */
    public void removePaintTab() {
        this.getTabs().remove(this.getSelectionModel().getSelectedIndex());
    }

    /**
     * Returns Paint object in Tab
     * @return Paint object in Tab
     */
    public PaintTab getPaintTab() {
        return (PaintTab)this.getTabs().get(this.getSelectionModel().getSelectedIndex());
    }
    
    /**
     * Returns Paint object in given index
     * @param index index at which Paint object must be returned
     * @return Paint object in given index
     */
    public PaintTab getPaintTab(int index) {
        return (PaintTab)this.getTabs().get(index);
    }

    /**
     * Returns size of PaintTabPane (amount of PaintTabs)
     * @return size of PaintTabPane (amount of PaintTabs)
     */
    public int getSize() {
        return this.getTabs().size();
    }
}
