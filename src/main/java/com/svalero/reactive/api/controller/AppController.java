package com.svalero.reactive.api.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.svalero.reactive.api.model.Color;
import com.svalero.reactive.api.model.Palette;
import com.svalero.reactive.api.service.ColourService;
import com.svalero.reactive.api.task.ColorTask;
import com.svalero.reactive.api.task.PaletteTask;

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
            paleteTitles.add(palette.getTitle());
        };

        mainSelectorPalette.setItems(paleteTitles);
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

    public void showPalette() {
        ColourService colourService = new ColourService();

        Consumer<Palette> user = (palette) -> {
            if(palette.getTitle().equals(mainSelectorPalette.getValue())) {
                System.out.print(palette.getId());
                searchPalette(palette.getId());
            }
        };

        colourService.getAllPalettesInformation().subscribe(user);
    }

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

    public void searchPalette(long id) {

        Consumer<Palette> user = (palette) -> {
            Platform.runLater(() -> {
                lbPaletteViews.setText(String.valueOf(palette.getNumViews()));
                lbPaletteVotes.setText(String.valueOf(palette.getNumVotes()));
                lbPaletteComments.setText(String.valueOf(palette.getNumComments()));
                lbPaletteHearts.setText(String.valueOf(palette.getNumHearts()));
                lbPaletteRank.setText(String.valueOf(palette.getRank()));
            });
            imvPalette.setImage(new Image(palette.getApiUrl()));
        };

        PaletteTask paletteTask = new PaletteTask(user, id);

        // progressBar.progressProperty().bind(colorTask.progressProperty());
        new Thread(paletteTask).start();
    }

    ColorTask colorTask;

    //UI Elements
    ObservableList<String> colorTitles = FXCollections.observableArrayList();
    public ComboBox<String> mainSelector = new ComboBox<>(colorTitles);

    ObservableList<String> paleteTitles = FXCollections.observableArrayList();
    public ComboBox<String> mainSelectorPalette = new ComboBox<>(paleteTitles);

    public Label lbViews;
    public Label lbVotes;
    public Label lbComments;
    public Label lbHearts;
    public Label lbRank;
    public Label lbHex;
    public Label lbColor;

    public Label lbPaletteViews;
    public Label lbPaletteVotes;
    public Label lbPaletteComments;
    public Label lbPaletteHearts;
    public Label lbPaletteRank;

    public Slider slRed;
    public Slider slGreen;
    public Slider slBlue;

    public Slider slHue;
    public Slider slSaturation;
    public Slider slValue;

    public Circle clColor;

    public ImageView imvPalette;

    public ProgressBar progressBar;
}
