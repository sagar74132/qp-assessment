package com.qp.qpassessment.utils;

import lombok.NoArgsConstructor;

import java.security.MessageDigest;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class MD5Util {

    public static String generateMD5Hash(String input) {
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Add password bytes to digest
            md.update(input.getBytes());

            // Get the hash's bytes
            byte[] digest = md.digest();

            // Convert bytes to hexadecimal format
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error generating MD5 hash", e);
        }
    }
}

