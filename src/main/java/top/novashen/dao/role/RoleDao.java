package top.novashen.dao.role;

import top.novashen.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RoleDao {
    //获取角色列表给前端用
    List<Role> getRoleList(Connection connection) throws SQLException;
}
