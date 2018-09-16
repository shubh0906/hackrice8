package com.shopiholik.app;

import com.shopiholik.app.model.PlacesResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author agrawroh
 * @version v1.0
 */
public interface GetPlacesApi {
    @GET("maps/api/place/findplacefromtext/json")
    Call<PlacesResults> getPlaces(@Query("inputtype") String inputType,
                                  @Query("input") String input,
                                  @Query("locationbias") String locationBias,
                                  @Query("fields") String fields,
                                  @Query("key") String key);
}
