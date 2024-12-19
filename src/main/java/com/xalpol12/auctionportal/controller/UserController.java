package com.xalpol12.auctionportal.controller;

import com.xalpol12.auctionportal.model.User;
import com.xalpol12.auctionportal.service.UserService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User.UserInput userInput) {
        User result = userService.insert(userInput);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> result = userService.selectAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable UUID id) {
        User user = userService.selectById(id);
        return ResponseEntity.ok(user);
    }
}
