package com.svalero.reactive.api.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.svalero.reactive.api.model.Color;
import com.svalero.reactive.api.model.Palette;
import com.svalero.reactive.api.service.ColourService;
import com.svalero.reactive.api.task.ColorTask;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class AppController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillColorSelector();
        fillPaletteSelector();
        slRed.setMax(255);
        slGreen.setMax(255);
        slBlue.setMax(255);
    }
    
    public void fillColorSelector() {
        ColourService colourService = new ColourService();

        Consumer<Color> user = (color) -> {
            colorTitles.add(color.getTitle());
        };

        mainSelector.setItems(colorTitles);
        colourService.getAllInformation().subscribe(user);
    }

    public void fillPaletteSelector() {
        ColourService colourService = new ColourService();

        Consumer<Palette> user = (palette) -> {
            paleteTitles.put(palette.getId(), palette.getTitle());
        };

        mainSelectorPalette.setItems(FXCollections.observableArrayList(paleteTitles.values()));
        colourService.getAllPalettesInformation().subscribe(user);
    }

    public void showColor() {
        ColourService colourService = new ColourService();

        Consumer<Color> user = (color) -> {
            if(color.getTitle().equals(mainSelector.getValue())) {
                searchColor(color.getHex());
            }
        };

        colourService.getAllInformation().subscribe(user);
    }

    @FXML
    public void searchColor(String hex) {

        Consumer<Color> user = (color) -> {
            Platform.runLater(() -> {
                lbViews.setText(String.valueOf(color.getNumViews()));
                lbVotes.setText(String.valueOf(color.getNumVotes()));
                lbComments.setText(String.valueOf(color.getNumComments()));
                lbHearts.setText(String.valueOf(color.getNumHearts()));
                lbRank.setText(String.valueOf(color.getRank()));
                lbHex.setText("#" + String.valueOf(color.getHex()));
            });

            clColor.setFill(javafx.scene.paint.Color.valueOf("#" + color.getHex()));
            slRed.setValue(color.getRgb().getRed());
            slGreen.setValue(color.getRgb().getGreen());
            slBlue.setValue(color.getRgb().getBlue());
            slHue.setValue(color.getHsv().getHue());
            slSaturation.setValue(color.getHsv().getSaturation());
            slValue.setValue(color.getHsv().getValue());
        };

        ColorTask colorTask = new ColorTask(hex, user);

        // progressBar.progressProperty().bind(colorTask.progressProperty());
        new Thread(colorTask).start();
    }

    ColorTask colorTask;

    //UI Elements
    ObservableList<String> colorTitles = FXCollections.observableArrayList();
    public ComboBox<String> mainSelector = new ComboBox<>(colorTitles);

    ObservableMap<Long, String> paleteTitles = FXCollections.observableHashMap();
    public ComboBox<String> mainSelectorPalette;

    public Label lbViews;
    public Label lbVotes;
    public Label lbComments;
    public Label lbHearts;
    public Label lbRank;
    public Label lbHex;
    public Label lbColor;

    public Slider slRed;
    public Slider slGreen;
    public Slider slBlue;

    public Slider slHue;
    public Slider slSaturation;
    public Slider slValue;

    public Circle clColor;

    public ProgressBar progressBar;
}
