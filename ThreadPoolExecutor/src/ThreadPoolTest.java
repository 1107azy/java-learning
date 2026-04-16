import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {
    public static void main(String[] args) {
        // 自定义线程池：核心5，最大5，无超时，无界队列
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5,                  // corePoolSize
                5,                  // maximumPoolSize
                1L,                 // keepAliveTime
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),  // 任务队列
                new ThreadPoolExecutor.AbortPolicy() // 拒绝策略
        );

        // 提交20个任务
        for (int i = 1; i <= 20; i++) {
            int taskNum = i;
            executor.execute(() -> {
                System.out.println("线程名：" + Thread.currentThread().getName()
                        + "，执行任务：" + taskNum);
                try {
                    Thread.sleep(100); // 模拟任务执行
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown(); // 关闭线程池
    }
}
