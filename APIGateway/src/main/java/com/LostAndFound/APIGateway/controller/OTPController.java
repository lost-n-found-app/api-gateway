package com.LostAndFound.APIGateway.controller;

import com.LostAndFound.APIGateway.services.JWTService;
import com.LostAndFound.APIGateway.services.TwilioService;
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

    @Autowired
    private TwilioService twilioService;

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOTP(@RequestParam String phoneNumber) {
        phoneNumber="+"+phoneNumber;
        String response = twilioService.sendOTP(phoneNumber);
        return ResponseEntity.ok(response);
    }


    private String generateOTP() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestParam String phoneNumber, @RequestParam String otp) {

        boolean isVerified =true;
                isVerified=twilioService.verifyOTP(phoneNumber, otp);

        if (isVerified!=false) {

            String token=jwtService.generateToken(phoneNumber);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(400).body("Invalid or expired OTP!");
        }
    }
}
