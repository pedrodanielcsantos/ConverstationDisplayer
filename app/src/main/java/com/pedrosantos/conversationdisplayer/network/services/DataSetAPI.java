package com.pedrosantos.conversationdisplayer.network.services;

import com.pedrosantos.conversationdisplayer.models.CDDataSet;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Service that maps the endpoint on the API of the main dataset retrieval.
 */
public interface DataSetAPI {

    @GET("1585962/api.json")
    Call<CDDataSet> getData();

}
