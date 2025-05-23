/*
 * Copyright (c) 2025 Oracle, Inc.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl/
 */

package com.oracle.jdbc.samples.sessionlesstxns.jdbc;

import oracle.jdbc.OracleConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class CustomDataSource {

  private static final Logger logger = LoggerFactory.getLogger(CustomDataSource.class);

  public final DataSource connectionPool;
  private final Map<String, OracleConnection> connectionList;

  public CustomDataSource(DataSource ds) {
    connectionPool = ds;
    connectionList = new HashMap<>();
  }

  public Map.Entry<String, OracleConnection> getNewConnection() throws SQLException {
    OracleConnection conn = (OracleConnection) connectionPool.getConnection();
    String uuid = UUID.randomUUID().toString();

    logger.info("Number of active connections is {}", connectionList.size());
    connectionList.put(uuid, conn);

    return Map.entry(uuid, conn);
  }

  public OracleConnection getConnection(String uuid) {
    return connectionList.get(uuid);
  }

  public void releaseConnection(String uuid) throws SQLException {
    connectionList.get(uuid).close();
    connectionList.remove(uuid);
  }
}
