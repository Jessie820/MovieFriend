package com.example.movieReco.controller;

import com.example.movieReco.error.DuplicateException;
import com.example.movieReco.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    @GetMapping(value = "/signup/checkemail")
    public ResponseEntity checkEmail(Model model, @RequestParam(value="query") String email){
        System.out.println("checkemail");
        String checkResult = userService.checkEmail(email);
        if(!checkResult.equals("success")){
            throw new DuplicateException("중복된 이메일입니다.");
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
