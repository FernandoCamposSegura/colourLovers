package com.svalero.reactive.api.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.opencsv.CSVWriter;
import com.svalero.reactive.api.model.Color;
import com.svalero.reactive.api.model.Palette;
import com.svalero.reactive.api.service.ColourService;
import com.svalero.reactive.api.task.ColorTask;
import com.svalero.reactive.api.task.FillColorTask;
import com.svalero.reactive.api.task.FillPaletteTask;
import com.svalero.reactive.api.task.PaletteTask;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
        colorIndicator.setVisible(false);
        paletteIndicator.setVisible(false);
    }

    public void fillColorSelector() {
        FillColorTask fillColorTask = new FillColorTask(mainSelector);
        new Thread(fillColorTask).start();
    }

    public void fillPaletteSelector() {
        FillPaletteTask fillPaletteTask = new FillPaletteTask(mainSelectorPalette);
        new Thread(fillPaletteTask).start();
    }

    public void showColor() {
        ColourService colourService = new ColourService();

        Consumer<Color> user = (color) -> {
            if (color.getTitle().equals(mainSelector.getValue())) {

                colorIndicator.setVisible(true);
                searchColor(color.getHex());
            }
        };

        colourService.getAllInformation().subscribe(user);
    }

    public void showPalette() {
        ColourService colourService = new ColourService();

        Consumer<Palette> user = (palette) -> {
            if (palette.getTitle().equals(mainSelectorPalette.getValue())) {

                paletteIndicator.setVisible(true);
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
                searchedColorsArea.setText(searchedColorsArea.getText() +
                    "Color searched -> " + color.getTitle() + " Hex Code -> " + color.getHex() + "\n");
            });

            clColor.setFill(javafx.scene.paint.Color.valueOf("#" + color.getHex()));
            slRed.setValue(color.getRgb().getRed());
            slGreen.setValue(color.getRgb().getGreen());
            slBlue.setValue(color.getRgb().getBlue());
            slHue.setValue(color.getHsv().getHue());
            slSaturation.setValue(color.getHsv().getSaturation());
            slValue.setValue(color.getHsv().getValue());
            
            colorsSearched.add(color);
        };

        ColorTask colorTask = new ColorTask(hex, user, colorIndicator);
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
            imvPalette.setImage(new Image(getUrlImageFromPalette(palette.getColors(), palette.getTitle())));
        };

        PaletteTask paletteTask = new PaletteTask(user, id, paletteIndicator);
        new Thread(paletteTask).start();
    }

    public void deleteColorRegister() {
        colorsSearched.remove(Integer.parseInt(colorRegisterSelected.getText()));

        for(Color color : colorsSearched) {
            searchedColorsArea.setText(searchedColorsArea.getText() +
                    "Color searched -> " + color.getTitle() + " Hex Code -> " + color.getHex() + "\n");
        }
    }

    @FXML
    public void exportColorCSV(ActionEvent event) {
        convertDataToCSV(false);
    }

    @FXML
    public void exportColorCSVComprossed(ActionEvent event) {
        convertDataToCSV(true);
    }

    public void convertDataToCSV(boolean compressIsRequired) {
        File outputFile = new File(System.getProperty("user.dir") + System.getProperty("file.separator")
                + mainSelector.getValue() + "_color.csv");

        try {
            FileWriter writer = new FileWriter(outputFile);
            CSVWriter csvWriter = new CSVWriter(writer);
            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[] { searchedColorsArea.getText() });
            csvWriter.writeAll(data);
            csvWriter.close();

            if (compressIsRequired) {

                String zipFile = outputFile.getName() + ".zip";
                FileInputStream fis = new FileInputStream(outputFile);

                // Output zip file stream
                FileOutputStream fos = new FileOutputStream(zipFile);
                ZipOutputStream zipOut = new ZipOutputStream(fos);

                // Add file entry to zip file
                ZipEntry zipEntry = new ZipEntry(outputFile.getAbsolutePath());
                zipOut.putNextEntry(zipEntry);

                // Write file content to zip file
                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }

                // Close streams
                fis.close();
                zipOut.close();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUrlImageFromPalette(List<String> colors, String title) {
        String url = "http://www.colourlovers.com/paletteImg/";
        for (String color : colors) {
            url += color + "/";
        }
        return url.concat(title.replace(' ', '_')).concat(".png");
    }

    ColorTask colorTask;

    // UI Elements
    ObservableList<String> colorTitles = FXCollections.observableArrayList();
    public ComboBox<String> mainSelector = new ComboBox<>(colorTitles);

    ObservableList<String> paleteTitles = FXCollections.observableArrayList();
    public ComboBox<String> mainSelectorPalette = new ComboBox<>(paleteTitles);

    public TextArea searchedColorsArea;
    public List<Color> colorsSearched;
    public TextField colorRegisterSelected;

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
    public ProgressIndicator colorIndicator;
    public ProgressIndicator paletteIndicator;
}
