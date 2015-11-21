package morbrian.j2eesandbox.requestdump.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by morbrian on 8/26/14.
 */
public class CsvRequestDumpFilter implements Filter {
    private static final String EOL = "\n"; // end of line
    private static final String INDENT = "\t"; // line indent
    private static final String PAR = "\n=========\n"; // paragraph
    private static final String NVP = " = "; // name = value for example
    private static final String VSEP = ", "; // value separator

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // nothing to do
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String content = dumpRequestContent((HttpServletRequest)request);
        System.out.println(content);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // nothing to do
    }

    private String nvpOut(String name, String value) {
        StringBuilder buf = new StringBuilder();
        buf.append(INDENT).append(name).append(NVP).append(value).append(EOL);
        return buf.toString();
    }

    private String dumpRequestContent(HttpServletRequest request) {

        StringBuilder buf = new StringBuilder();

        // ----------
        buf.append(PAR).append("GENERAL").append(EOL);

        buf.append(nvpOut("ServerName", request.getServerName()));
        buf.append(nvpOut("ServerPort", String.valueOf(request.getServerPort())));
        buf.append(nvpOut("Scheme", request.getScheme()));
        buf.append(nvpOut("Protocol", request.getProtocol()));
        buf.append(nvpOut("PathInfo", request.getPathInfo()));
        buf.append(nvpOut("PathTranslated", request.getPathTranslated()));
        buf.append(nvpOut("ServletPath", request.getServletPath()));
        buf.append(nvpOut("ContextPath", request.getContextPath()));
        buf.append(nvpOut("RequestURI", request.getRequestURI()));
        buf.append(nvpOut("AuthType", request.getAuthType()));
        buf.append(nvpOut("ContentType", request.getContentType()));
        buf.append(nvpOut("ContentLength", String.valueOf(request.getContentLength())));
        buf.append(nvpOut("QueryString", request.getQueryString()));
        buf.append(nvpOut("RemoteUser", request.getRemoteUser()));
        buf.append(nvpOut("RequestedSessionId", request.getRequestedSessionId()));
        buf.append(nvpOut("Method", request.getMethod()));

        // --------
        buf.append(PAR).append("LOCAL").append(EOL);
        buf.append(nvpOut("LocalAddr", request.getLocalAddr()));
        buf.append(nvpOut("LocalName", request.getLocalName()));
        buf.append(nvpOut("LocalPort", String.valueOf(request.getLocalPort())));

        // ----------
        buf.append(PAR).append("REMOTE").append(EOL);
        buf.append(nvpOut("RemoteUser", request.getRemoteUser()));
        buf.append(nvpOut("RemoteAddr", request.getRemoteAddr()));
        buf.append(nvpOut("RemoteHost", request.getRemoteHost()));
        buf.append(nvpOut("RemotePort", String.valueOf(request.getRemotePort())));

        //---------
        buf.append(PAR).append("ATTRIBUTES").append(EOL);
        Enumeration<String> atts = request.getAttributeNames();
        while (atts.hasMoreElements()) {
            String name = atts.nextElement();
            buf.append(INDENT).append(name).append(NVP).append(request.getAttribute(name)).append(EOL);
            if ("javax.servlet.request.X509Certificate".equals(name)) {
                X509Certificate[] certObj = (X509Certificate[]) request.getAttribute(name);
//                certObj.getNotAfter(); // Date
//                certObj.getNotBefore(); //Date
//                certObj.getSerialNumber(); // BigInteger
//                certObj.getSigAlgName(); // String
//                certObj.getSigAlgOID(); //String
//                certObj.getSubjectDN(); //Principal
//                certObj.getVersion(); //int
//                certObj.getPublicKey(); //PublicKey
                for (int i = 0; i < certObj.length; ++i) {
                    buf.append(INDENT).append(INDENT).append("CERT-" + i).append(EOL);
                    Principal subject = certObj[i].getSubjectDN();
                    buf.append(INDENT).append(nvpOut("SubjectDN", subject == null ? null : subject.getName()));
                    Principal issuer = certObj[i].getIssuerDN();
                    buf.append(INDENT).append(nvpOut("IssuerDN", issuer == null ? "null" : issuer.getName()));
                }
            }
        }

        //--------
        buf.append(PAR).append("HEADERS").append(EOL);
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            buf.append(INDENT).append(header).append(NVP);
            Enumeration<String> hvals = request.getHeaders(header);
            while (hvals.hasMoreElements()) {
                String hval = hvals.nextElement();
                buf.append(hval).append(VSEP);
            }
            buf.append(EOL);
        }

        //--------
        buf.append(PAR).append("PARAMETERS").append(EOL);
        Enumeration<String> pnames = request.getParameterNames();
        Map<String, String[]> pmap = request.getParameterMap();
        while (pnames.hasMoreElements()) {
            String pname = pnames.nextElement();
            buf.append(INDENT).append(pname).append(NVP);
            String[] pvals = pmap.get(pname);
            for (int i = 0; i < pvals.length; ++i) {
                buf.append(pvals[i]).append(VSEP);
            }
            buf.append(EOL);
        }

        request.getCookies(); //Cookie[]
        //request.getParts(); //Collection<Part)

        request.getSession();//HttpSession
        request.getUserPrincipal(); //Principal

        return buf.toString();
    }

}
