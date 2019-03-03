import java.util.concurrent.*;

public class ThreadPoolService {
    public static ThreadPoolExecutor requestExcutor = new ThreadPoolExecutor(
            5,
            100,
            10,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r);
                }
            });
}
