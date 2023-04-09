package com.svalero.reactive.api.service;

import java.util.List;

import com.svalero.reactive.api.model.Color;
import com.svalero.reactive.api.model.Palette;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IColourService {

    @GET("api/color/{hex}?format=json")
    Observable<List<Color>> getInformation(@Path("hex") String hex);

    @GET("api/colors?format=json")
    Observable<List<Color>> getAllInformation();

    @GET("api/palettes?format=json")
    Observable<List<Palette>> getAllPalettesInformation();
}
