package morbrian.j2eesandbox.requestdump.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

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
    ModifyHeaderServletRequestWrapper wrappedRequest =
        new ModifyHeaderServletRequestWrapper((HttpServletRequest) request);

    configureUserId(wrappedRequest);
    configureUserRole(wrappedRequest);

    chain.doFilter(wrappedRequest, response);
  }

  private void configureUserId(ModifyHeaderServletRequestWrapper wrappedRequest)
      throws ServletException {
    // we expect a cert to be provided, it has already been validated with OCSP.
    // we want this to fail if no certs were provided
    wrappedRequest.setUid("JOHN.DOE.123456");
    //        X509Certificate[] certChain = (X509Certificate[]) wrappedRequest.getRequest().getAttribute("javax.servlet.request.X509Certificate");
    //        try {
    //            String ldif = certChain[0].getSubjectDN().getName();
    //            String[] parts = ldif.split(",");
    //            for (int i = 0; i < parts.length; ++i) {
    //                String[] nvp = parts[i].split("=");
    //                if ("CN".equalsIgnoreCase(nvp[0])) {
    //                    wrappedRequest.setUid(nvp[1]);
    //                    return;
    //                }
    //            }
    //            // if we did not find the "CN" call out an exception
    //            throw new InvalidParameterException("Could not find CN value in X509 Certificate LDIF.");
    //        } catch (Exception exc) {
    //            if (certChain == null)
    //                throw new ServletException("No X509 Certificate Chain.");
    //            else if (certChain.length == 0)
    //                throw new ServletException("X509 Certificate Chain is empty.");
    //            else if (certChain[0].getSubjectDN() == null)
    //                throw new ServletException("X509 Certificate provided null subject DN.");
    //        }
  }

  private void configureUserRole(ModifyHeaderServletRequestWrapper wrappedRequest)
      throws ServletException {
    wrappedRequest.setRole("admin");
  }

  @Override public void destroy() {
    // nothing to do
  }
}
