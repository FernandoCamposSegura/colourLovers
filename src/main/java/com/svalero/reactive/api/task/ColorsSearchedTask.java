package com.svalero.reactive.api.task;

import com.svalero.reactive.api.model.Color;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

public class ColorsSearchedTask extends Task<Integer> {
    ObservableList<Color> colors;
    TextArea colorsArea;

    public ColorsSearchedTask(ObservableList<Color> colors, TextArea colorsArea) {
        this.colors = colors;
        this.colorsArea = colorsArea;
    }


    @Override
    protected Integer call() throws Exception {
        StringBuilder sb = new StringBuilder();
        for(Color color : colors) {
            sb.append("Color searched -> " + color.getTitle() + " Hex Code -> " + color.getHex() + "\n");
        }
        colorsArea.setText(sb.toString());
        return null;
    }

    
}
