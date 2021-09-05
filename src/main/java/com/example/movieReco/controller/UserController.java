package com.example.movieReco.controller;

import com.example.movieReco.domain.Member;
import com.example.movieReco.mapper.MemberDetail;
import com.example.movieReco.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/signup")
    public String signUpForm(Model model){
        model.addAttribute("memberDetail", new MemberDetail());
        return "signup";
    }

    @GetMapping(value = "/login")
    public String loginForm(){
        return "login";
    }

    @PostMapping(value = "/signup")
    public String signUp(MemberDetail memberDetail, BindingResult result){
        System.out.println("join process");
        Member member = new Member();
        member.setBirthDate(memberDetail.getBirthDate());
        member.setGender(memberDetail.getGender());
        member.setEmail(memberDetail.getUsername());
        member.setPassword(memberDetail.getPassword());
        member.setAuth("USER");
        userService.joinUser(member);
        return "redirect:/login";
    }

    @GetMapping(value = "/")
    public String userAccess(Model model, Authentication authentication){
        System.out.println("user access");
        MemberDetail memberDetail = (MemberDetail)authentication.getPrincipal();
        model.addAttribute("info", memberDetail.getUsername());
        return "userAccess";
    }

    // 추가
    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }
}
