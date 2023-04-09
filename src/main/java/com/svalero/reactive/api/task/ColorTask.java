package com.svalero.reactive.api.task;

import com.svalero.reactive.api.model.Color;
import com.svalero.reactive.api.service.ColourService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

public class ColorTask extends Task<Integer> {
    Consumer<Color> user;
    String hex;

    public ColorTask(String hex, Consumer<Color> user){
        this.user = user;
        this.hex = hex;
    }

    @Override
    protected Integer call() throws Exception {
        ColourService colourService = new ColourService();
        colourService.getColorInformation(hex).subscribe(user);
        return null;
    }
    
}
