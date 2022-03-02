package com.example.movieReco.mapper;

public class RecoDetail {
    private String movieId;
    private String title;
    private String director;
    private String actor;
    private long userHeart;
    private String imageLink;

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public long getUserHeart() {
        return userHeart;
    }

    public void setUserHeart(long userHeart) {
        this.userHeart = userHeart;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }
}
