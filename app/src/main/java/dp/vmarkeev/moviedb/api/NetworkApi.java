package dp.vmarkeev.moviedb.api;

import dp.vmarkeev.moviedb.models.DetailModel;
import dp.vmarkeev.moviedb.models.movies.MovieModel;
import dp.vmarkeev.moviedb.models.trailers.TrailerModel;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface NetworkApi {

    String API_KEY = "api_key";
    String LANGUAGE = "language";
    String PAGE = "page";
    String MOVIE_ID = "movie_id";

    @GET("popular")
    Observable<MovieModel> getPopular(@Query(API_KEY) String api_key,
                                      @Query(LANGUAGE) String language,
                                      @Query(PAGE) int page);

    @GET("top_rated")
    Observable<MovieModel> getTopRated(@Query(API_KEY) String api_key,
                                       @Query(LANGUAGE) String language,
                                       @Query(PAGE) int page);

    @GET("{movie_id}")
    Observable<DetailModel> getDetails(@Path(MOVIE_ID) String movie_id,
                                       @Query(API_KEY) String api_key,
                                       @Query(LANGUAGE) String language);

    @GET("{movie_id}/videos")
    Observable<TrailerModel> getTrailers(@Path(MOVIE_ID) String movie_id,
                                         @Query(API_KEY) String api_key,
                                         @Query(LANGUAGE) String language);
}
