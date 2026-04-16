import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// 共享队列
class ShareQueue {
    private final int[] items;
    private int putIndex;  // 存放索引
    private int takeIndex; // 取出索引
    private int count;     // 元素数量
    private final Lock lock = new ReentrantLock();
    // 生产者等待队列、消费者等待队列
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public ShareQueue(int size) {
        items = new int[size];
    }

    // 生产者放入数据
    public void put(int value) throws InterruptedException {
        lock.lock();
        try {
            // 队列满了，生产者等待
            while (count == items.length) {
                notFull.await();
            }
            items[putIndex] = value;
            putIndex = (putIndex + 1) % items.length;
            count++;
            System.out.println(Thread.currentThread().getName() + " 生产：" + value + "，库存：" + count);
            // 唤醒消费者
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    // 消费者取出数据
    public int take() throws InterruptedException {
        lock.lock();
        try {
            // 队列空了，消费者等待
            while (count == 0) {
                notEmpty.await();
            }
            int value = items[takeIndex];
            takeIndex = (takeIndex + 1) % items.length;
            count--;
            System.out.println(Thread.currentThread().getName() + " 消费：" + value + "，库存：" + count);
            // 唤醒生产者
            notFull.signal();
            return value;
        } finally {
            lock.unlock();
        }
    }
}

public class ProducerConsumerDemo {
    public static void main(String[] args) {
        ShareQueue queue = new ShareQueue(5);

        // 生产者线程
        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    queue.put(i);
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "生产者A").start();

        // 消费者线程
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    queue.take();
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "消费者B").start();
    }
}