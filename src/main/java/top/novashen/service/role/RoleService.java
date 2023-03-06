package top.novashen.service.role;

import top.novashen.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RoleService {
    //获取角色列表
    List<Role> getRoleList() throws SQLException, ClassNotFoundException;
}
