package com.example.movieReco.controller;

import com.example.movieReco.domain.Member;
import com.example.movieReco.domain.Movie;
import com.example.movieReco.domain.Recommendation;
import com.example.movieReco.error.EmptyInputException;
import com.example.movieReco.error.NoResultException;
import com.example.movieReco.mapper.MemberDetail;
import com.example.movieReco.mapper.NaverMovieItem;
import com.example.movieReco.mapper.RecoDetail;
import com.example.movieReco.mapper.RecoSaved;
import com.example.movieReco.repository.RecommendRepository;
import com.example.movieReco.service.MovieService;
import com.example.movieReco.service.NaverMovieService;
//import lombok.extern.slf4j.Slf4j;
import com.example.movieReco.service.RecommendService;
import com.example.movieReco.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final Logger log = LoggerFactory.getLogger(MovieController.class);

    private final MovieService movieService;
    private final RecommendService recommendService;
    private final UserService userService;

    @GetMapping(value = "/movies")
    public String home() {
        return "movieHome";
    }

    @GetMapping("/movies/search")
    public String get(Model model, @RequestParam(value="query") String keyword){
       if(!StringUtils.hasText(keyword)){
           throw new EmptyInputException("데이터를 입력해주세요.");
       }

       log.info("Naver Movie Search");
       NaverMovieItem[] movieItems = NaverMovieService.findByKeyword(keyword).getItems();
       if(movieItems.length == 0){
           throw new NoResultException("영화 검색결과가 없습니다.");
       }
       model.addAttribute("movieItems", movieItems);
        //return moviesService.findByKeyword(keyword);
        return "movieHome :: #list";
    }

    @PostMapping(value = "/movies/recoNew")
    public String createRecoForm(RecoDetail recoDetail, Model model) {
        log.info("movie title" + recoDetail.getTitle());
        log.info("movie director" + recoDetail.getDirector());
        log.info("movie movieId" + recoDetail.getMovieId());
        log.info("movie imageLink" + recoDetail.getImageLink());

        MovieRecommendForm movieRecommendForm = new MovieRecommendForm(recoDetail);
        model.addAttribute("movieRecommendForm", movieRecommendForm);

        return "recommendMovieForm";
    }

    @PostMapping(value = "/movies/scoreReco")
    public String scoreReo(RecoSaved recoSaved, Model model) {
        log.info("recipient Heart" + recoSaved.getRecipientHeart());
        log.info("recommendId" + recoSaved.getRecommendId());
        recommendService.saveScore(recoSaved);
        model.addAttribute("memberDetail", new MemberDetail());
        return "redirect:/signup";
    }

    @PostMapping(value = "movies/recommendMovie")
    public String recommendMovie(@ModelAttribute("movieRecommendForm") MovieRecommendForm movieRecommendForm
                                    ,Authentication authentication){
        Movie movie = Movie.createMovie(movieRecommendForm);
        movieService.saveMovie(movie);

        Member member = getMemberCurInfo(authentication);

        Long recoId = saveRecommendation(movieRecommendForm, movie, member);
        String recommendView = "redirect:/recommendation/";

        return (recommendView + recoId);
    }

    @NotNull
    private Member getMemberCurInfo(Authentication authentication) {
        //현재 로그인 된 사용자 정보 가져오기
        MemberDetail memberDetail = (MemberDetail) authentication.getPrincipal();
        Member member = Member.createMember(memberDetail);

        //하트 수 업데이트를 위한 사용자 정보 재조회
        MemberDetail newMember = new MemberDetail(userService.find(member.getId()));
        Authentication newAuth = new UsernamePasswordAuthenticationToken(newMember, memberDetail.getPassword(), memberDetail.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        return member;
    }

    @GetMapping("/recommendation/{recoId}")
    public String recommendation(Model model, @PathVariable Long recoId){
        Recommendation recommendation = recommendService.findRecommendation(recoId);
        RecoSaved recoSaved = new RecoSaved(recommendation);
        model.addAttribute("recoSaved", recoSaved);
        return "recommendation";
    }


    @GetMapping("/recommendation/{recoId}/appreciate")
    public String recommendAppreciate(Model model, @PathVariable Long recoId){
        Recommendation recommendation = recommendService.findRecommendation(recoId);
        RecoSaved recoSaved = new RecoSaved(recommendation);
        model.addAttribute("recoSaved", recoSaved);
        return "recommendAppreciate";
    }

    public Long saveRecommendation(MovieRecommendForm mrf, Movie movie, Member member){
        Recommendation recommendation = new Recommendation();
        recommendation.setMember(member);
        recommendation.setRecipientName(mrf.getRecipientName());
        recommendation.setComment(mrf.getComment());
        recommendation.setMovie(movie);
        recommendation.setUserHeart(mrf.getUserHeart());
        Long id = recommendService.save(member, recommendation);
        return id;
    }

    @GetMapping("/myRecommendList")
    public String getMyRecommendList(Model model, Authentication authentication){
        //현재 로그인 된 사용자 정보 가져오기
        MemberDetail memberDetail = (MemberDetail)authentication.getPrincipal();
        Member member = Member.createMember(memberDetail);
        getMemberCurInfo(authentication);
        List<Recommendation> recommendItems = recommendService.findMyRecommendations(member);
        model.addAttribute("recommendItems", recommendItems);
        return "myRecommendList";
    }


}
