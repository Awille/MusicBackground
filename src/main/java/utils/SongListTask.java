package utils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import java.util.concurrent.Callable;

public class SongListTask implements Callable<Boolean> {
    private HttpServletRequest myRequest;
    private JspWriter myOut;
    private ServletContext myContext;

    public SongListTask(HttpServletRequest myRequest, JspWriter myOut, ServletContext myContext) {
        this.myRequest = myRequest;
        this.myOut = myOut;
        this.myContext = myContext;
    }


    @Override
    public Boolean call() throws Exception {

        return null;
    }
}
