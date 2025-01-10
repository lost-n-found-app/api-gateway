package com.LostAndFound.APIGateway.controller;

import com.LostAndFound.APIGateway.services.YugabyteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/db")
public class YugabyteController {
    @Autowired
    YugabyteService yugabyteService;

    @GetMapping("/cleanup-idle")
    public String cleanupIdleConnections() {
        return yugabyteService.terminateIdleConnections();
    }

}
