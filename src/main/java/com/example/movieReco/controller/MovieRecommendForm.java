package com.example.movieReco.controller;


import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter @Setter
public class MovieRecommendForm {
    public String title;
/*    public String link;
    public String image;
    public Date pubDate;*/
    public String director;

    @NotEmpty(message = "추천 이유는 필수 입니다")
    public String comment;
}
