package com.svalero.reactive.api.task;

import com.svalero.reactive.api.model.Color;
import com.svalero.reactive.api.service.ColourService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;

public class ColorTask extends Task<Integer> {
    Consumer<Color> user;
    String hex;
    ProgressIndicator colorIndicator;

    public ColorTask(String hex, Consumer<Color> user, ProgressIndicator colorIndicator){
        this.user = user;
        this.hex = hex;
        this.colorIndicator = colorIndicator;
    }

    @Override
    protected Integer call() throws Exception {
        ColourService colourService = new ColourService();
        colourService.getColorInformation(hex).subscribe(user);
        return null;
    }

    @Override
    protected void succeeded() {
        colorIndicator.setVisible(false);
        super.succeeded();
    }
}
