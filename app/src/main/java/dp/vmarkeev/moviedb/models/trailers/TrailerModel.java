package dp.vmarkeev.moviedb.models.trailers;

import java.util.List;

/**
 * Created by vmarkeev on 02.03.2017.
 */

public class TrailerModel {

    private String id;

    private List<Results> results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }
}
