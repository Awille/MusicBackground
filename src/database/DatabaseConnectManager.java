package database;

import commonconstant.CommonConstant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectManager {
    private static DatabaseConnectManager INSTANCE;

    /**
     * 数据库连接
     */
    private static Connection databaseConnection;

    private static String connectUrl = "jdbc:mysql://" +
            CommonConstant.Database.host + ":" + CommonConstant.Database.port
            + CommonConstant.Database.databaseName
            + "?autoReconnect=true&useUnicode=true"
            + "&characterEncoding=UTF-8&useSSL=false";

    public static DatabaseConnectManager getInstance() {
        if (INSTANCE == null) {
            synchronized (DatabaseConnectManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DatabaseConnectManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 建立数据库连接
     * @return  连接结果
     */
    public boolean establishConnection() {
        try {
            databaseConnection = DriverManager.getConnection(connectUrl, CommonConstant.Database.userName, CommonConstant.Database.password);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭数据库连接
     * @return 关闭数据库连接结果
     */
    public boolean closeConnection() {
        try {
            databaseConnection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



}
