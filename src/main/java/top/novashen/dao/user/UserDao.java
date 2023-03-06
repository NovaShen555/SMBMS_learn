package top.novashen.dao.user;

import top.novashen.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    //得到要登录的用户
    User getLoginUser(Connection connection, String userCode) throws SQLException;

    //修改当前用户的密码
    int updatePassword(Connection connection, int id, String password) throws SQLException;

    //根据用户名或角色获取用户总数
    int getUserCount(Connection connection, String userName, int userRoleId) throws SQLException;

    //通过各种参数获取用户列表
    List<User> getUserList(Connection connection, String  userName, int userRoleId, int currentPageNo, int pageSize) throws SQLException;


}
