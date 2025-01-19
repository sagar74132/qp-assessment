package com.qp.qpassessment.controller;


import com.qp.qpassessment.model.LoginRequest;
import com.qp.qpassessment.model.LoginResponse;
import com.qp.qpassessment.security.JwtHelper;
import com.qp.qpassessment.utils.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class LoginController {

    private final AuthenticationManager manager;
    private final UserDetailsService userDetailsService;
    private final JwtHelper helper;
    private final AppConfig appConfig;

    @Autowired
    public LoginController(final AuthenticationManager manager,
                           final UserDetailsService userDetailsService,
                           final JwtHelper helper,
                           final AppConfig appConfig) {
        this.manager = manager;
        this.userDetailsService = userDetailsService;
        this.helper = helper;
        this.appConfig = appConfig;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        this.doAuthenticate(request.getEmail(), request.getPassword());


        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);

        LoginResponse response = LoginResponse.builder()
                .message(appConfig.getProperty("login.success"))
                .token(token)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }
}
