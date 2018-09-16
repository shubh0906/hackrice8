package com.shopiholik.app.com.shopiholik.app.search;

import com.shopiholik.app.model.OfferLogo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author agrawroh
 * @version v1.0
 */
public interface OfferBrandImageApi {
    @GET("v1/companies/suggest")
    Call<List<OfferLogo>> getBrandLogoImage(@Query("query") String query);
}
