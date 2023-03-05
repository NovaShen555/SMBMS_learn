package top.novashen.dao.user;

import top.novashen.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;

public interface UserDao {
    //得到要登录的用户
    User getLoginUser(Connection connection, String userCode) throws SQLException;

    //修改当前用户的密码
    int updatePassword(Connection connection, int id, String password) throws SQLException;
}
