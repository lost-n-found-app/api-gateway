package com.LostAndFound.APIGateway.controller;

import com.LostAndFound.APIGateway.services.YugabyteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/db")
public class YugabyteController {

    private static final Logger logger = LoggerFactory.getLogger(YugabyteController.class);
    @Autowired
    YugabyteService yugabyteService;

    @GetMapping("/cleanup-idle")
    public String cleanupIdleConnections() {
        logger.info("Entering cleanupIdleConnections method");
        return yugabyteService.terminateIdleConnections();
    }
}
