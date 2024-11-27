package com.xalpol12.auctionportal.repository;

import com.xalpol12.auctionportal.model.User;
import com.xalpol12.auctionportal.repository.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final UserMapper userMapper;

    public User insert(User user) {
    }

    public List<User> selectAll() {
    }
}
