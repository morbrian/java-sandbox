package morbrian.sandbox.j2ee.rdbms2hive.rest;

import morbrian.sandbox.j2ee.rdbms2hive.ServiceController;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;


@Path("/csv") @RequestScoped public class CsvRestApi {

  @Inject ServiceController controller;
  @Inject private Logger log;

  @GET @Path("/produce/{schema}/{tablename}/{id}") @Produces(MediaType.APPLICATION_JSON)
  public String produceCsvData(@PathParam("schema") final String schema,
      @PathParam("tablename") final String tablename, @PathParam("id") final String id) {

    StringBuilder sb = new StringBuilder();
    sb.append("\"" + schema + "\", \"" + tablename + "\", \"" + id + "\"\n");
    sb.append("\"Age of Ultron\", \"PG-13\", \"Science Fiction\"\n");
    sb.append("\"Peanuts the Movie\", \"PG\", \"Family\"\n");
    sb.append("\"Short Term 12\", \"R\", \"Drama\"\n");
    sb.append("\"Jurassic Park\", \"PG-13\", \"Adventure\"\n");

    log.info(sb.toString());

    return sb.toString();
  }

  @GET @Path("/process/{schema}/{tablename}") @Produces(MediaType.APPLICATION_JSON)
  public Map<String, String> processRdbmsData(@PathParam("schema") final String schema,
      @PathParam("tablename") final String tablename) {

    Map<String, String> result = prepareResult(schema, tablename);
    if (result.containsKey("error")) {
      return result;
    }

    String id = UUID.randomUUID().toString();
    result.put("id", id);

    //TODO: instead of thread, show example of MDB
    Executors.defaultThreadFactory().newThread(new Runnable() {
      @Override public void run() {
        controller.processData(schema, tablename, id);
      }
    }).start();

    return result;
  }

  private Map<String, String> prepareResult(String schema, String tablename) {
    Map<String, String> result = new HashMap<>();
    result.put("schema", schema);
    result.put("table", tablename);

    if (!controller.isSchemaSupported(schema)) {
      result.put("error", "invalid schema: " + schema);
      return result;
    }

    if (!controller.isTableSupported(tablename)) {
      result.put("error", "invalid table: " + tablename);
      return result;
    }

    return result;
  }

}

