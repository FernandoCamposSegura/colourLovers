package com.svalero.reactive.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Color {
    String title;
    String username;
    int numViews;
    int numVotes;
    int numComments;
    float numHearts;
    String hex;
    RGB rgb;
    HSV hsv;
}
