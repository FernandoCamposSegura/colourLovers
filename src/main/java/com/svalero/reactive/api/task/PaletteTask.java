package com.svalero.reactive.api.task;

import com.svalero.reactive.api.model.Palette;
import com.svalero.reactive.api.service.ColourService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;

public class PaletteTask extends Task<Integer> {
    Consumer<Palette> user;
    long id;
    ProgressIndicator palettIndicator;

    public PaletteTask(Consumer<Palette> user, long id, ProgressIndicator paletteIndicator){
        this.user = user;
        this.id = id;
        this.palettIndicator = paletteIndicator;
    }

    @Override
    protected Integer call() throws Exception {
        ColourService colourService = new ColourService();
        colourService.getPaletteInformation(id).subscribe(user);
        return null;
    }

    @Override
    protected void succeeded() {
        palettIndicator.setVisible(false);
        super.succeeded();
    }

    
}
