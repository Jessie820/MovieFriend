package com.example.movieReco.domain;

import com.example.movieReco.controller.MovieRecommendForm;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="recommendation")
@Getter @Setter
public class Recommendation extends BaseEntity{
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;

    private long userHeart;

    private String comment;
    private String recipientName;
    private Long recipientId;
    private String recipientEmail;

    @ColumnDefault("0")
    private long recipientHeart;//추천받은 사람이 주는 하트

    @ColumnDefault("0")
    private long rewardHeart;//보상 하트

}
