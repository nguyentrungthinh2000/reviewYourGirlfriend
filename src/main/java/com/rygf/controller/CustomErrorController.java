package com.rygf.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomErrorController {
    
    @GetMapping("/error")
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
        int httpErrorCode = (Integer) httpRequest
            .getAttribute("javax.servlet.error.status_code");
        
        ModelAndView view = new ModelAndView("error");
        view.addObject("code", httpErrorCode);
        StringBuilder builder = new StringBuilder();
        switch (httpErrorCode) {
            case 403:
                Authentication auth
                    = SecurityContextHolder.getContext().getAuthentication();
                builder.append("Don\'t worry this is just because your account aren\'t authorized to be in this zone\n");
                builder.append("Your account : " + auth.getName() + " | Permission : " + auth.getAuthorities());
                view.addObject("message", builder.toString());
                break;
            case 404:
                builder.append("Your page can\'t be found !");
                view.addObject("message", builder.toString());
                break;
        }
        
        return view;
    }
    
}
