package morbrian.j2eesandbox.requestdump.rest;

import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hikethru08 on 12/20/15.
 */
public class RestHelper {

  public static Response createResponse(String tag, Principal principal) {
    Map<String, String> stuff = new HashMap<>();
    stuff.put("tag", tag);
    try {
      stuff.put("user", (principal != null) ? principal.getName() : "null");
    } catch(Exception exc) {
      stuff.put("error", "can't get username: " + exc.getMessage());
    }
    stuff.put("timestamp", Long.toString(System.currentTimeMillis()));
    return Response.ok(stuff).build();
  }
}
