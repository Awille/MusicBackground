package database.crud;

import bean.User;
import database.DatabaseConnectManager;
import sun.dc.pr.PRError;
import utils.HtmlFilterUtils;
import utils.TextUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class UserCrud {
    public static User addUser(User user) {
        if (!TextUtils.isEmpty(user.getAccount()) && !TextUtils.isEmpty(user.getPassword()) && !TextUtils.isEmpty(user.getNickName())) {
            if (DatabaseConnectManager.getInstance().autoConnect()) {
                try {
                    PreparedStatement preparedStatement = DatabaseConnectManager.getInstance().getDatabaseConnection().prepareStatement(
                            "INSERT INTO user (account, nick_name, password, gender, birth, signature, avatar_url) VALUES (?, ?, ?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, user.getAccount());
                    preparedStatement.setString(2, user.getNickName());
                    preparedStatement.setString(3, user.getPassword());
                    preparedStatement.setInt(4, user.getGender());
                    preparedStatement.setString(5, user.getBirth());
                    preparedStatement.setString(6, user.getSignature());
                    preparedStatement.setString(7, user.getAvatarUrl());


                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            } else {
                return null;
            }
        }
        return null;
    }
}
