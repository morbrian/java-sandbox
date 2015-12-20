package morbrian.j2eesandbox.requestdump.filter;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.security.cert.X509Certificate;

/**
 * Created by bmoriarty on 11/20/15.
 */
public class X509Extraction {

    public static String extractCnFromRequest(HttpServletRequest request) {
        return extractCnFromLdif(extractPrimaryLdifFromRequest(request));
    }

    public static String extractCnFromLdif(String ldif) {
        String result = "";
        String[] parts = ldif.split(",");
        for (String nvp: parts) {
            String[] pair = nvp.split("=");
            if("CN".equalsIgnoreCase(pair[0].trim())) {
                result = pair[1].trim();
            }
        }
        return result;
    }

    public static String extractPrimaryLdifFromRequest(HttpServletRequest request) {
        return extractPrimaryLdif(extractCertChainFromRequest(request));
    }

    public static X509Certificate[] extractCertChainFromRequest(HttpServletRequest request) {
        if(request == null) {
            return null;
        } else {
            return (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
        }
    }

    public static String extractPrimaryLdif(X509Certificate[] certObj) {
        String def = "";
        if(certObj == null || certObj.length < 1) {
            return def;
        } else {
            Principal subject = certObj[0].getSubjectDN();
            if(subject != null) return subject.getName();
            else return def;
        }
    }

    public static X509Identity extractX509IdentityFromRequest(HttpServletRequest request) {
        return new X509Identity(extractPrimaryLdifFromRequest(request));
    }
}
