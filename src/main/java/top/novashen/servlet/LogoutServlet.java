package top.novashen.servlet;

import top.novashen.util.Constants;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //移除session
        request.getSession().removeAttribute(Constants.USER_SESSION);
        //重定向至登录页面
        System.out.println(request.getContextPath() + "/login.jsp");
        response.sendRedirect(request.getContextPath()+"/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
