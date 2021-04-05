package com.tu.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpSession session){
        String referrer = request.getHeader("Referer");
        String requestUrl = (String) session.getAttribute("url_prior_login");
        if(requestUrl != null){
        request.getSession().setAttribute("url_prior_login", referrer);
        }
        return "login";
    }
}
