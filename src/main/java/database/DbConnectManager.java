package database;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import utils.TextUtils;

import javax.imageio.IIOException;
import java.io.*;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnectManager {
    private static DbConnectManager INSTANCE = null;
    private static DruidDataSource druidDataSource = null;

    static {
        Properties properties = loadPropertiesFile("db_server.properties");
        try {
            druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DbConnectManager getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (DbConnectManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DbConnectManager();
                }
            }
        }
        return INSTANCE;
    }


    public DruidPooledConnection getConnection() throws SQLException {
        return druidDataSource.getConnection();
    }



    /**
     * 加载配置文件
     * @param path 文件路径（包括了文件名）
     * @return 配置文件实例
     */
    private static Properties loadPropertiesFile(String path) {
        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("Properties File path is incorrect:" + path);
        }
        String rootPath = DbConnectManager.class.getClassLoader().getResource("").getPath();
        InputStream inputStream = null;
        Properties properties = null;
        try {
            inputStream = new FileInputStream(rootPath + File.separator + path);
            properties = new Properties();
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }


}
