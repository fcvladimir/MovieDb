package dp.vmarkeev.moviedb.models.movies;

/**
 * Created by vmarkeev on 28.02.2017.
 */

public class Results {

    private String id;

    private String poster_path;

    public Results(String id, String poster_path) {
        this.id = id;
        this.poster_path = poster_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
}
