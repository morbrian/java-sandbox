package morbrian.j2eesandbox.requestdump.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class X509HeaderServletRequestWrapper extends HttpServletRequestWrapper {

  Map<String, List<String>> augmentedHeaders = new HashMap<String, List<String>>();

  /**
   * Constructs a request object wrapping the given request.
   *
   * @throws IllegalArgumentException if the request is null
   */
  public X509HeaderServletRequestWrapper(HttpServletRequest request) {
    super(request);
  }

  public void setSubjectDN(String subjectDn) {
    augmentedHeaders.put("subjectDN", Collections.singletonList(subjectDn));
  }

  public void addRole(String role) {
    List<String> list = augmentedHeaders.get("role");
    if (list != null) {
      List copyList = new ArrayList<>(list);
      copyList.add(role);
      augmentedHeaders.put("role", Collections.unmodifiableList(copyList));
    } else {
      augmentedHeaders.put("role", Collections.singletonList(role));
    }
  }

  @Override public String getHeader(String name) {
    return augmentedHeaders.containsKey(name) ?
        augmentedHeaders.get(name).get(0) :
        super.getHeader(name);
  }

  @Override public Enumeration<String> getHeaders(String name) {
    return augmentedHeaders.containsKey(name) ?
        Collections.enumeration(augmentedHeaders.get(name)) :
        super.getHeaders(name);
  }

  @Override public Enumeration<String> getHeaderNames() {
    ArrayList<String> list = Collections.list(super.getHeaderNames());
    list.addAll(augmentedHeaders.keySet());
    return Collections.enumeration(list);
  }

}
