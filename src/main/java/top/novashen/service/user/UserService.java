package top.novashen.service.user;

import top.novashen.pojo.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    //登录业务
    User login(String userCode, String password) throws SQLException, ClassNotFoundException;

    //修改密码
    Boolean updatePassword(int id, String password) throws SQLException, ClassNotFoundException;

    //添加用户
    Boolean addUser(User user) throws SQLException, ClassNotFoundException;

    //删除用户
    Boolean delUser(int userId);

    //获取用户数量
    int getUserCount(String userName, int userRoleId) throws SQLException, ClassNotFoundException;

    //根据参数获取用户列表
    List<User> getUserList(String userName, int userRoleId, int currentPageNo, int pageSize) throws SQLException, ClassNotFoundException;

    //判断userCode是否存在
    Boolean isUserExist(String userCode) throws SQLException, ClassNotFoundException;

    //通过用户id查询用户信息，显示前端
    User userQueryById(int userId) throws SQLException, ClassNotFoundException;

    //修改用户信息
    Boolean userModify(User user) throws SQLException, ClassNotFoundException;
}
