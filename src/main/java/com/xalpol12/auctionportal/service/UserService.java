package com.xalpol12.auctionportal.service;

import com.xalpol12.auctionportal.model.User;
import com.xalpol12.auctionportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User insert(User.UserInput userInput) {
        return userRepository.insert(userInput);
    }

    public List<User> selectAll() {
        return userRepository.selectAll();
    }

    public User selectById(UUID id) {
        return userRepository.selectById(id);
    }
}
