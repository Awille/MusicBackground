package threadpoolservice;

import java.util.concurrent.*;

public class ThreadPoolService {
    /**
     * 用于处理请求的线程池
     */
    public static ThreadPoolExecutor requestExcutor = new ThreadPoolExecutor(
            5,
            100,
            100,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r);
                }
            });

    /**
     * 用于处理数据回退的线程池
     */
    public static ThreadPoolExecutor backDataExcutor = new ThreadPoolExecutor(
            5,
            100,
            100,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r);
                }
            });

}
