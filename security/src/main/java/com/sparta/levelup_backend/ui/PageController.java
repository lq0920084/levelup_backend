package com.sparta.levelup_backend.ui;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PageController {

    @GetMapping("/v2/signin")
    public String signInPage() {
        return "signin";
    }

    @GetMapping("/v2/signup")
    public String signUpUserPage() {
        return "signup";
    }

    @GetMapping("/v2/oauth2signup")
    public String oAuth2SignUpUserPage(Model model, HttpServletRequest request) {

        model.addAttribute("email", request.getAttribute("email"));
        model.addAttribute("nickName", request.getAttribute("nickName"));
        model.addAttribute("phoneNummber", request.getAttribute("phoneNumber"));

        return "oauth2signup";
    }


}
