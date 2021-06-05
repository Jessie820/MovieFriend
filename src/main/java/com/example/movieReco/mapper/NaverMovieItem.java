package com.example.movieReco.mapper;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter

public class NaverMovieItem {
    public String title;
    public String link;
    public String image;
    public Date pubDate;
    public String director;
    public String actor;
    public float userRating;
    public String movieId;

}
