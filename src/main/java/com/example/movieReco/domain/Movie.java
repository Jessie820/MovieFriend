package com.example.movieReco.domain;

import com.example.movieReco.mapper.NaverMovieItem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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

    @Temporal(TemporalType.DATE)
    private Date ReleaseDate;

    @Column(name = "Rating")
    private float Rating;

    private String image;

    @Column(name = "RecommendCnt")
    private int RecommendCnt;

    public static Movie createMovie(NaverMovieItem nm) {
        Movie movie = new Movie();
        movie.setMovieId(nm.getMovieId());
        movie.setMovieTitle(nm.getTitle());
        movie.setDirector(nm.getDirector());
        movie.setRating(nm.getUserRating());
        movie.setImage(nm.getImage());
        movie.setRecommendCnt(1);
        movie.setReleaseDate(nm.getPubDate());
        return movie;
    }
}
