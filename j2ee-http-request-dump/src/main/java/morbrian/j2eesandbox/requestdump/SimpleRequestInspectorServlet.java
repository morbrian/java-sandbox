package morbrian.j2eesandbox.requestdump;

import java.io.IOException;

import javax.annotation.security.DeclareRoles;
import javax.servlet.http.HttpServletRequest;

@DeclareRoles("reader")
public class SimpleRequestInspectorServlet extends javax.servlet.http.HttpServlet {
  protected void doPost(HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
      throws javax.servlet.ServletException, IOException {
    inspectRequest(request, response);
  }

  protected void doGet(HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
      throws javax.servlet.ServletException, IOException {
    inspectRequest(request, response);
  }

  private void inspectRequest(HttpServletRequest request,
      javax.servlet.http.HttpServletResponse response)
      throws javax.servlet.ServletException, IOException {
    RequestInspectorUtil riu = new RequestInspectorUtil("\n", "\t", "\n========\n", " = ", ", ");
    String content = riu.dumpRequestContent(request);
    System.out.println(content);
    response.getOutputStream().print("<html><pre>" + content + "</pre></html>");
  }
}
