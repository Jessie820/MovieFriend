package com.example.movieReco.domain;

import com.example.movieReco.controller.MovieRecommendForm;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Movie {
    @Id
    @Column(name = "MOVIE_ID")
    private String MovieId;

    @Column(name = "MOVIE_TITLE")
    private String MovieTitle;

    @Column(name = "Director")
    private String Director;
    
    private String ReleaseDate;

    @Column(name = "Rating")
    private float Rating;

    private String image;

    @Column(name = "RecommendCnt")
    private int RecommendCnt;

    public static Movie createMovie(MovieRecommendForm mrf) {
        Movie movie = new Movie();
        movie.setMovieId(mrf.getMovieId());
        movie.setMovieTitle(mrf.getTitle());
        movie.setDirector(mrf.getDirector());
        movie.setRating(mrf.getUserRating());
        movie.setImage(mrf.getImageLink());
        movie.setRecommendCnt(1);
        movie.setReleaseDate(mrf.getReleasesDate());
        return movie;
    }
}
