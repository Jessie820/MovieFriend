package com.example.movieReco.controller;

import com.example.movieReco.mapper.NaverMovieItem;
import com.example.movieReco.service.NaverMovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MovieController {

    @GetMapping(value = "/movies")
    public String home() {
        return "movieHome";
    }

    @GetMapping("/movies/{keyword}")
    public String get(Model model, @PathVariable String keyword){
        System.out.println("Controller: "+keyword);
        NaverMovieItem[] movieItems = NaverMovieService.findByKeyword(keyword).getItems();
        model.addAttribute("movieItems", movieItems);
        //return moviesService.findByKeyword(keyword);
        return "movieHome :: #list";
    }
}
