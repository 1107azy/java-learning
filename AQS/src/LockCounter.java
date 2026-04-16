import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockCounter {
    private int count = 0;
    // 非公平锁（默认），性能更高
    private final Lock lock = new ReentrantLock();

    public void increment() {
        lock.lock(); // 加锁
        try {
            count++;
        } finally {
            lock.unlock(); // 必须finally释放锁
        }
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        LockCounter counter = new LockCounter();
        long start = System.currentTimeMillis();

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100000; j++) {
                    counter.increment();
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) t.join();
        System.out.println("ReentrantLock 结果：" + counter.getCount());
        System.out.println("耗时：" + (System.currentTimeMillis() - start) + "ms");
    }
}
