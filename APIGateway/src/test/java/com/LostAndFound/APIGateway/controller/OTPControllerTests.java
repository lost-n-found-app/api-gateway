package com.LostAndFound.APIGateway.controller;

import com.LostAndFound.APIGateway.services.JWTService;
import com.LostAndFound.APIGateway.services.TwilioService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OTPControllerTests {
    @Mock
    private TwilioService twilioService;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private OTPController otpController;

    public OTPControllerTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendOTP() {
        String phoneNumber = "1234567890";
        String responseMessage = "OTP sent successfully!";
        when(twilioService.sendOTP("+" + phoneNumber)).thenReturn(responseMessage);

        ResponseEntity<String> response = otpController.sendOTP(phoneNumber);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseMessage, response.getBody());
}

    @Test
    void testVerifyOTP_Success() {
        String phoneNumber = "1234567890";
        String otp = "123456";
        String jwtToken = "mock-jwt-token";

        when(twilioService.verifyOTP(phoneNumber, otp)).thenReturn(true);
        when(jwtService.generateToken(phoneNumber)).thenReturn(jwtToken);
        ResponseEntity<String> response = otpController.verifyOTP(phoneNumber,otp);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(jwtToken, response.getBody());
    }

    @Test
    void testVerifyOTP_InvalidOTP() {
        String phoneNumber = "1234567890";
        String otp = "123456";

        when(twilioService.verifyOTP(phoneNumber, otp)).thenReturn(false);
        ResponseEntity<String> response = otpController.verifyOTP(phoneNumber, otp);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid or expired OTP!", response.getBody());
    }
}
