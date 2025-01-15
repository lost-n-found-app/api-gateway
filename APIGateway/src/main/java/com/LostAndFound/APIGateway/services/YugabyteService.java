package com.LostAndFound.APIGateway.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class YugabyteService {

    private static final Logger logger = LoggerFactory.getLogger(YugabyteService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String terminateIdleConnections() {
        String terminateQuery = """
            SELECT pg_terminate_backend(pid) 
            FROM pg_stat_activity 
            WHERE state = 'idle' 
              AND pid <> pg_backend_pid();
            """;

        try {
            logger.info("Attempting to terminate idle connections...");
            jdbcTemplate.execute(terminateQuery);
            logger.info("Idle connections terminated successfully.");
            return "Successfully terminated idle connections.";
        } catch (Exception e) {
            logger.error("Failed to terminate idle connections: {}", e.getMessage(), e);
            return "Failed to terminate idle connections: " + e.getMessage();
        }
    }
}
