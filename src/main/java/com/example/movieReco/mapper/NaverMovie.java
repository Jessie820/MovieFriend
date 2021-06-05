package com.example.movieReco.mapper;

import org.springframework.util.StringUtils;

public class NaverMovie {
    private int display;
    private NaverMovieItem[] items;

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public NaverMovieItem[] getItems() {
        return items;
    }

    public void setItems(NaverMovieItem[] items) {
        this.items = items;
    }

    static public NaverMovie modifyNaverMovie(NaverMovie naverMovie){
        for(NaverMovieItem item : naverMovie.getItems()){
            String link = item.getLink();
            int startIndex = link.lastIndexOf("code=")+5;
            item.setMovieId(link.substring(startIndex));
            item.setTitle(removeSpecialChar(item.getTitle()));
            item.setDirector(removeSpecialChar(item.getDirector()));
        }
        return naverMovie;
    }

    static private String removeSpecialChar(String str){
        String resultStr = str;
        resultStr = StringUtils.replace(resultStr, "<b>", "");
        resultStr = StringUtils.replace(resultStr, "</b>", "");
        resultStr = StringUtils.replace(resultStr, "|", "");
        return resultStr;
    }

}
