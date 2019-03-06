package bean;

import java.io.Serializable;

public class UploadFile implements Serializable {
    /**
     * 账户
     */
    private String account;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件字符串
     */
    private String fileStr;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileStr() {
        return fileStr;
    }

    public void setFileStr(String fileStr) {
        this.fileStr = fileStr;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
