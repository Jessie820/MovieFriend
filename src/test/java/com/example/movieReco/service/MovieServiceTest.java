package com.example.movieReco.service;

import com.example.movieReco.domain.Movie;
import com.example.movieReco.mapper.NaverMovieItem;
import com.example.movieReco.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Rollback(false)
class MovieServiceTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    MovieService movieService;
    @Autowired
    MovieRepository movieRepository;

    @Test
    public void 추천영화저장() throws Exception {
        //given
        NaverMovieItem nm = new NaverMovieItem();
        nm.setMovieId("123");
        nm.setActor("냥냥이");
        nm.setDirector("집사");
        nm.setImage("sidsid.jpg");
        nm.setLink("navermovie.com");
        nm.setTitle("냥냥이의 행복한 하루");
        nm.setPubDate(new Date());
        nm.setUserRating(10);

        //when
        String movieId = movieService.saveMovie(nm);

        //Then
        Movie getMovie = movieRepository.findOne(movieId);
        assertEquals("집사",getMovie.getDirector());
    }


}