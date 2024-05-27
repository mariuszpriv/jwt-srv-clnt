package org.example.demojwt.util;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class GenerateJwtToken {

    public static void main(String[] args) {
        JwtUtil jwtUtil = new JwtUtil();

        UserDetails userDetails = User.withUsername("staticUser")
                .password("dummyPassword")
                .roles("USER")
                .build();

        String token = jwtUtil.generateToken(userDetails);
        System.out.println("Generated Token: " + token);

        //basic auth
        String credentials = new String(Base64.getEncoder().encode("staticUser:dummyPassword".getBytes(StandardCharsets.UTF_8)));
        System.out.println("Credentials basic: "+ credentials);
    }
}
