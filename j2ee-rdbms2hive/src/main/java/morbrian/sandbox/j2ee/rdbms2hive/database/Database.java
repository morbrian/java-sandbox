package morbrian.sandbox.j2ee.rdbms2hive.database;

import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.sql.DataSource;

import morbrian.sandbox.j2ee.rdbms2hive.model.NamesAndRecords;

/**
 * Created by hikethru08 on 11/15/15.
 */
@Stateless public class Database {

  private static Set<String> SUPPORTED_SCHEMAS =
      Collections.unmodifiableSet(new HashSet(Arrays.asList("public")));
  private static Set<String> SUPPORTED_TABLES =
      Collections.unmodifiableSet(new HashSet(Arrays.asList("tablenames")));
  @Resource(mappedName = "java:/DocumentDS") DataSource dataSource;
  @Inject private Logger log;

  public Database() {
  }

  public boolean isSchemaSupported(String schema) {
    return SUPPORTED_SCHEMAS.contains(schema);
  }

  public boolean isTableSupported(String tablename) {
    return SUPPORTED_TABLES.contains(tablename);
  }

  public NamesAndRecords fetchData(String query) {
    // TODO: make this method take a closure so we can specify an operation to perform
    // TODO: explore fetch size performance
    // for a certain batch size of records.
    List<String> fieldNames = new ArrayList<>();
    List<List<Object>> records = new ArrayList<>();

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet resultSet = null;
    ResultSetMetaData metaData = null;

    try {
      conn = dataSource.getConnection();
      stmt = conn.prepareStatement(query);
      resultSet = stmt.executeQuery();

      metaData = resultSet.getMetaData();
      int columnCount = metaData.getColumnCount();
      // remember that MetaData columns are indexed from 1

      for (int i = 1; i <= columnCount; i++) {
        fieldNames.add(metaData.getColumnName(i));
        log.info(
            "Col " + metaData.getColumnName(i) + " MetaData: " + metaData.getColumnTypeName(i));
      }

      while (resultSet.next()) {
        List<Object> record = new ArrayList<>();
        // remember that MetaData columns are indexed from 1
        for (int i = 1; i <= columnCount; i++) {
          // TODO: handle types appropriately (probably don't want SQL types)
          // TODO: easiest way is probably to use template from OpenCSV to get case statement with all SQL types and handle
          Object obj = resultSet.getObject(i);
          log.info("Col " + metaData.getColumnName(i) + " Java Type: " + (obj == null ?
              "null" :
              obj.getClass().getName()));
          record.add(obj);
        }
        records.add(record);
      }

    } catch (SQLException exc) {
      log.error("", exc);
    } finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException exc2) {
        }
      }
      if (stmt != null) {
        try {
          stmt.close();
        } catch (SQLException exc2) {
        }
      }
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException exc2) {
        }
      }
    }

    return new NamesAndRecords(fieldNames, records);

  }


}
