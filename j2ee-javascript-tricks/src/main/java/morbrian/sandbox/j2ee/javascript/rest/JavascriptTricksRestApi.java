package morbrian.sandbox.j2ee.javascript.rest;

import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;


@Path("/tricks") @RequestScoped public class JavascriptTricksRestApi {

  @Inject private Logger log;

  @GET @Path("/check") @Produces(MediaType.APPLICATION_JSON)
  public Response checkValuePage(@QueryParam("value") String value) {
    Map<String, String> map = new HashMap<>();
    map.put("method", "GET");
    map.put("value", value);
    return Response.ok(map).build();
  }

  @POST @Path("/check") @Produces(MediaType.APPLICATION_JSON)
  public Response checkValue(@QueryParam("value") String value) {
    Map<String, String> map = new HashMap<>();
    map.put("method", "POST");
    map.put("redirect", "static-page.html");
    map.put("value", value);
    return Response.ok(map).build();
  }
}
