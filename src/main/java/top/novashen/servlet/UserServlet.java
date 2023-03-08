package top.novashen.servlet;


import com.alibaba.fastjson2.JSONArray;
import com.mysql.cj.util.StringUtils;
import top.novashen.pojo.Role;
import top.novashen.pojo.User;
import top.novashen.service.role.RoleService;
import top.novashen.service.role.RoleServiceImpl;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//与user有关的所有提交到这里
public class UserServlet extends HttpServlet {

    private UserService userService;

    public UserServlet() {
        userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取方法名，判断去哪个方法
        String method = request.getParameter("method");
        if (method==null)return;
        switch (method) {
            case "savepwd" -> this.updatePassword(request, response);
            case "pwdmodify" -> this.verifyPassword(request, response);
            case "query" -> this.userQuery(request, response);
            case "add" -> this.userAdd(request,response);
            case "getrolelist" -> this.getRoleList(request,response);
            case "deluser" -> this.userDel(request,response);
            case "ucexist" -> this.isUserExist(request,response);
            case "view" -> userQueryById(request,response);
            case "modify" -> this.userModifyQueryById(request,response);
            case "modifyexe" -> this.userModifyById(request,response);
        }
    }

    private void userModifyQueryById(HttpServletRequest request, HttpServletResponse response) {

        String userId = request.getParameter("uid");
        if (!StringUtils.isNullOrEmpty(userId)) {
            UserService userService = new UserServiceImpl();
            try {
                User user = userService.userQueryById(Integer.parseInt(userId));

                request.setAttribute("user", user);
                request.getRequestDispatcher("usermodify.jsp").forward(request, response);

            } catch (SQLException | ClassNotFoundException | ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void userModifyById(HttpServletRequest request, HttpServletResponse response) {

        String userId = request.getParameter("uid");
        Object attribute = request.getSession().getAttribute(Constants.USER_SESSION);
        if (attribute != null) {
            User user = (User) attribute;
            String userName = request.getParameter("userName");
            String gender = request.getParameter("gender");
            Date birthday = null;
            try {
                birthday = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("birthday"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String userRole = request.getParameter("userRole");

            user.setUserName(userName);
            user.setGender(Integer.valueOf(gender));
            user.setBirthday(birthday);
            user.setPhone(phone);
            user.setAddress(address);
            user.setUserRole(Integer.valueOf(userRole));

            try {
                if (userService.userModify(user)) {
                    response.sendRedirect(request.getContextPath() + "/jsp/user.do?method=query");
                } else {
                    request.getRequestDispatcher("usermodify.jsp").forward(request, response);
                }
            } catch (SQLException | ClassNotFoundException | ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void userQueryById(HttpServletRequest request, HttpServletResponse response) {

        String userId = request.getParameter("uid");
        if (!StringUtils.isNullOrEmpty(userId)) {
//            UserService userService = new UserServiceImpl();
            try {
                User user = userService.userQueryById(Integer.parseInt(userId));

                request.setAttribute("user", user);
                request.getRequestDispatcher("userview.jsp").forward(request, response);

            } catch (SQLException | ClassNotFoundException | ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void isUserExist(HttpServletRequest request, HttpServletResponse response) {

        String userCode = request.getParameter("userCode");

//        System.out.println("is userCode="+userCode+" exist?");

        //用map的形式传参，以后会很有用，传递json
        HashMap<String, String> resultMap = new HashMap<>();

        if (!StringUtils.isNullOrEmpty(userCode)){
//            UserService userService = new UserServiceImpl();
            try {
                if (userService.isUserExist(userCode)) {
                    resultMap.put("userCode","exist");
                } else {
                    resultMap.put("userCode","non-exist");
                }
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
//            System.out.println("userCode empty");
            resultMap.put("userCode","exist");
        }

//        System.out.println(resultMap);

        returnResultAsJSON(response,resultMap);

    }

    private void userDel(HttpServletRequest request, HttpServletResponse response) {

        String temp = request.getParameter("uid");
        HashMap<String, String> resultMap = new HashMap<>();

        if (!StringUtils.isNullOrEmpty(temp)) {
//            UserService userService = new UserServiceImpl();
            if (userService.delUser(Integer.parseInt(temp))) {
                resultMap.put("delResult","true");
            } else {
                resultMap.put("delResult","false");
            }
        } else {
            resultMap.put("delResult","notexist");
        }

        returnResultAsJSON(response,resultMap);

    }

    private void getRoleList(HttpServletRequest request, HttpServletResponse response) {
        RoleService roleService = new RoleServiceImpl();
        List<Role> roleList = null;
        try {
            roleList = roleService.getRoleList();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //把roleList转换成json对象输出
        returnResultAsJSON(response, roleList);

    }

    private void userAdd(HttpServletRequest request, HttpServletResponse response) {

        String userCode = request.getParameter("userCode");
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        String gender = request.getParameter("gender");
        Date birthday = null;
        try {
            birthday = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("birthday"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setGender(Integer.valueOf(gender));
        user.setBirthday(birthday);
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));

        System.out.println("Add user "+user);

//        UserService userService = new UserServiceImpl();

        try {
            if (userService.addUser(user)) {
                response.sendRedirect(request.getContextPath() + "/jsp/user.do?method=query");
            } else {
                request.getRequestDispatcher("useradd.jsp").forward(request, response);
            }
        } catch (SQLException | ClassNotFoundException | IOException | ServletException e) {
            throw new RuntimeException(e);
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
        returnResultAsJSON(response, resultMap);

    }

    private void returnResultAsJSON(HttpServletResponse response, Object resultMap) {
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

    //查询用户？
    private void userQuery(HttpServletRequest request, HttpServletResponse response){

        //要查的名字
        String userName = request.getParameter("queryname");
        //要查的角色
        String userRole = request.getParameter("queryUserRole");
        //当前页码
        String pageIndex = request.getParameter("pageIndex");

        int userRoleId = 0;
        int pageSize = Constants.PAGE_SIZE;
        int currentPageNo = 1;

//        UserService userService = new UserServiceImpl();

        //判断前端值是否合法并赋值
        if (userName == null) userName = "";
        if (!StringUtils.isNullOrEmpty(userRole)) userRoleId = Integer.parseInt(userRole);
        if (!StringUtils.isNullOrEmpty(pageIndex)) currentPageNo = Integer.parseInt(pageIndex);

        //获取用户总数
        try {
            int userCount = userService.getUserCount(userName, userRoleId);
//            //为了支持其上一页和下一页的操作
//            PageSupport pageSupport = new PageSupport();
//            pageSupport.setCurrentPageNo(currentPageNo);
//            pageSupport.setPageSize(pageSize);
//            pageSupport.setTotalPageCount(userCount);

            if (userCount > 0) {//如果有数据
                int totalPageCount = userCount % pageSize == 0 ? userCount / pageSize : userCount / pageSize + 1;
                //首页不往前，末页不往后
                if (currentPageNo < 1) {
                    currentPageNo = 1;
                } else if (currentPageNo > totalPageCount) {
                    currentPageNo = totalPageCount;
                }

                System.out.println("Current Page No " + currentPageNo + " Total Page Count " + totalPageCount);

                //获取用户列表
                List<User> userList = userService.getUserList(userName, userRoleId, currentPageNo, pageSize);
                //将列表设置进回应
                request.setAttribute("userList", userList);

                //获取角色列表 并返回
                RoleService roleService = new RoleServiceImpl();
                List<Role> roleList = roleService.getRoleList();
                request.setAttribute("roleList", roleList);

                //放回当前页数等信息
                request.setAttribute("totalCount", userCount);
                request.setAttribute("currentPageNo", currentPageNo);
                request.setAttribute("totalPageCount", totalPageCount);
            } else {
                //获取角色列表 并返回
                RoleService roleService = new RoleServiceImpl();
                List<Role> roleList = roleService.getRoleList();
                request.setAttribute("roleList", roleList);

                request.setAttribute("userList", null);

                request.setAttribute("totalCount", 1);
                request.setAttribute("currentPageNo", 1);
                request.setAttribute("totalPageCount", 1);
            }

            //将原有数据发回去
            request.setAttribute("queryUserName", userName);
            request.setAttribute("queryUserRole", userRole);
//            request.setAttribute("pageIndex", pageIndex);

            //将数据转发回页面
            request.getRequestDispatcher("userlist.jsp").forward(request,response);


        } catch (SQLException | ClassNotFoundException | IOException | ServletException e) {
            throw new RuntimeException(e);
        }

    }

}
