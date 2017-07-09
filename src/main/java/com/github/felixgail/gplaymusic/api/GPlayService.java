package com.github.felixgail.gplaymusic.api;


import com.github.felixgail.gplaymusic.model.SubscriptionTypes;
import com.github.felixgail.gplaymusic.model.config.ConfigResponse;
import com.github.felixgail.gplaymusic.model.search.SearchResponse;
import com.github.felixgail.gplaymusic.model.search.SearchTypes;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.Locale;

public interface GPlayService {

    @GET("query?dv=0")
    Call<SearchResponse> search(@Query("q") String query,
                                @Query("max-results") int maxResults,
                                @Query("tier") SubscriptionTypes tier,
                                @Query("ct") SearchTypes searchTypes,
                                @Query("hl") Locale locale);

    default Call<SearchResponse> search(String query, SearchTypes searchTypes, int maxResults) {
        return search(query, maxResults, SubscriptionTypes.SUBSCRIBED, searchTypes, Locale.US);
    }

    default Call<SearchResponse> search(String query, SearchTypes searchTypes) {
        return search(query, 50, SubscriptionTypes.SUBSCRIBED, searchTypes, Locale.US);
    }

    @GET("config?dv=0&tier=ff")
    Call<ConfigResponse> config(@Query("hl") Locale locale);

    default Call<ConfigResponse> config() {
        return config(Locale.US);
    }
}
