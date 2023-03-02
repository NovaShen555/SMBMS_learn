package top.novashen.service.user;

import top.novashen.pojo.User;

import java.sql.SQLException;

public interface UserService {
    //登录业务
    public User login(String userCode, String password) throws SQLException, ClassNotFoundException;
}
