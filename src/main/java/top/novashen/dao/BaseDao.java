package top.novashen.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

//操作数据库的公共类
public class BaseDao {

    private static String driver;
    private static String url;
    private static String user;
    private static String password;


    static {
        //通过类加载器读取资源
        InputStream inputStream = BaseDao.class.getClassLoader().getResourceAsStream("database.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        user = properties.getProperty("user");
        password = properties.getProperty("password");

    }

    //获取数据库连接，可以仍到util里面
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(url,user,password);
    }


    //查询公共方法
    public static ResultSet execute(Connection connection, String sql, Object[] params, PreparedStatement statement, ResultSet resultSet) throws SQLException {
        statement = connection.prepareStatement(sql);
        int counter = 0;
        for (Object i : params){
            counter++;
            statement.setObject(counter,i);
        }
        resultSet = statement.executeQuery();
        return  resultSet;
    }

    //更新公共方法
    public static int execute(Connection connection, String sql, Object[] params, PreparedStatement statement) throws SQLException {
        statement = connection.prepareStatement(sql);
        int counter = 0;
        for (Object i : params){
            counter++;
            statement.setObject(counter,i);
        }
        int result = statement.executeUpdate();
        return result;
    }

    //关闭资源
    public static boolean closeResources(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        boolean flag = true;
        if (resultSet!=null){
            try {
                resultSet.close();
                //让垃圾回收器回收
                resultSet = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (statement!=null){
            try {
                statement.close();
                statement = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (connection!=null){
            try {
                connection.close();
                //让垃圾回收器回收
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }

}
