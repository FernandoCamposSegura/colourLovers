package com.svalero.reactive.api.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.svalero.reactive.api.model.Color;
import com.svalero.reactive.api.service.ColourService;
import com.svalero.reactive.api.task.ColorTask;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.*;

public class AppController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.fillSelector();
    }
    
    public void fillSelector() {
        ColourService colourService = new ColourService();

        Consumer<Color> user = (color) -> {
            titles.add(color.getTitle());
        };

        mainSelector.setItems(titles);
        colourService.getAllInformation().subscribe(user);
    }

    public void showColor() {
        ColourService colourService = new ColourService();

        Consumer<Color> user = (color) -> {
            if(color.getTitle().equals(mainSelector.getValue())) {
                currentHex = color.getHex();
                searchColor();
            }
        };

        colourService.getAllInformation().subscribe(user);
    }

    @FXML
    public void searchColor() {
        System.out.println(currentHex);
        Consumer<Color> user = (color) -> {
            lbViews.setText(String.valueOf(color.getNumViews()));
            lbVotes.setText(String.valueOf(color.getNumVotes()));
            lbComments.setText(String.valueOf(color.getNumComments()));
            lbHearts.setText(String.valueOf(color.getNumHearts()));
            lbRank.setText(String.valueOf(color.getRank()));
            lbHex.setText("#" + String.valueOf(color.getHex()));
            clColor.setFill(javafx.scene.paint.Color.valueOf("#" + color.getHex()));
        };

        ColourService colourService = new ColourService();
        colourService.getInformation(currentHex).subscribe(user);
        fillData();
    }

    @FXML
    public void fillData() {
        Platform.runLater(() -> {
            
        });
    }

    ColorTask colorTask;
    String currentHex;

    //UI Elements
    ObservableList<String> titles = FXCollections.observableArrayList();
    public ComboBox<String> mainSelector = new ComboBox<>(titles);

    public Label lbViews;
    public Label lbVotes;
    public Label lbComments;
    public Label lbHearts;
    public Label lbRank;
    public Label lbHex;
    public Label lbColor;

    public Circle clColor;
}
