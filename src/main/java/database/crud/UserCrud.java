package database.crud;

import bean.User;
import commonconstant.CommonConstant;
import database.DatabaseConnectManager;
import sun.dc.pr.PRError;
import utils.EncryptUtils;
import utils.HtmlFilterUtils;
import utils.TextUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserCrud {
    /**
     * 增加用户
     * @param user
     * @return 增加的用户实例
     */
    public static User addUser(User user) {
        if (user != null && !TextUtils.isEmpty(user.getAccount()) && !TextUtils.isEmpty(user.getPassword()) && !TextUtils.isEmpty(user.getNickName())) {
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = DatabaseConnectManager.getInstance().getDatabaseConnection().prepareStatement(
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
     * @return 更新结果
     */
    public static User updateUser(User user) {
        if (user == null) {
            return null;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DatabaseConnectManager.getInstance().getDatabaseConnection().prepareStatement(
                    "UPDATE music.user set "
                            + "nick_name = ?, gender = ?, birth = ?, signature = ?"
                            + "WHERE account = ?");
            preparedStatement.setString(1, user.getNickName());
            preparedStatement.setInt(2, user.getGender());
            preparedStatement.setString(3, user.getBirth());
            preparedStatement.setString(4, user.getSignature());
            preparedStatement.setString(5, user.getAccount());
            if (preparedStatement.executeUpdate() > 0) {
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
        return null;
    }

    /**
     * 更改用户密码
     * @param user
     * @return 修改密码成功与否
     */
    public static boolean changePassword(User user) {
        if (user == null) {
            return false;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DatabaseConnectManager.getInstance().getDatabaseConnection().prepareStatement(
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
     * @param avatarUrl
     * @return 用户头像更改结果
     */
    public static boolean changeUserAvatarUrl(String account, String avatarUrl) {
        if (TextUtils.isEmpty(account)) {
            return false;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DatabaseConnectManager.getInstance().getDatabaseConnection().prepareStatement(
                    "UPDATE music.user set avatar_url = ? WHERE account = ?");
            preparedStatement.setString(1, account);
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
     * @return 用户查询结果
     */
    public static User queryUserByAccount(String account) {
        User user = null;
        if (TextUtils.isEmpty(account)) {
            return null;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DatabaseConnectManager.getInstance().getDatabaseConnection().prepareStatement(
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
     * @return 用户删除结果
     */
    public static boolean deleteUser(String account) {
        if (TextUtils.isEmpty(account)) {
            return false;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DatabaseConnectManager.getInstance().getDatabaseConnection().prepareStatement(
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
}
