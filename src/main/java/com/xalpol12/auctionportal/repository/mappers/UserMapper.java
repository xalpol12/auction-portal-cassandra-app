package com.xalpol12.auctionportal.repository.mappers;

import com.datastax.driver.core.Row;
import com.xalpol12.auctionportal.model.User;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class UserMapper implements CassandraMapper<User, User.UserInput> {

    @Override
    public User map(Row row) {
        return User.builder()
                .id(row.getUUID("id"))
                .name(row.getString("name"))
                .auctions(row.getSet("auctions", UUID.class))
                .build();
    }

    @Override
    public User map(User.UserInput record) {
        return User.builder()
                .id(UUID.randomUUID())
                .name(record.name())
                .auctions(Set.of())
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
