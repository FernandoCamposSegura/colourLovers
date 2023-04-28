package com.svalero.reactive.api.task;

import com.svalero.reactive.api.model.Color;
import com.svalero.reactive.api.service.ColourService;

import io.reactivex.functions.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.ComboBox;

public class FillColorTask extends Task<Integer> {
    ObservableList<String> colorTitles = FXCollections.observableArrayList();
    ComboBox<String> mainSelector;

    public FillColorTask(ComboBox<String> mainSelector){
        this.mainSelector = mainSelector;
    }

    @Override
    protected Integer call() throws Exception {
        Consumer<Color> user = (color) -> {
            colorTitles.add(color.getTitle());
        };
        ColourService colourService = new ColourService();
        colourService.getAllInformation().subscribe(user);
        return null;
    }

    @Override
    protected void succeeded() {
        mainSelector.setItems(colorTitles);
        super.succeeded();
    }
}
