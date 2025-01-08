package com.LostAndFound.APIGateway.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/auth/google")
public class GoogleAuthController {

    RestTemplate restTemplate;

    @GetMapping("/greet")
    public String greet() {
        return "Hello";
    }



}
