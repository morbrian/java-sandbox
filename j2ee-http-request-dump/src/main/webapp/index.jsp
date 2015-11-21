<%@ page import="morbrian.j2eesandbox.requestdump.RequestInspectorUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title></title>
  </head>
  <body>
  <%
      new RequestInspectorUtil("<br />", "&nbsp;&nbsp;", "<hr />", " = ", ", ").dumpRequestContent(request);
  %>
  </body>
</html>
