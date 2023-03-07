package top.novashen.service.user;

import org.junit.Test;
import top.novashen.dao.BaseDao;
import top.novashen.dao.user.UserDao;
import top.novashen.dao.user.UserDaoImpl;
import top.novashen.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
//        User user = null;

        //通过业务层调用dao层，实现操作数据库
        System.out.println("userCode is "+userCode);

        connection = BaseDao.getConnection();
        User user = userDao.getLoginUser(connection,userCode);

        BaseDao.closeResources(connection,null,null);

//        System.out.println("passWord is "+user.getUserPassword());
        if (user != null && password.equals(user.getUserPassword())) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public Boolean updatePassword(int id, String password) throws SQLException, ClassNotFoundException {

        Connection connection = null;
        connection = BaseDao.getConnection();

        int result = userDao.updatePassword(connection, id, password);

        BaseDao.closeResources(connection,null,null);

        //01正常返回，若超过1则说明有问题，需要回滚（没必要，先不写，就这样）
        return result == 1;

    }

    @Override
    public Boolean addUser(User user) throws SQLException, ClassNotFoundException {

        Connection connection = null;
        boolean result = false;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);//开启JDBC事务。
            result = userDao.addUser(connection,user);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
                System.out.println("========rollback=========");
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        } finally {
            //在service层进行connection连接的关闭.
            BaseDao.closeResources(connection,null,null);
        }

        return result;
    }

    @Override
    public Boolean delUser(int userId) {

        Connection connection = null;
        boolean result = false;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);//开启JDBC事务。
            result = userDao.delUser(connection,userId);
            connection.commit();//提交
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
                System.out.println("========rollback=========");
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        } finally {
            //在service层进行connection连接的关闭.
            BaseDao.closeResources(connection,null,null);
        }

        return  result;
    }

    @Override
    public int getUserCount(String userName, int userRoleId) throws SQLException, ClassNotFoundException {

        Connection connection = null;
        connection = BaseDao.getConnection();

        int userCount = userDao.getUserCount(connection, userName, userRoleId);

        BaseDao.closeResources(connection,null,null);

        return userCount;
    }

    @Override
    public List<User> getUserList(String userName, int userRoleId, int currentPageNo, int pageSize) throws SQLException, ClassNotFoundException {

        Connection connection = null;
        connection = BaseDao.getConnection();
        List<User> userList = new ArrayList<>();

        userList = userDao.getUserList(connection, userName, userRoleId, currentPageNo, pageSize);

        BaseDao.closeResources(connection,null,null);

        return userList;
    }

    @Override
    public Boolean isUserExist(String  userCode) throws SQLException, ClassNotFoundException {

        Connection connection = null;
        connection = BaseDao.getConnection();

        return userDao.isUserExist(connection, userCode);

    }



}
