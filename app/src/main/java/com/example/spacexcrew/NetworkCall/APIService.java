package com.example.spacexcrew.NetworkCall;

import com.example.spacexcrew.Model.CrewDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("crew")
    Call<List<CrewDetails>> getAllItemsFromAPI();
}
