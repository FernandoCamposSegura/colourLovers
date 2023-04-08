package com.svalero.reactive.api.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.svalero.reactive.api.model.Color;
import com.svalero.reactive.api.service.ColourService;

import io.reactivex.functions.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

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
        System.out.println(mainSelector.getSelectionModel());
    }

    ObservableList<String> titles = FXCollections.observableArrayList();
    public ComboBox<String> mainSelector = new ComboBox<>(titles);
}
