package com.github.felixgail.gplaymusic.api;

import static com.github.felixgail.gplaymusic.model.Station.BATCH_URL;

import com.github.felixgail.gplaymusic.exceptions.NetworkException;
import com.github.felixgail.gplaymusic.model.MutationResponse;
import com.github.felixgail.gplaymusic.model.PagingHandler;
import com.github.felixgail.gplaymusic.model.Station;
import com.github.felixgail.gplaymusic.model.requests.ListStationTracksRequest;
import com.github.felixgail.gplaymusic.model.requests.PagingRequest;
import com.github.felixgail.gplaymusic.model.requests.mutations.MutationFactory;
import com.github.felixgail.gplaymusic.model.requests.mutations.Mutator;
import com.github.felixgail.gplaymusic.model.responses.ListResult;
import com.github.felixgail.gplaymusic.model.snippets.StationSeed;
import com.github.felixgail.gplaymusic.util.language.Language;
import java.io.IOException;
import java.util.List;
import javax.validation.constraints.NotNull;

public class StationApi implements SubApi {

  private GPlayMusic mainApi;

  StationApi(GPlayMusic api) {
    this.mainApi = api;
  }

  /**
   * Creates a new Station.
   *
   * @param seed a seed to build the station upon.
   * @param name name of the new station
   * @param includeTracks whether the response should
   * @return Returns the newly created station
   */
  public Station create(final StationSeed seed, final String name, final boolean includeTracks)
      throws IOException {
    final Mutator mutator = new Mutator(
        MutationFactory.getAddStationMutation(name, seed, includeTracks));
    final MutationResponse response = mainApi.getService().makeBatchCall(BATCH_URL, mutator);
    MutationResponse.Item item = response.getItems().get(0);
    if (item.hasStationKey()) {
      return item.getStation();
    }
    throw new NetworkException(400, Language.get("station.create.NetworkException"));
  }

  public List<Station> listStations()
      throws IOException {
    return new PagingHandler<Station>() {
      @Override
      public ListResult<Station> getChunk(String nextPageToken) throws IOException {
        return mainApi.getService()
            .listStations(new PagingRequest(nextPageToken, -1)).execute().body();
      }
    }.getAll();
  }

  public List<Station> getFilledStations(@NotNull ListStationTracksRequest request)
      throws IOException {
    return mainApi.getService()
        .getFilledStations(request).execute().body().toList();
  }

  public void deleteStations(Station... stations)
      throws IOException {
    Mutator mutator = new Mutator();
    for (Station station : stations) {
      mutator.addMutation(MutationFactory.getDeleteStationMutation(station));
    }
    mainApi.getService().makeBatchCall(Station.BATCH_URL, mutator);
  }

  @Override
  public GPlayMusic getMainApi() {
    return mainApi;
  }
}
