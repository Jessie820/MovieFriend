package com.example.movieReco.controller;

import com.example.movieReco.mapper.RecoDetail;

public class MovieRecommendForm {
    public String movieId;
    public String title;
    public String director;
    public String actor;
    // @NotEmpty(message = "추천 이유는 필수 입니다")
    public String comment;
    public String releasesDate;
    private long userHeart;
    private String imageLink;
    private String recipientName;

    public MovieRecommendForm(){

    }

    public MovieRecommendForm(RecoDetail recoDetail){
        this.movieId = recoDetail.getMovieId();
        this.title = recoDetail.getTitle();
        this.director = recoDetail.getDirector();
        this.actor = recoDetail.getActor();
        this.imageLink = recoDetail.getImageLink();
        this.userHeart = recoDetail.getUserHeart();
    }

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

    public String getReleasesDate() {
        return releasesDate;
    }

    public void setReleasesDate(String releasesDate) {
        this.releasesDate = releasesDate;
    }

    public long getUserHeart() {
        return userHeart;
    }

    public void setUserHeart(long heart) {
        this.userHeart = heart;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
