package utils;

import threadpoolservice.ThreadPoolService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class RequestProcessUtils {
    public static void processUserRequest(HttpServletRequest request, JspWriter out) {
        FutureTask<Boolean> futureTask = new FutureTask<>(new UserTask(request, out));
        ThreadPoolService.requestExcutor.submit(futureTask);
        try {
            futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
