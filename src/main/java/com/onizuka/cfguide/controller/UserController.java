package com.onizuka.cfguide.controller;

import com.onizuka.cfguide.dto.UserSubmissionByDateRequest;
import com.onizuka.cfguide.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    public UserService userService;

    @RequestMapping("/")
    public String index() {
        return "helloWorld";
    }

    @PostMapping("/user/submission/date")
    public String getSubmissionByDate(@RequestBody UserSubmissionByDateRequest userSubmissionByDateRequest) {
        return "ok";
    }

    @PostMapping("/user/submission/date/count")
    public Long getSubmissionByDateCount(@RequestBody UserSubmissionByDateRequest userSubmissionByDateRequest) {
        return userService.getSubmissionByDateCount(userSubmissionByDateRequest);
    }

}
