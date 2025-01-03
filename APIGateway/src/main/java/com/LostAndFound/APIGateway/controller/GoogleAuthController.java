package com.LostAndFound.APIGateway.controller;


import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/auth/google")
public class GoogleAuthController {

    RestTemplate restTemplate;

    @GetMapping("/greet")
    public String greet() {
        return "Hello";
    }



}
