package com.nds.nanodegree.movieapp.net.Response;

import com.google.gson.annotations.SerializedName;
import com.nds.nanodegree.movieapp.net.tos.Movie;

import java.util.List;
/*Movie search result response.
* */
/**
 * Created by Namrata Shah on 2/26/2017.
 */
public class SearchResult {
    @SerializedName("page")
    private int page;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("results")
    private List<Movie> movieResults;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<Movie> getMovieResults() {
        return movieResults;
    }

    public void setMovieResults(List<Movie> movieResults) {
        this.movieResults = movieResults;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "page=" + page +
                ", totalResults=" + totalResults +
                ", totalPages=" + totalPages +
                ", movieResults=" + movieResults +
                '}';
    }
}
