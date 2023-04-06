package com.svalero.reactive.api.controller;

import com.svalero.reactive.api.model.Color;
import com.svalero.reactive.api.task.ColorTask;

import io.reactivex.functions.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AppController {

    public TextField wordInput;
    public Button btSearch;
    public TextArea definitionsArea;

    private ColorTask colorTask;

    @FXML
    public void searchColors(ActionEvent event){
        String requestedColor = wordInput.getText();
        wordInput.clear();
        wordInput.requestFocus();
        definitionsArea.setText("");
        //Obtener color por hexadecimal
        Consumer<Color> user = (color) -> {
            definitionsArea.setText(definitionsArea.getText() + "\n" + color.getNumComments());
        };

        colorTask = new ColorTask(requestedColor, user);
        new Thread(colorTask).start();
    }

    @FXML
    public void searchAllColors(ActionEvent event){
        String requestedColor = wordInput.getText();
        definitionsArea.setText("");

        Consumer<Color> user = (color) -> {
            definitionsArea.setText(color.getTitle() + definitionsArea.getText() + "\n");
        };

        colorTask = new ColorTask(requestedColor, user);
        new Thread(colorTask).start();
    }
}
