package com.example.movieReco.service;

import com.example.movieReco.mapper.NaverMovie;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class NaverMovieService {
    private static final RestTemplate restTemplate = new RestTemplate();

    private static final String CLIENT_ID = "n7ho3gt3_wkd233d7ZJl";
    private static final String CLIENT_SECRET = "pcl1b0WtJ4";

    private static final String OpenNaverMovieUrl_getMovies ="https://openapi.naver.com/v1/search/movie.json?query={keyword}";

    static public NaverMovie findByKeyword(String keyword){
        NaverMovie mv = new NaverMovie();
        final HttpHeaders headers = new HttpHeaders(); // 헤더에 key들을 담아준다.
        headers.set("X-Naver-Client-Id", CLIENT_ID);
        headers.set("X-Naver-Client-Secret", CLIENT_SECRET);
        final HttpEntity<String> entity = new HttpEntity<>(headers);
        HashMap<String, Object> result = new HashMap<String, Object>();
        String jsonInString = "";
        try {
            ResponseEntity<Map> resultMap  = restTemplate.exchange(OpenNaverMovieUrl_getMovies, HttpMethod.GET, entity, Map.class, keyword);
            result.put("statusCode", resultMap.getStatusCodeValue()); //http status code를 확인
            result.put("header", resultMap.getHeaders()); //헤더 정보 확인
            result.put("body", resultMap.getBody()); //실제 데이터 정보 확인

            System.out.println("statusCode: "+result.get("statusCode"));
            //데이터를 제대로 전달 받았는지 확인 string형태로 파싱해줌
            ObjectMapper mapper = new ObjectMapper();
            jsonInString = mapper.writeValueAsString(resultMap.getBody());
            System.out.println("jsonInString: "+jsonInString);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put("statusCode", e.getRawStatusCode());
            result.put("body"  , e.getStatusText());
            System.out.println("dfdfdfdf");
            System.out.println(e.toString());

        } catch (Exception e) {
            result.put("statusCode", "999");
            result.put("body"  , "excpetion오류");
            System.out.println(e.toString());
        }

        NaverMovie naverMovie = restTemplate.exchange(OpenNaverMovieUrl_getMovies, HttpMethod.GET, entity, NaverMovie.class, keyword).getBody();
        NaverMovie.modifyNaverMovie(naverMovie);
        return naverMovie;
    }

}
