package br.com.agenda.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitInicializador {
    private final Retrofit retrofit;

    public RetrofitInicializador() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.14:8080/api/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
