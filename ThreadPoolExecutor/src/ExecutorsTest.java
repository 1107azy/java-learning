import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutorsTest {
    public static void main(String[] args) throws InterruptedException {

        // ========== 1. 单线程池 ==========
        ExecutorService single = Executors.newSingleThreadExecutor();
        System.out.println("=== 单线程池 ===");
        for (int i = 1; i <= 3; i++) {
            int n = i;
            single.execute(() -> System.out.println(Thread.currentThread().getName() + " 执行任务" + n));
        }
        single.shutdown();
        single.awaitTermination(1, TimeUnit.SECONDS);

        // ========== 2. 缓存线程池 ==========
        ExecutorService cached = Executors.newCachedThreadPool();
        System.out.println("\n=== 缓存线程池 ===");
        for (int i = 1; i <= 5; i++) {
            int n = i;
            cached.execute(() -> System.out.println(Thread.currentThread().getName() + " 执行任务" + n));
        }
        cached.shutdown();
        cached.awaitTermination(1, TimeUnit.SECONDS);

        // ========== 3. 定时线程池 ==========
        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(2);
        System.out.println("\n=== 定时线程池（延迟2秒执行）===");

        // 安排2秒后执行的任务
        scheduled.schedule(() -> System.out.println("定时任务执行"), 2, TimeUnit.SECONDS);

        // 先睡觉等它执行完，再关闭
        Thread.sleep(2500);
        scheduled.shutdown();
    }
}