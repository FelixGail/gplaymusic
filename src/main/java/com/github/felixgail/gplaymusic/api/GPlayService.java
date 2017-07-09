package com.github.felixgail.gplaymusic.api;


import com.github.felixgail.gplaymusic.model.SubscriptionType;
import com.github.felixgail.gplaymusic.model.config.Config;
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
                                @Query("tier") SubscriptionType tier,
                                @Query("ct") SearchTypes searchTypes,
                                @Query("hl") Locale locale);

    default Call<SearchResponse> search(String query, SearchTypes searchTypes, int maxResults, Config config) {
        return search(query, maxResults, config.getSubscription(), searchTypes, config.getLocale());
    }

    default Call<SearchResponse> search(String query, SearchTypes searchTypes, Config config) {
        return search(query, 50, config.getSubscription(), searchTypes, config.getLocale());
    }

    @GET("config?dv=0&tier=ff")
    Call<Config> config(@Query("hl") Locale locale);

    default Call<Config> config(Config c) {
        return config(c.getLocale());
    }
}
