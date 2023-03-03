package top.novashen.servlet;

import top.novashen.pojo.User;
import top.novashen.service.user.UserServiceImpl;
import top.novashen.util.Constants;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;


public class LoginServlet extends HttpServlet {
    //servlet调用业务层
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Start Login");
        //获取用户密码
        String userCode = request.getParameter("userCode");
        String userPassword = request.getParameter("userPassword");
        //查询数据库中的密码，与之比较
        UserServiceImpl userService = new UserServiceImpl();
        System.out.println("Get User Information");
        try {
            //获取到用户名是userCode的用户的信息
            User user = userService.login(userCode, userPassword);
            if (user != null) { //用户名存在
                //保存用户所有信息进入session
                System.out.println("Store User in Session");
                request.getSession().setAttribute(Constants.USER_SESSION,user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
