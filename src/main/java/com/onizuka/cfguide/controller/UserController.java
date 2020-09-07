package com.onizuka.cfguide.controller;

import com.onizuka.cfguide.dto.UserSubmissionByDateRequest;
import com.onizuka.cfguide.dto.UserSubmissionByDateResponse;
import com.onizuka.cfguide.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// TODO Better to use web config class to enable cross origin
@CrossOrigin
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{handle}/submissions/count")
    public UserSubmissionByDateResponse getSubmissionByDateCount(@PathVariable String handle,
                                         @RequestParam(name = "days", defaultValue = "1") Long days) {
        return userService.getSubmissionByDate(new UserSubmissionByDateRequest(handle, days));
    }

}
