package com.migeleiei.imagesresizer.model;

import javafx.scene.layout.StackPane;

/**
 *
 * Author  MigelEiEi  or Seksan Jomchanasuk and .....
 * For collect list of image Buffer after calulation
 */
public class ImageViewWithPosition {

    private  StackPane stackPane;
    private  int columnIndex;
    private int rowIndex;

    public StackPane getStackPane() {
        return stackPane;
    }

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
}
