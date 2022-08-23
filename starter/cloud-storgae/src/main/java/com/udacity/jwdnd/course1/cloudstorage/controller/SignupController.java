package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String signupView() {
        return "signup";
    }

    @PostMapping
    public String signupSend(@ModelAttribute User user, Model model) {
        String error = null;

        if(userService.isExist(user.getUsername())) {
            error = "This username already exist!";
        }

        if(error == null) {
            int generatedID = userService.createUser(user);
            if(generatedID < 0) {
                error = "Can't create new user. Please try again!";
            }
        }

        if(error == null) {
            model.addAttribute("success", true);
            return "login";
        } else {
            model.addAttribute("error", error);
        }
        return "signup";
    }
}
