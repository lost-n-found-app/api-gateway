package com.LostAndFound.APIGateway.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class TwilioService {

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

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }


    public String generateOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }
    public String sendOTP(String phoneNumber) {
        String otp = generateOTP();

        try {

            Message.creator(
                            new PhoneNumber(phoneNumber),
                            new PhoneNumber(FROM_PHONE),
                            "Your OTP code is: " + otp
                    )
                    .create();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending OTP";
        }
         otpData.put(phoneNumber,otp);

        return "OTP sent successfully";
    }


    public boolean verifyOTP(String phoneNumber, String otp) {
        String otpp = otpData.get("+"+phoneNumber);

        if (otpp == null) {
            return false;
        }

        if (otpp.equals(otp)) {
            otpData.remove("+"+phoneNumber);
            return true;
        }

        return false;
    }

}
