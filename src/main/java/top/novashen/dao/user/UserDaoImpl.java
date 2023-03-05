package top.novashen.dao.user;

import top.novashen.dao.BaseDao;
import top.novashen.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao{
    @Override
    public User getLoginUser(Connection connection, String userCode) throws SQLException {

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        String sql = "select * from `smbms_user` where `userCode` = ?";
        Object[] params = {userCode};

        if (connection!=null) {
            resultSet = BaseDao.execute(connection, sql, params, statement, resultSet);
            if (resultSet.next()) {
                user = new User();
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
}
