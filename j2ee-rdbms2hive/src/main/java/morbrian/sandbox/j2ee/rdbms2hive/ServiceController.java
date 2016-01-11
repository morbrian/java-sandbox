package morbrian.sandbox.j2ee.rdbms2hive;

import com.fasterxml.jackson.databind.ObjectMapper;
import morbrian.sandbox.j2ee.rdbms2hive.database.Database;
import morbrian.sandbox.j2ee.rdbms2hive.model.NamesAndRecords;
import org.slf4j.Logger;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

@Singleton public class ServiceController {

  @Inject Database database;
  @Inject private Logger log;

  public ServiceController() {
  }

  public boolean isSchemaSupported(String schema) {
    return database.isSchemaSupported(schema);
  }

  public boolean isTableSupported(String tablename) {
    return database.isTableSupported(tablename);
  }


  public void processData(String schema, String category, String id) {
    Writer writer = new StringWriter();
    ObjectMapper mapper = new ObjectMapper();

    NamesAndRecords nars = database.fetchData("select * from " + schema + "." + category);
    List<String> fieldNames = nars.getFieldNames();
    List<List<Object>> records = nars.getRecords();
    log.info("Found " + records.size() + " records, each with " + fieldNames.size() + " fields.");

    try {
      appendNext(fieldNames, writer, mapper);
      for (List<Object> record : records) {
        appendNext(record, writer, mapper);
      }
      writer.close();
    } catch (IOException exc) {
      exc.printStackTrace();
    }

    log.info("OUTPUT:\n" + writer.toString());
  }

  public void appendNext(List<?> values, Writer writer, ObjectMapper om) throws IOException {
    boolean first = true;
    for (Object value : values) {
      if (first) {
        first = false;
      } else {
        writer.append(',');
      }
      om.writeValue(writer, value);
    }
    writer.append('\n');
    writer.flush();
  }

}
