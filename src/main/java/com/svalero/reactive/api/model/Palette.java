package com.svalero.reactive.api.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Palette {
    long id;
    String title;
    int numViews;
    int numVotes;
    int numComments;
    float numHearts;
    //List<Colors> colors;
}
