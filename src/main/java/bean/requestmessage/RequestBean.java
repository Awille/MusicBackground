package bean.requestmessage;

import java.io.Serializable;

public class RequestBean implements Serializable {
    /**
     * 服务类型
     */
    private String service;
    /**
     * 请求body数据
     */
    private Object data;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
