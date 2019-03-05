package bean;

public class Message {
    /**
     * 返回码
     * 0 业务成功
     * 1 业务失败
     */
    private String code;
    /**
     * 具体信息
     */
    private String message;
    /**
     * 返回数据
     */
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
