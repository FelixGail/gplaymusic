package com.github.felixgail.gplaymusic.api;


import com.github.felixgail.gplaymusic.model.SongQuality;
import com.github.felixgail.gplaymusic.model.config.Config;
import com.github.felixgail.gplaymusic.model.search.SearchResponse;
import com.github.felixgail.gplaymusic.model.search.SearchTypes;
import com.github.felixgail.gplaymusic.model.shema.DeviceList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

import java.util.Locale;

public interface GPlayService {

    @GET("sj/v2.5/query")
    Call<SearchResponse> search(@Query("q") String query,
                                @Query("max-results") int maxResults,
                                @Query("ct") SearchTypes searchTypes);

    @GET("sj/v2.5/config?dv=0&tier=ff")
    Call<Config> config(@Query("hl") Locale locale);

    @GET("music/mplay?net=mob&pt=e")
    Call<Void> getTrackLocation(@Header("X-Device-ID") String androidID,
                                        @Query("opt") SongQuality quality,
                                        @Query("slt") String salt,
                                        @Query("sig") String signature,
                                        @Query("mjck") String trackID);

    @GET("sj/v2.5/devicemanagementinfo")
    Call<DeviceList> getDevices();
}
