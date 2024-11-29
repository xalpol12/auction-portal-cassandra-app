package com.xalpol12.auctionportal.repository.mappers;

import com.datastax.driver.core.Row;

public interface CassandraMapper<T, R> {
    T map(Row row);
    T map(R record);
    String getInsertStatement(String tableName);
}
