package commonconstant;

public interface CommonConstant {
    //数据库相关
    interface Database {
        String host = "localhost";
        String port = "3306";
        String databaseName = "music";
        String userName = "root";
        String password = "";
    }
    //数据库操作结果
    interface Result {
        String SUCCESS_CODE = "0";
        String FAIL_CODE = "-1";

        String SUCCESS_MSG = "success";
        String FAIL_MSG = "fail";
    }
}
