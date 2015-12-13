package morbrian.sandbox.j2ee.cookie.rest;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;


@Path("/gogo") @RequestScoped public class CookieExampleRestApi {

  private static final String DEFAULT_GADGET = "Wowzers!";
  private static final Map<String, String> gadgetRepertoire = new HashMap<String, String>();

  static {
    gadgetRepertoire.put("A", "copter");
    gadgetRepertoire.put("B", "brella");
    gadgetRepertoire.put("C", "legs");
    gadgetRepertoire.put("D", "neck");
    gadgetRepertoire.put("E", "hands");
    gadgetRepertoire.put("F", "cuffs");
    gadgetRepertoire.put("G", "hammer");
  }

  @Inject private Logger log;

  @GET @Path("/pick/{gadgetId}") @Produces(MediaType.APPLICATION_JSON)
  public Response pickGadgetById(@PathParam("gadgetId") String gadgetId) {
    String gadget = activateGadgetForId(gadgetId);
    NewCookie gadgetCookie = new NewCookie("Gadget", gadget, "/", "", "comment", 100, false);
    return Response.ok("Picking Gadget: " + gadget, MediaType.TEXT_PLAIN_TYPE).cookie(gadgetCookie)
        .build();
  }

  @GET @Path("/gadget/go")
  public String goGoGadgetGo(@CookieParam(value = "Gadget") String gadget) {
    return "Go, Go gadget " + gadget;
  }

  private String activateGadgetForId(String gadgetId) {
    String result;
    if (gadgetRepertoire.containsKey(gadgetId)) {
      result = gadgetRepertoire.get(gadgetId);
    } else {
      log.warn("No Gadget Found For ID = " + gadgetId);
      result = DEFAULT_GADGET;
    }
    return result;
  }
}
