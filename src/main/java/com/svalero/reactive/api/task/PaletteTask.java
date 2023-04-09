package com.svalero.reactive.api.task;

import com.svalero.reactive.api.model.Palette;
import com.svalero.reactive.api.service.ColourService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

public class PaletteTask extends Task<Integer> {
    Consumer<Palette> user;
    long id;

    public PaletteTask(Consumer<Palette> user, long id){
        this.user = user;
        this.id = id;
    }

    @Override
    protected Integer call() throws Exception {
        System.out.print("Hilo lanzado");
        ColourService colourService = new ColourService();
        colourService.getPaletteInformation(id).subscribe(user);
        return null;
    }
}
