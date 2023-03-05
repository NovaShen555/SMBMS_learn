package top.novashen.filter;

import top.novashen.pojo.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static top.novashen.util.Constants.USER_SESSION;

public class PermissionFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        //从session中获取用户，若null则禁止访问
        User user = (User) servletRequest.getSession().getAttribute(USER_SESSION);

        if (user == null){
            servletResponse.sendRedirect(servletRequest.getContextPath()+"/error.jsp");
            System.out.println("Permission denied");
            return;
        }
//        System.out.println("doFiltering...");
        chain.doFilter(request, response);
    }
}
