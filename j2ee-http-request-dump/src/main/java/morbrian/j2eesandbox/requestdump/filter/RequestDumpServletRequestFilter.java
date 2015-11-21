package morbrian.j2eesandbox.requestdump.filter;

import morbrian.j2eesandbox.requestdump.RequestInspectorUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by morbrian on 8/26/14.
 */
public class RequestDumpServletRequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // nothing to do
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestInspectorUtil riu = new RequestInspectorUtil("\n", "\t", "\n========\n", " = ", ", ");
        String content = riu.dumpRequestContent((HttpServletRequest) request);
        System.out.println(content);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // nothing to do
    }

}
