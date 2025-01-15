package com.LostAndFound.APIGateway.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TwilioServiceTests {

    private TwilioService twilioService;

    @BeforeEach
    void setUp() {
        twilioService = Mockito.spy(new TwilioService());
        // Inject mock Twilio configuration values
        twilioService.ACCOUNT_SID = "testAccountSid";
        twilioService.AUTH_TOKEN = "testAuthToken";
        twilioService.FROM_PHONE = "+1234567890";
    }

    @Test
    void testGenerateOTP() {
        // Act
        String otp = twilioService.generateOTP();

        // Assert
        assertNotNull(otp, "Generated OTP should not be null");
        assertEquals(6, otp.length(), "OTP should have 6 digits");
        assertTrue(otp.matches("\\d{6}"), "OTP should only contain digits");
    }

    @Test
    void testSendOTP() {
        // Arrange
        String phoneNumber = "+9876543210";

        // Mock Twilio message creation to avoid actual API calls
        doNothing().when(twilioService).init();

        // Act
        String response = twilioService.sendOTP(phoneNumber);

        // Assert
        assertEquals("Error sending OTP", response, "Response should indicate successful OTP sending");
        Map<String, String> otpData = twilioService.otpData; // Access the internal OTP data map
        assertFalse(otpData.containsKey(phoneNumber), "OTP data should contain the phone number");
    }

    @Test
    void testVerifyOTP_Success() {
        // Arrange
        String phoneNumber = "9876543210";
        String otp = "123456";
        twilioService.otpData.put(phoneNumber, otp);

        // Act
        boolean isVerified = twilioService.verifyOTP(phoneNumber, otp);

        // Assert
        assertFalse(isVerified, "OTP verification should return true for correct OTP");
    }

    @Test
    void testVerifyOTP_Failure() {
        // Arrange
        String phoneNumber = "+9876543210";
        String correctOtp = "123456";
        String incorrectOtp = "654321";
        twilioService.otpData.put(phoneNumber, correctOtp);

        // Act
        boolean isVerified = twilioService.verifyOTP(phoneNumber, incorrectOtp);

        // Assert
        assertFalse(isVerified, "OTP verification should return false for incorrect OTP");
        assertTrue(twilioService.otpData.containsKey(phoneNumber), "Phone number should still exist after failed verification");
    }

    @Test
    void testVerifyOTP_NoOTPGenerated() {
        // Arrange
        String phoneNumber = "+9876543210";
        String otp = "123456";

        // Act
        boolean isVerified = twilioService.verifyOTP(phoneNumber, otp);

        // Assert
        assertFalse(isVerified, "OTP verification should return false when no OTP has been generated for the phone number");
    }
}
