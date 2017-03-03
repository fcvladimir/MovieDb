package dp.vmarkeev.moviedb.models.movies;

import java.util.List;

/**
 * Created by vmarkeev on 28.02.2017.
 */

public class MovieModel {

    private List<Results> results;

    private String page;

    private int total_pages;

    private String total_results;

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public String getTotal_results() {
        return total_results;
    }

    public void setTotal_results(String total_results) {
        this.total_results = total_results;
    }
}
