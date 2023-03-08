package top.novashen.dao.user;

import com.mysql.cj.util.StringUtils;
import org.junit.Test;
import top.novashen.dao.BaseDao;
import top.novashen.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao{
    @Override
    public User getLoginUser(Connection connection, String userCode) throws SQLException {

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        String sql = "select * from `smbms_user` u,`smbms_role` r where u.userRole=r.id and `userCode` = ?";
        Object[] params = {userCode};

        if (connection!=null) {
            resultSet = BaseDao.execute(connection, sql, params, statement, resultSet);
            while (resultSet.next()) {
                user = new User();
                getUserFromResultSet(resultSet, user);
            }
            //连接属于业务层，从上级传下来的东西不要动，让上级关
            BaseDao.closeResources(null,statement,resultSet);
        }

        return user;
    }

    @Override
    public int updatePassword(Connection connection, int id, String password) throws SQLException {

        PreparedStatement statement = null;
        int result = 0;

        String sql = "update smbms_user set `userPassword` = ? where `id` = ?";
        Object[] params = {password, id};

        if (connection!=null) {
            result = BaseDao.execute(connection, sql, params, statement);
        }
        //连接属于业务层，从上级传下来的东西不要动，让上级关
        BaseDao.closeResources(null,statement,null);

        //返回受影响的行数
        return result;
    }

    @Override
    public Boolean addUser(Connection connection, User user) throws SQLException {

        PreparedStatement statement = null;
        int result = 0;

        if (connection!=null){
            String sql = "insert into smbms_user (userCode,userName,userPassword," +
                    "userRole,gender,birthday,phone,address,creationDate,createdBy) " +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {user.getUserCode(), user.getUserName(), user.getUserPassword(),
                    user.getUserRole(), user.getGender(), user.getBirthday(),
                    user.getPhone(), user.getAddress(), user.getCreationDate(), user.getCreatedBy()};
            result = BaseDao.execute(connection, sql, params, statement);
            BaseDao.closeResources(null,statement,null);
        }

        return result != 0;
    }

    @Override
    public Boolean delUser(Connection connection, int userId) throws SQLException {

        PreparedStatement statement = null;
        int result = 0;

        if (connection!=null){
            String sql = "DELETE FROM `smbms_user` WHERE id = ?";
            Object[] params = {userId};
            result = BaseDao.execute(connection, sql, params, statement);
            BaseDao.closeResources(null,statement,null);
        }

        return result != 0;
    }


    @Override
    public int getUserCount(Connection connection, String userName, int userRoleId) throws SQLException {

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int count = 0;

        //使用StringBuffer实现动态sql
        StringBuffer sql = new StringBuffer("select count(1) as count from smbms_user u,smbms_role r where u.userRole = r.id ");
        //因为sql参数不确定有多少个，所以使用arraylist存放
        ArrayList<Object> list = new ArrayList<>();

        if (connection!=null) {

            if (!StringUtils.isNullOrEmpty(userName)) {
                sql.append("and u.userName like ? ");
                //实现模糊查询
                list.add("%"+userName+"%");
            }
            if (userRoleId > 0) {
                sql.append("and r.id = ?");
                list.add(userRoleId);
            }

            Object[] params = list.toArray();

            System.out.println("查询用户数量："+ sql);

            resultSet = BaseDao.execute(connection, String.valueOf(sql), params, statement, resultSet);

            //若有，获取
            while (resultSet.next()) {
                count = resultSet.getInt("count");
            }

        }

        BaseDao.closeResources(null,statement, resultSet);

        return count;
    }


    @Override
    public List<User> getUserList(Connection connection, String userName, int userRoleId, int currentPageNo, int pageSize) throws SQLException {

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<User> userList = new ArrayList<>();

        //使用StringBuffer实现动态sql
        StringBuffer sql = new StringBuffer("select * from smbms_user u,smbms_role r where u.userRole = r.id ");
        //因为sql参数不确定有多少个，所以使用arraylist存放
        ArrayList<Object> list = new ArrayList<>();

        if (connection!=null) {

            if (!StringUtils.isNullOrEmpty(userName)) {
                sql.append("and u.userName like ? ");
                //实现模糊查询
                list.add("%"+userName+"%");
            }
            if (userRoleId > 0) {
                sql.append("and r.id = ? ");
                list.add(userRoleId);
            }

            sql.append("limit ?,?");
            int start = (currentPageNo-1)*pageSize;
            list.add(start);
            list.add(pageSize);

            Object[] params = list.toArray();

            System.out.println("查询用户数量："+ sql);

            resultSet = BaseDao.execute(connection, String.valueOf(sql), params, statement, resultSet);

            //若有，获取
            while (resultSet.next()) {
                User user = new User();
                getUserFromResultSet(resultSet, user);
                userList.add(user);
            }

        }

        BaseDao.closeResources(null,statement, resultSet);

        return userList;

    }

    @Override
    public Boolean isUserExist(Connection connection, String userCode) throws SQLException {

        PreparedStatement statement = null;
        String sql = "select count(1) r from smbms_user where userCode = ?";
        Object[] params = {userCode};
        ResultSet resultSet = null;
        int count = 0;

        resultSet = BaseDao.execute(connection, sql, params, statement, resultSet);

        //若有，获取
        while (resultSet.next()) {
            count = resultSet.getInt("r");
        }

        return !(count ==0);

    }

    @Override
    public User userQueryById(Connection connection, int userId) throws SQLException {

        PreparedStatement statement = null;
        String sql = "select * from smbms_user s,smbms_role r where s.userRole = r.id and s.id = ?";
        Object[] params = {userId};
        ResultSet resultSet = null;
        User user = new User();

        resultSet = BaseDao.execute(connection,sql,params,statement,resultSet);

        while (resultSet.next()) {
            getUserFromResultSet(resultSet,user);
        }

        BaseDao.closeResources(null,statement,resultSet);

        return user;

    }

    @Override
    public Boolean modifyUser(Connection connection, User user) throws SQLException {

        PreparedStatement statement = null;
        String sql = "update smbms_user set userName=?," +
                "gender=?,birthday=?,phone=?,address=?,userRole=?,modifyBy=?,modifyDate=? where id = ? ";
        Object[] params = {user.getUserName(), user.getGender(), user.getBirthday(),
                user.getPhone(), user.getAddress(), user.getUserRole(), user.getModifyBy(),
                user.getModifyDate(), user.getId()};
        int result = 0;

        result = BaseDao.execute(connection, sql, params, statement);

        BaseDao.closeResources(null,statement,null);

        return result==1;
    }


    protected void getUserFromResultSet(ResultSet resultSet, User user) throws SQLException {
        user.setId(resultSet.getInt("id"));
        user.setUserCode(resultSet.getString("userCode"));
        user.setUserName(resultSet.getString("userName"));
        user.setUserPassword(resultSet.getString("userPassword"));
        user.setGender(resultSet.getInt("gender"));
        user.setBirthday(resultSet.getDate("birthday"));
        user.setPhone(resultSet.getString("phone"));
        user.setAddress(resultSet.getString("address"));
        user.setUserRole(resultSet.getInt("userRole"));
        user.setCreatedBy(resultSet.getInt("createdBy"));
        user.setCreationDate(resultSet.getTimestamp("creationDate"));
        user.setModifyBy(resultSet.getInt("modifyBy"));
        user.setModifyDate(resultSet.getTimestamp("modifyDate"));
        user.setUserRoleName(resultSet.getString("roleName"));
    }

}
