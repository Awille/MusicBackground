package database.crud;

import bean.User;
import database.DatabaseConnectManager;
import utils.HtmlFilterUtils;
import utils.TextUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class UserCrud {
    public static User addUser(User user) {
        if (!TextUtils.isEmpty(user.getAccount()) && !TextUtils.isEmpty(user.getPassword()) && !TextUtils.isEmpty(user.getNickName())) {
            if (DatabaseConnectManager.getInstance().autoConnect()) {
                user.setAccount(HtmlFilterUtils.HtmlFilter(user.getAccount()));
                user.setPassword(HtmlFilterUtils.HtmlFilter(user.getPassword()));
                user.setAccount(HtmlFilterUtils.HtmlFilter(user.getAccount()));
                try {
                    PreparedStatement preparedStatement = DatabaseConnectManager.getInstance().getDatabaseConnection().prepareStatement(
                            "INSERT INTO user (account,nickname,password) VALUES (?,?,?)",
                            Statement.RETURN_GENERATED_KEYS);
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
