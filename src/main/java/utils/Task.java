package utils;

import javax.servlet.jsp.JspWriter;

public class Task implements Runnable {
    private Runnable realTask;
    private JspWriter jspWriter;

    public Task(JspWriter jspWriter, Runnable realTask) {
        this.jspWriter = jspWriter;
        this.realTask = realTask;
    }
    @Override
    public void run() {

    }
}
