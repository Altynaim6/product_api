package com.example.product_api.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OAuth2LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // thymeleaf шаблон login.html
    }

    @GetMapping("/login/success")
    public String loginSuccess(Model model, Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        model.addAttribute("name", oAuth2User.getAttribute("name"));
        model.addAttribute("email", oAuth2User.getAttribute("email"));
        return "loginSuccess"; // thymeleaf шаблон loginSuccess.html
    }
    @GetMapping("/home")
    public String home(@RequestParam(required = false) String token, Model model) {
        model.addAttribute("token", token);
        return "home"; // thymeleaf -> home.html
    }

}
