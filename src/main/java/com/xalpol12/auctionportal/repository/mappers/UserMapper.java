package com.xalpol12.auctionportal.repository.mappers;

import com.datastax.driver.core.Row;
import com.xalpol12.auctionportal.model.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserMapper implements CassandraMapper<User> {

    @Override
    public User map(Row row) {
        return User.builder()
                .id(row.getUUID("id"))
                .name(row.getString("name"))
                .auctions(row.getSet("auctions", UUID.class))
                .build();
    }

    @Override
    public String getInsertStatement(String tableName) {
        return "INSERT INTO " +
                tableName +
                " (id, name, auctions) " +
                "VALUES " +
                "(?, ?, ?)";
    }
}
