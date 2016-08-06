package com.pedrosantos.conversationdisplayer.network.clients;

import com.google.gson.GsonBuilder;

import com.pedrosantos.conversationdisplayer.network.services.DataSetAPI;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Application's network client.
 */
public class CDNetworkClient {

    private static final String API_BASE_URL = "https://dl.dropboxusercontent.com/u/";
    private static CDNetworkClient mClient;
    private DataSetAPI mConversationAPI;

    private CDNetworkClient() {

        // Initialize auxiliary objects to use on the retrofit object

        //Logging interceptor
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        //Initialize retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        // Create services objects
        mConversationAPI = retrofit.create(DataSetAPI.class);
    }

    public static CDNetworkClient getInstance() {
        if (mClient == null) {
            mClient = new CDNetworkClient();
        }
        return mClient;
    }

    public DataSetAPI getConversationAPI() {
        return mConversationAPI;
    }
}
