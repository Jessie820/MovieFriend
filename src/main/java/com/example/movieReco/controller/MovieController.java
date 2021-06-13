package com.example.movieReco.controller;

import com.example.movieReco.mapper.NaverMovie;
import com.example.movieReco.mapper.NaverMovieItem;
import com.example.movieReco.service.NaverMovieService;
//import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
//@Slf4j
public class MovieController {

    private final Logger log = LoggerFactory.getLogger(MovieController.class);

    @GetMapping(value = "/movies")
    public String home() {
        return "movieHome";
    }

    @GetMapping("/movies/{keyword}")
    public String get(Model model, @PathVariable String keyword){
       log.info("Naver Movie Search");
       NaverMovieItem[] movieItems = NaverMovieService.findByKeyword(keyword).getItems();
       model.addAttribute("movieItems", movieItems);
        //return moviesService.findByKeyword(keyword);
        return "movieHome :: #list";
    }

    @GetMapping(value = "/movies/recoNew")
    public String createForm(Model model, @RequestParam("movieTitle") String movieTitle
                                         ,@RequestParam("movieDirector") String movieDirector) {
        log.info("Naver movie form");
        log.info("movie title" + movieTitle);
        log.info("movie director" + movieDirector);
        model.addAttribute("MovieRecommendForm", new MovieRecommendForm());
        model.addAttribute("title", movieTitle);
        model.addAttribute("director", movieDirector);
        return "recommendMovieForm";
    }
}
