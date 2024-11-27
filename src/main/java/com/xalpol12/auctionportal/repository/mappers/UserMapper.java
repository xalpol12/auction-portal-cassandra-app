package com.xalpol12.auctionportal.repository.mappers;

import com.datastax.driver.core.Row;
import com.xalpol12.auctionportal.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper implements CassandraMapper<User> {

    @Override
    public User map(Row row) {
        return null;
    }

    @Override
    public String getInsertStatement(String tableName) {
        return "";
    }
}
