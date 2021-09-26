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
    private String id;

    @Column(name = "MOVIE_TITLE")
    private String title;

    @Column(name = "DIRECTOR")
    private String director;
    
    private String releaseDate;

    @Column(name = "RATING")
    private float rating;

    private String imageLink;

    @Column(name = "RECOMMEND_CNT")
    private int recommendCnt;

    public static Movie createMovie(MovieRecommendForm mrf) {
        Movie movie = new Movie();
        movie.setId(mrf.getMovieId());
        movie.setTitle(mrf.getTitle());
        movie.setDirector(mrf.getDirector());
        movie.setRating(mrf.getUserRating());
        movie.setImageLink(mrf.getImageLink());
        movie.setRecommendCnt(1);
        movie.setReleaseDate(mrf.getReleasesDate());
        return movie;
    }
}
