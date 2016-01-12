package morbrian.j2eesandbox.requestdump;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Map;

/**
 * Useful for dumping ServletRequest parameters for debugging.
 *
 */
public class RequestInspectorUtil {
  private final String EOL; // end of line
  private final String INDENT; // line indent
  private final String PAR; // paragraph
  private final String NVP; // name = value for example
  private final String VSEP; // value separator

  public RequestInspectorUtil(String eol, String indent, String par, String nvp, String vsep) {
    this.EOL = eol;
    this.INDENT = indent;
    this.PAR = par;
    this.NVP = nvp;
    this.VSEP = vsep;
  }

  public String nvpOut(String name, String value) {
    StringBuilder buf = new StringBuilder();
    buf.append(INDENT).append(name).append(NVP).append(value).append(EOL);
    return buf.toString();
  }

  public String dumpRequestContent(HttpServletRequest request) {
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
          buf.append(INDENT)
              .append(nvpOut("SubjectDN", subject == null ? null : subject.getName()));
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
    Principal pal = request.getUserPrincipal(); //Principal
    buf.append(PAR).append("PRINCIPAL").append(EOL);
    buf.append(INDENT).append(nvpOut("Principal", pal == null ? "null" : pal.getName()));
    buf.append(INDENT)
        .append(nvpOut("Principal Class", pal == null ? "null" : pal.getClass().getName()));

    return buf.toString();
  }

}
