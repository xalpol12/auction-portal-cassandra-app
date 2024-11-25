package com.xalpol12.auctionportal.repository.mappers;

import com.datastax.driver.core.Row;

public interface CassandraMapper<T> {
    T map(Row row);
    String getInsertStatement(String tableName);
}
