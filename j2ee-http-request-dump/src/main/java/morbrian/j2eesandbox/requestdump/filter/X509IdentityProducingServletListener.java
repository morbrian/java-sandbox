package morbrian.j2eesandbox.requestdump.filter;

import javax.enterprise.inject.Produces;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

@WebListener
public class X509IdentityProducingServletListener implements ServletRequestListener {

    private static ThreadLocal<ServletRequest> SERVLET_REQUESTS = new ThreadLocal<ServletRequest>();

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        SERVLET_REQUESTS.set(sre.getServletRequest());
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        SERVLET_REQUESTS.remove();
    }

    private HttpServletRequest obtainHttp() {
        ServletRequest req = SERVLET_REQUESTS.get();
        return req instanceof HttpServletRequest ? (HttpServletRequest) req : null;
    }

    @Produces
    private X509Identity obtainX509Identity() {
        return X509Extraction.extractX509IdentityFromRequest(obtainHttp());
    }
}


