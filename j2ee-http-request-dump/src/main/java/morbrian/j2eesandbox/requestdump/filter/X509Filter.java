package morbrian.j2eesandbox.requestdump.filter;

import javax.security.auth.login.LoginException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by morbrian on 8/23/14.
 */
public class X509Filter implements Filter {

  @Override public void init(FilterConfig filterConfig) throws ServletException {
    // nothing to do
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    X509UserPrincipalServletRequestWrapper wrappedRequest = null;
    try {
      wrappedRequest = new X509UserPrincipalServletRequestWrapper((HttpServletRequest) request);
    } catch(LoginException exc) {
      if (response instanceof HttpServletResponse) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, exc.getMessage());
      }
    } catch(ServletException exc) {
      if (response instanceof HttpServletResponse) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exc.getMessage());
      }
    }
    if (wrappedRequest != null) {
      chain.doFilter(wrappedRequest, response);
    }
  }

  @Override public void destroy() {
    // nothing to do
  }
}
