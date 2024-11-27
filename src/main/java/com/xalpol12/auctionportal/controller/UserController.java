package com.xalpol12.auctionportal.controller;

import com.xalpol12.auctionportal.model.User;
import com.xalpol12.auctionportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> addUser(User user) {
        User result = userService.insert(user);
        return ResponseEntity.ok(result);
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUsers() {
        List<User> result = userService.selectAll();
        return ResponseEntity.ok(result);
    }
}
