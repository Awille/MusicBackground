package database.crud;

import bean.UploadFile;
import bean.User;
import com.alibaba.druid.pool.DruidPooledConnection;
import database.DbConnectManager;
import sun.misc.BASE64Decoder;
import utils.EncryptUtils;
import utils.TextUtils;

import javax.servlet.ServletContext;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserCrud {
    /**
     * 增加用户
     * @param user
     * @param connection
     * @return 增加的用户实例
     */
    public static User addUser(User user, DruidPooledConnection connection) {
        if (user != null && !TextUtils.isEmpty(user.getAccount()) && !TextUtils.isEmpty(user.getPassword()) && !TextUtils.isEmpty(user.getNickName())) {
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO music.user (account, nick_name, password, gender, birth, signature, avatar_url) VALUES (?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, user.getAccount());
                preparedStatement.setString(2, user.getNickName());
                preparedStatement.setString(3, EncryptUtils.encryptByMd5(user.getPassword()));
                preparedStatement.setInt(4, user.getGender());
                preparedStatement.setString(5, user.getBirth());
                preparedStatement.setString(6, user.getSignature());
                preparedStatement.setString(7, user.getAvatarUrl());
                if (preparedStatement.executeUpdate() > 0) {
                    ResultSet resultSet = preparedStatement.getGeneratedKeys();
                    if (resultSet.next()) {
                        user.setUserId(resultSet.getLong(1));
                    }
                    return user;
                } else {
                    return null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 更新用户信息，不包括密码以及头像
     * @param user
     * @param connection
     * @return 更新结果
     */
    public static User updateUser(User user, DruidPooledConnection connection) {
        if (user == null) {
            return null;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "UPDATE music.user set "
                            + "nick_name = ?, gender = ?, birth = ?, signature = ?"
                            + "WHERE account = ?");
            preparedStatement.setString(1, user.getNickName());
            preparedStatement.setInt(2, user.getGender());
            preparedStatement.setString(3, user.getBirth());
            preparedStatement.setString(4, user.getSignature() == null ? "" : user.getSignature());
            preparedStatement.setString(5, user.getAccount());
            if (preparedStatement.executeUpdate() > 0) {
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("异常" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 更改用户密码
     * @param user
     * @param connection
     * @return 修改密码成功与否
     */
    public static boolean changePassword(User user, DruidPooledConnection connection) {
        if (user == null) {
            return false;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "UPDATE music.user set password = ? WHERE account = ?");
            preparedStatement.setString(1, EncryptUtils.encryptByMd5(user.getPassword()));
            preparedStatement.setString(2, user.getAccount());
            if (preparedStatement.executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 更改用户头像
     * @param account
     * @param connection
     * @param avatarUrl
     * @return 用户头像更改结果
     */
    public static boolean changeUserAvatarUrl(String account, String avatarUrl, DruidPooledConnection connection) {
        if (TextUtils.isEmpty(account)) {
            return false;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "UPDATE music.user set avatar_url = ? WHERE account = ?");
            preparedStatement.setString(1, avatarUrl);
            preparedStatement.setString(2, account);
            if (preparedStatement.executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 用户查询
     * @param account
     * @param connection
     * @return 用户查询结果
     */
    public static User queryUserByAccount(String account, DruidPooledConnection connection) {
        User user = null;
        if (TextUtils.isEmpty(account)) {
            return null;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM music.user WHERE  account = ?");
            preparedStatement.setString(1, account);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setNickName(resultSet.getString("nick_name"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirth(resultSet.getString("birth"));
                user.setSignature(resultSet.getString("signature"));
                user.setAvatarUrl(resultSet.getString("avatar_url"));
                user.setAccount(resultSet.getString("account"));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    /**
     * 删除用户
     * @param account
     * @param connection
     * @return 用户删除结果
     */
    //todo 待更新 删除用户并且与之有关的所有操作， 注销用户操作一般不要做
    public static boolean deleteUser(String account, DruidPooledConnection connection) {
        if (TextUtils.isEmpty(account)) {
            return false;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "DELETE FROM music.user where account = ?");
            preparedStatement.setString(1, account);
            if (preparedStatement.executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 上传图片返回文件名
     * @param img
     * @return 文件路径
     */
    private static String saveUserAvatar(UploadFile img, ServletContext servletContext) {
        boolean flag = false;
        byte[] bytes = null;
        try {
            bytes = new BASE64Decoder().decodeBuffer(img.getFileStr());
            flag = true;
        } catch (IOException e) {
            flag = false;
            e.printStackTrace();
        }
        if (!flag) {
            return null;
        }
        flag = false;
        String fileFormat = img.getFileName().substring(img.getFileName().indexOf("."));
        File path = new File(servletContext.getRealPath("/") + "\\upload\\avatar");
        if (!path.exists()) {
            flag = path.mkdirs();
        }
        String fileName = servletContext.getRealPath("/") + "\\upload\\avatar\\" + img.getAccount() + "_avatar" + fileFormat;
        File imgFile = new File(fileName);
        flag = true;
        if (!imgFile.exists()) {
            try {
                imgFile.createNewFile();
            } catch (IOException e) {
                flag = false;
                e.printStackTrace();
            }
        }
        if (!flag) {
            return null;
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(bytes);
            return  fileName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 上传图片函数
     * @param file
     * @param connection
     * @return 图片上传结果
     */
    public static boolean uploadUserAvatar(UploadFile file, DruidPooledConnection connection, ServletContext servletContext) {
        String avatarUrl = saveUserAvatar(file, servletContext);
        if (avatarUrl == null) {
            return false;
        }
        if (changeUserAvatarUrl(file.getAccount(), avatarUrl, connection)) {
            return true;
        }
        return false;
    }


}
