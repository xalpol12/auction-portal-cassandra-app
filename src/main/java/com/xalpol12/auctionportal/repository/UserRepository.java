package com.xalpol12.auctionportal.repository;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.xalpol12.auctionportal.model.User;
import com.xalpol12.auctionportal.repository.mappers.CassandraMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private static final String TABLE_NAME = "USERS";
    private final Session session;
    private final CassandraMapper<User, User.UserInput> userMapper;

    private PreparedStatement INSERT_INTO_USERS;
    private PreparedStatement SELECT_USER_BY_ID;

    public User insert(User.UserInput userInput) {
        BoundStatement bsInsert = new BoundStatement(INSERT_INTO_USERS);
        User user = userMapper.map(userInput);
        bsInsert.bind(user.getId(), user.getName(), user.getAuctions());
        ResultSet result = session.execute(bsInsert);
        if (result.wasApplied()) {
            return user;
        } else {
            throw new RuntimeException("User insert failed");
        }
    }

    public List<User> selectAll() {
        Select select = QueryBuilder.select().all().from(TABLE_NAME);
        ResultSet result = session.execute(select);
        List<User> users = new ArrayList<>();

        result.forEach(x -> users.add(userMapper.map(x)));

        return users;
    }

    public User selectById(UUID id) {
        BoundStatement bsSelect = new BoundStatement(SELECT_USER_BY_ID);
        bsSelect.bind(id);

        ResultSet result = session.execute(bsSelect);
        return userMapper.map(result.one());
    }

    public User addAuction(UUID auctionId, UUID userId) {
        User user = selectById(userId);
        user.getAuctions().add(auctionId);
        BoundStatement bsUpdate = new BoundStatement(INSERT_INTO_USERS);
        bsUpdate.bind(user.getId(), user.getName(), user.getAuctions());
        ResultSet result = session.execute(bsUpdate);
        if (result.wasApplied()) {
            return user;
        } else {
            throw new RuntimeException("User update failed");
        }
    }

    @PostConstruct
    private void init() {
        INSERT_INTO_USERS = session.prepare(userMapper.getInsertStatement(TABLE_NAME));
        SELECT_USER_BY_ID = session.prepare(
                new StringBuilder("SELECT * FROM ")
                        .append(TABLE_NAME)
                        .append(" WHERE id = ?")
                        .toString()
        );
    }
}
