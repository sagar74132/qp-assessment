package com.qp.qpassessment.utils;

import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PasswordEncoderUtils {

    public static String encode(String input) {
        try {
            PasswordEncoder encoder = new BCryptPasswordEncoder();

            return encoder.encode(input);
        } catch (Exception e) {
            throw new RuntimeException("Error generating MD5 hash", e);
        }
    }
}

