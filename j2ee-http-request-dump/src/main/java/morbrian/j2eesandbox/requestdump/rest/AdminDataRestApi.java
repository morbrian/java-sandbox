package morbrian.j2eesandbox.requestdump.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Principal;

// these authorization tags aren't working, not sure why
//@RolesAllowed({"admin"})
@Path("/admin") @RequestScoped public class AdminDataRestApi {

  @Inject private Principal principal;
  //@Inject private Logger log;

  @GET @Path("/data") @Produces(MediaType.APPLICATION_JSON)
  public Response getData() {
    return RestHelper.createResponse("ADMIN", principal);
  }

}
