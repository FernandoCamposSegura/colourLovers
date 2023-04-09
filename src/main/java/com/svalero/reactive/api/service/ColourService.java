package com.svalero.reactive.api.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.svalero.reactive.api.model.Color;
import com.svalero.reactive.api.model.Palette;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ColourService {

    private String BASE_URL = "http://www.colourlovers.com/";
    private IColourService coloursAPI;

    public ColourService() {
    
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Gson gsonParser = new GsonBuilder()
                .setLenient()
                .create();
        
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gsonParser))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

        this.coloursAPI = retrofit.create(IColourService.class);
    }

    public Observable<Color> getInformation(String hex){
        return this.coloursAPI.getInformation(hex).flatMapIterable(color -> color);
    }

    public Observable<Color> getAllInformation() {
        return this.coloursAPI.getAllInformation().flatMapIterable(colors -> colors);
    }

    public Observable<Palette> getAllPalettesInformation() {
        return this.coloursAPI.getAllPalettesInformation().flatMapIterable(palettes -> palettes);
    }

}
