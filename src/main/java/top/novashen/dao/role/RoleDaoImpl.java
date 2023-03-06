package top.novashen.dao.role;

import top.novashen.dao.BaseDao;
import top.novashen.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao{

    @Override
    public List<Role> getRoleList(Connection connection) throws SQLException {

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Role> roleList = new ArrayList<>();
        Object[] params = {};

        String sql = "select * from smbms_role";

        if (connection!=null) {

            resultSet = BaseDao.execute(connection, String.valueOf(sql), params, statement, resultSet);

            //若有，获取
            while (resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getInt("id"));
                role.setRoleCode(resultSet.getString("roleCode"));
                role.setRoleName(resultSet.getString("roleName"));
                roleList.add(role);
            }

        }

        BaseDao.closeResources(null,statement, resultSet);

        return roleList;
    }
}
