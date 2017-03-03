package dp.vmarkeev.moviedb.api;

import dp.vmarkeev.moviedb.Config;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vmarkeev on 28.02.2017.
 */

public class RetrofitUtils {
    private static Retrofit sSingleton;

    public static <T> T createApi(Class<T> clazz) {
        if (sSingleton == null) {
            synchronized (RetrofitUtils.class) {
                if (sSingleton == null) {
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                    httpClient.addInterceptor(logging);

                    Retrofit.Builder builder = new Retrofit.Builder();
                    builder.baseUrl(Config.BASE_URL)
                            .client(httpClient.build())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
                    sSingleton = builder.build();
                }
            }
        }
        return sSingleton.create(clazz);
    }
}
