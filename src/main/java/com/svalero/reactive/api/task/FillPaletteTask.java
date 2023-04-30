package com.svalero.reactive.api.task;

import com.svalero.reactive.api.model.Palette;
import com.svalero.reactive.api.service.ColourService;

import io.reactivex.functions.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.ComboBox;

public class FillPaletteTask extends Task<Integer> {
    ObservableList<String> paletteTitles = FXCollections.observableArrayList();
    ComboBox<String> mainSelector;

    public FillPaletteTask(ComboBox<String> mainSelector){
        this.mainSelector = mainSelector;
    }

    @Override
    protected Integer call() throws Exception {
        Consumer<Palette> user = (palette) -> {
            paletteTitles.add(palette.getTitle());
        };
        ColourService colourService = new ColourService();
        colourService.getAllPalettesInformation().subscribe(user);
        return null;
    }

    @Override
    protected void succeeded() {
        mainSelector.setItems(paletteTitles);
        super.succeeded();
    }
    
}
