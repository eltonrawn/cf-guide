package com.onizuka.cfguide.controller;

import com.onizuka.cfguide.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    public UserService userService;

    @RequestMapping("/")
    public String index() {
        return userService.getSubmission();
    }
}
