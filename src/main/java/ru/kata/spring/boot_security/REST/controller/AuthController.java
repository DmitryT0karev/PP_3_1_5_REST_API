package ru.kata.spring.boot_security.REST.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.REST.model.User;
import ru.kata.spring.boot_security.REST.security.PersonDetails;


@RestController
public class AuthController {

    @GetMapping("/userInfo")
    public ResponseEntity<User> showUserDetails(Authentication authentication) {
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return new ResponseEntity<>(personDetails.getUser(), HttpStatus.OK);
    }
}
