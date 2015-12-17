package morbrian.j2eesandbox.requestdump.filter;

import javax.security.auth.Subject;
import java.security.Principal;

/**
 * Created by bmoriarty on 12/16/15.
 */
public class SimplePrincipal implements Principal {

    private String name;

    public SimplePrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

}
