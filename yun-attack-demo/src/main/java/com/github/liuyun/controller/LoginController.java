package com.github.liuyun.controller;

import com.github.liuyun.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public Boolean login(String username, String password) {
        return userService.login(username, password);
    }

}
