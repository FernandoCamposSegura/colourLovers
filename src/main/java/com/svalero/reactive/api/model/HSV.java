package com.svalero.reactive.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HSV {
    int hue;
    int saturation;
    int value;
}
