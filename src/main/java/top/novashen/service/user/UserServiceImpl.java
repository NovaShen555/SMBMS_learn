package top.novashen.service.user;

import org.junit.Test;
import top.novashen.dao.BaseDao;
import top.novashen.dao.user.UserDao;
import top.novashen.dao.user.UserDaoImpl;
import top.novashen.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;

public class UserServiceImpl implements UserService{

    //业务层都会调用dao层，所以我们要引入
    private UserDao userDao;

    public UserServiceImpl() {
        //new该对象时自动new user
        userDao = new UserDaoImpl();
    }

    @Override
    public User login(String userCode, String password) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        User user = null;

        connection = BaseDao.getConnection();
        //通过业务层调用dao层，实现操作数据库
        user = userDao.getLoginUser(connection,userCode);

        BaseDao.closeResources(connection,null,null);
        return user;
    }

}