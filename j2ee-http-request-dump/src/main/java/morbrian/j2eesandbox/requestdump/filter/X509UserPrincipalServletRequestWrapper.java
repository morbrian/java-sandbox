package morbrian.j2eesandbox.requestdump.filter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;


public class X509UserPrincipalServletRequestWrapper extends HttpServletRequestWrapper {

 // private Principal principal;

  public X509UserPrincipalServletRequestWrapper(HttpServletRequest request) throws ServletException {
    super(request);
    loginToSetPrincipal(request);
  }

  private void loginToSetPrincipal(HttpServletRequest request) throws ServletException {
    String username = X509Extraction.extractCnFromRequest(request);
    if (username != null) {
      //TODO: CSAGA only matches lower case usernames
      username = username.toLowerCase();
    }

    // fail if no user can be identified
    if (username == null) {
      throw new IllegalArgumentException("No common name in PKI certificate, unable to authenticate.");
    }

    Principal superPrincipal = super.getUserPrincipal();

    // if principal not set yet, or principal is different than current PKI certificate
    if (superPrincipal == null || !username.equals(superPrincipal.getName())) {
      if(request instanceof HttpServletRequest) {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        httpRequest.getSession();
        if (username != null) {
          username = "dev_moore";
          httpRequest.login(username, "changeme");
        }
      }
    }

  }
}
