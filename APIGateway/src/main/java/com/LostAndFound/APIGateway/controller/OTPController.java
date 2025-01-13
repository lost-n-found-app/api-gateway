package com.LostAndFound.APIGateway.controller;

import com.LostAndFound.APIGateway.services.JWTService;
import com.LostAndFound.APIGateway.services.TwilioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class OTPController {

    @Autowired
    JWTService  jwtService;

    private static final Logger logger = LoggerFactory.getLogger(OTPController.class);

    @Autowired
    private TwilioService twilioService;

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOTP(@RequestParam String phoneNumber) {
        logger.info("Received request to send OTP to phone number: {}", phoneNumber);
        phoneNumber="+"+phoneNumber;
        String response = twilioService.sendOTP(phoneNumber);
        return ResponseEntity.ok(response);
    }


    private String generateOTP() {
        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestParam String phoneNumber, @RequestParam String otp) {
        logger.info("Received request to verify OTP for phone number: {}", phoneNumber);


        boolean isVerified =true;
                isVerified=twilioService.verifyOTP(phoneNumber, otp);

        if (isVerified!=false) {
            logger.info("OTP verified successfully for phone number: {}", phoneNumber);
            String token=jwtService.generateToken(phoneNumber);
            logger.debug("Generated JWT token for phone number: {}", phoneNumber);
            return ResponseEntity.ok(token);
        } else {
            logger.warn("Invalid or expired OTP for phone number: {}", phoneNumber);
            return ResponseEntity.status(400).body("Invalid or expired OTP!");
        }
    }
}
