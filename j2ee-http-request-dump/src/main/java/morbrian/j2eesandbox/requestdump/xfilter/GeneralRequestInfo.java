package morbrian.j2eesandbox.requestdump.xfilter;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Created by bmoriarty on 11/20/15.
 */
public class GeneralRequestInfo {
    private String serverName;
    private int serverPort;
    private String scheme;
    private String protocol;
    private String pathInfo;
    private String pathTranslated;
    private String servletPath;
    private String contextPath;
    private String requestUri;
    private String authType;
    private String remoteUser;
    private String method;
    private URI baseUri;

    public GeneralRequestInfo(HttpServletRequest request) {
        serverName     = request.getServerName();
        // ie. dev.jigsaw.ctisl.gtri.gatech.edu
        serverPort     = request.getServerPort();
        // ie. 443
        scheme         = request.getScheme();
        // ie. https
        protocol       = request.getProtocol();
        // ie. HTTP/1.1
        pathInfo       = request.getPathInfo();
        //ie. null or remaining path info after target servlet
        pathTranslated = request.getPathTranslated();
        // ie. null, not sure
        servletPath    = request.getServletPath();
        // ie. /servelet, part after the host and container
        contextPath    = request.getContextPath();
        // ie. /request-dump, just the container mapping without host or servlet
        requestUri     = request.getRequestURI();
        // ie. /request-dump/servlet, the path without host
        authType       = request.getAuthType();
        //ie. LOGIN - but not quite sure
        remoteUser     = request.getRemoteUser();
        // ie. CAC identity common name
        method         = request.getMethod();
        // ie. GET / POST / etc...

        UriBuilder builder = UriBuilder.fromPath(contextPath);
        builder.scheme(scheme);
        builder.port(serverPort);
        builder.host(serverName);
        baseUri = builder.build();
    }


    public String getServerName() {
        return serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getScheme() {
        return scheme;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getPathInfo() {
        return pathInfo;
    }

    public String getPathTranslated() {
        return pathTranslated;
    }

    public String getServletPath() {
        return servletPath;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getAuthType() {
        return authType;
    }

    public String getRemoteUser() {
        return remoteUser;
    }

    public String getMethod() {
        return method;
    }

    public URI getBaseUri() {
        return baseUri;
    }

}
