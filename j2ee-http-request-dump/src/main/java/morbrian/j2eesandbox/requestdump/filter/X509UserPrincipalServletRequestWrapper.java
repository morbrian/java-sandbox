package morbrian.j2eesandbox.requestdump.filter;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;


public class X509UserPrincipalServletRequestWrapper extends HttpServletRequestWrapper {

 // private Principal principal;

  public X509UserPrincipalServletRequestWrapper(HttpServletRequest request)
      throws ServletException, LoginException {
    super(request);
    loginToSetPrincipal(request);
  }

  private void loginToSetPrincipal(HttpServletRequest request)
      throws ServletException, LoginException {
//    String username = X509Extraction.extractCnFromRequest(request);
    String username = X509Extraction.extractPrimaryLdifFromRequest(request);


    //username = "dev_moore";
    // fail if no user can be identified
    if (username == null) {
      throw new LoginException("No identity in certificate, unable to authenticate.");
    }

    Principal superPrincipal = super.getUserPrincipal();

    // if principal not set yet, or principal is different than current PKI certificate
    if (superPrincipal == null || !username.equals(superPrincipal.getName())) {
      if(request instanceof HttpServletRequest) {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        httpRequest.getSession();
        if (username != null) {
          try {
            httpRequest.login(username, "changeme");
          } catch(Exception exc) {
            throw new LoginException("Login failed for " + username);
          }
        }
      }
    }

  }
}
