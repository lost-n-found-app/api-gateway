package com.LostAndFound.APIGateway.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class TwilioService {
    private static final Logger logger = LoggerFactory.getLogger(TwilioService.class);
    @Value("${twilio.account.sid}")
    public String ACCOUNT_SID;

    @Value("${twilio.auth.token}")
    public String AUTH_TOKEN;

    @Value("${twilio.phone.number}")
    String FROM_PHONE;

    public final Map<String, String> otpData = new HashMap<>();
    private String otpp;

    @PostConstruct
    public void init() {
        logger.info("Initializing Twilio with ACCOUNT_SID: {}", ACCOUNT_SID);
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }


    public String generateOTP() {


        return String.format("%06d", new Random().nextInt(999999));
    }
    public String sendOTP(String phoneNumber) {
        logger.info("Attempting to send OTP to phone number: {}", phoneNumber);
        String otp = generateOTP();

        try {

            Message.creator(
                            new PhoneNumber(phoneNumber),
                            new PhoneNumber(FROM_PHONE),
                            "Your OTP code is: " + otp
                    )
                    .create();
            logger.info("OTP sent successfully to {}", phoneNumber);
        } catch (Exception e) {
            logger.error("Error sending OTP to {}: {}", phoneNumber, e.getMessage(), e);
            e.printStackTrace();
            return "Error sending OTP";
        }
         otpData.put(phoneNumber,otp);

        return "OTP sent successfully";
    }


    public boolean verifyOTP(String phoneNumber, String otp) {
        String otpp = otpData.get("+"+phoneNumber);

        if (otpp == null) {
            logger.warn("No OTP found for phone number: {}", phoneNumber);
            return false;
        }

        if (otpp.equals(otp)) {
            otpData.remove("+"+phoneNumber);
            logger.info("OTP verified successfully for phone number: {}", phoneNumber);
            return true;
        }
        logger.warn("OTP verification failed for phone number: {}", phoneNumber);
        return false;
    }

}
