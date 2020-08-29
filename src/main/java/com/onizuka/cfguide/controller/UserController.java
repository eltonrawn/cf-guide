package com.onizuka.cfguide.controller;

import com.onizuka.cfguide.dto.UserSubmissionByDateRequest;
import com.onizuka.cfguide.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    public UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/submission/date/count")
    public Long getSubmissionByDateCount(@RequestBody UserSubmissionByDateRequest userSubmissionByDateRequest) {
        return userService.getSubmissionByDateCount(userSubmissionByDateRequest);
    }

}
