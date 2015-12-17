package morbrian.j2eesandbox.requestdump.xfilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class X509IdentityCheckPointFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // nothing to do
    }

    // Check to see if we have a certificate.
    // Proceed with the request through the chain if we have a cert,
    // otherwise redirect to configured page for missing certificate.
    // Assumes verification of the X509 cert happened during earlier processing.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = null;
        if(request instanceof HttpServletRequest) {
            httpRequest = (HttpServletRequest)request;
        }
        X509Identity certIdentity = X509Extraction.extractX509IdentityFromRequest(httpRequest);

        if(!certIdentity.hasCert()) {
            System.err.println("NO CERTIFICATE AVAILABLE");
        }
        chain.doFilter(request, response);

//        if(certIdentity.hasCert()) {
//            chain.doFilter(request, response);
//        } else {
//            Config config = ConfigFactory.load();
//            Boolean redirectPathInContext = config.getBoolean("auth-account-request.x509CheckPoint.missingX509CertificateRedirectInContext");
//            String redirectSetting = config.getString("auth-account-request.x509CheckPoint.missingX509CertificateRedirect");
//            String requestedPath = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
//
//            // if the request is already asking for what we would redirect to, then just continue the chain.
//            if(!redirectPathInContext || requestedPath.equals(redirectSetting)) {
//                chain.doFilter(request, response);
//            } else {
//                HttpServletResponse httpResponse = null;
//                if(response instanceof HttpServletResponse) {
//                    httpResponse = (HttpServletResponse)response;
//                }
//                String redirectPath;
//                if(redirectPathInContext) {
//                    redirectPath = httpRequest.getContextPath() + redirectSetting;
//                } else {
//                    redirectPath = redirectSetting;
//                }
//                if (httpResponse != null) {
//                    httpResponse.sendRedirect(redirectPath);
//                }
//            }
//        }
    }

    @Override
    public void destroy() {
        // nothing to do
    }
}
