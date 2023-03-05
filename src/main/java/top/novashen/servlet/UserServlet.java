package top.novashen.servlet;


import com.alibaba.fastjson2.JSONArray;
import com.mysql.cj.util.StringUtils;
import top.novashen.pojo.User;
import top.novashen.service.user.UserService;
import top.novashen.service.user.UserServiceImpl;
import top.novashen.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;


//与user有关的所有提交到这里
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取方法名，判断去哪个方法
        String method = request.getParameter("method");
        if (method==null)return;
        if (method.equals("savepwd")) {
            this.updatePassword(request, response);
        } else if (method.equals("pwdmodify")) {
            this.verifyPassword(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    //修改密码
    private void updatePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从session中获取信息
        Object attribute = request.getSession().getAttribute(Constants.USER_SESSION);

        String newPassword = request.getParameter("newpassword");

        if (attribute != null && !StringUtils.isNullOrEmpty(newPassword)){
            User user = (User) attribute;
            UserService userService = new UserServiceImpl();
            try {
                Boolean result = userService.updatePassword(user.getId(), newPassword);
                if (result) {
                    request.setAttribute("message","Update successful!Pls login!");
                    //移除session
                    request.getSession().removeAttribute(Constants.USER_SESSION);
                } else {
                    request.setAttribute(Constants.MESSAGE,"Update fail!");
                }
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            request.setAttribute(Constants.MESSAGE,"New password empty!");
        }
        request.getRequestDispatcher("pwdmodify.jsp").forward(request,response);
    }

    //判断旧密码是否正确
    private void verifyPassword(HttpServletRequest request, HttpServletResponse response){
        Object attribute = request.getSession().getAttribute(Constants.USER_SESSION);
        String pwd = request.getParameter("oldpassword");


        //用map的形式传参，以后会很有用，传递json
        HashMap<String, String> resultMap = new HashMap<>();

        //封装在map中
        if (attribute!=null && !StringUtils.isNullOrEmpty(pwd)){
            User user = (User) attribute;
            String userPassword = user.getUserPassword();
            if (userPassword.equals(pwd)) {
                resultMap.put("result","true");
            } else {
                resultMap.put("result","false");
            }
        } else if (attribute == null) {
            resultMap.put("result","sessionerror");
        } else if (StringUtils.isNullOrEmpty(pwd)) {
            resultMap.put("result","error");
        }
        //回传结果json
        response.setContentType("application/json");
        try {
            PrintWriter writer = response.getWriter();
            //转换格式，map->json
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
