package com.example.movieReco.domain;

import com.example.movieReco.controller.MovieRecommendForm;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="recommendation")
@Getter @Setter
public class Recommendation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;

    private String comment;
    private String recipientName;
    private Long recipientId;
    private String recipientEmail;


}
