package utils;

import threadpoolservice.ThreadPoolService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class RequestProcessUtils {
    public static void processUserRequest(HttpServletRequest request, JspWriter out, ServletContext context) {
        FutureTask<Boolean> futureTask = new FutureTask<>(new UserTask(request, out, context));
        ThreadPoolService.requestExcutor.submit(futureTask);
        try {
            futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void processSongRequest(HttpServletRequest request, JspWriter out, ServletContext context) {
        FutureTask<Boolean> futureTask = new FutureTask<>(new SongTask(request, out, context));
        ThreadPoolService.requestExcutor.submit(futureTask);
        try {
            futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void processSongListRequest(HttpServletRequest request, JspWriter out, ServletContext context) {
        FutureTask<Boolean> futureTask = new FutureTask<>(new SongListTask(request, out, context));
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
