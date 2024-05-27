package org.example.demojwt.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.demojwt.service.MyUserDetailsService;
import org.example.demojwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/authenticate")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @PostMapping
    public ResponseEntity<?> createAuthenticationToken(@RequestHeader("Authorization") String authHeader) throws Exception {
        log.info("New Auth request! Header:" + authHeader);
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            throw new RuntimeException("Missing or incorrect Authorization header");
        }

        String base64Credentials = authHeader.substring("Basic ".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] userDetails = credentials.split(":", 2);

        if (userDetails.length != 2) {
            throw new RuntimeException("Invalid Authorization header");
        }

        String username = userDetails[0];
        String password = userDetails[1];

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetailsObj = userDetailsService.loadUserByUsername(username);
        final String jwt = jwtUtil.generateToken(userDetailsObj);
        log.info("jwt token returned: " + jwt);
        return ResponseEntity.ok(jwt);
    }
}
