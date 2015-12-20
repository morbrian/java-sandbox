package morbrian.j2eesandbox.requestdump.filter;

public class X509Identity {

    private static final String ANONYMOUS = "anonymous";
    private static ThreadLocal<String> LDIF = new ThreadLocal<>();

    public X509Identity(String ldif) {
        LDIF.set(ldif);
    }

    public String getCommonName() {
        //TODO: NCSAwesom token is a development hack only.
        //return "NCSAwesome.Token.123456789";
        return X509Extraction.extractCnFromLdif(LDIF.get());
    }

    public boolean hasCert() {
        String commonName = getCommonName();
        return commonName != null && !"".equals(commonName) && !ANONYMOUS.equals(commonName);
    }
}
