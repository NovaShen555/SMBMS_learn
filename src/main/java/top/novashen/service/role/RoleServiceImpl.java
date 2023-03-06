package top.novashen.service.role;

import org.junit.Test;
import top.novashen.dao.BaseDao;
import top.novashen.dao.role.RoleDao;
import top.novashen.dao.role.RoleDaoImpl;
import top.novashen.pojo.Role;
import top.novashen.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleServiceImpl implements RoleService{

    private RoleDao roleDao = null;

    public RoleServiceImpl() {
        roleDao = new RoleDaoImpl();
    }

    @Override
    public List<Role> getRoleList() throws SQLException, ClassNotFoundException {

        Connection connection = null;
        connection = BaseDao.getConnection();
        List<Role> roleList = new ArrayList<>();

        roleList = roleDao.getRoleList(connection);

        BaseDao.closeResources(connection,null,null);

        return roleList;
    }

}
