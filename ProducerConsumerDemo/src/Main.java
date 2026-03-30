import java.util.LinkedList;
import java.util.Random;

public class Main {
    //建立消息队列
    static class MessageQueue {
        private final LinkedList<Integer> queue = new LinkedList<>();
        private static final int MAX_CAPACITY = 5;

        //生产者逻辑
        public synchronized void put(int value) throws InterruptedException {
            while (queue.size() == MAX_CAPACITY) {
                wait();
            }
            queue.add(value);
            System.out.println(Thread.currentThread().getName() + "放入" + value + "  队列大小：" + queue);
            notifyAll();
        }

        //消费者逻辑
        public synchronized int take() throws InterruptedException {
            while (queue.isEmpty()) {
                wait();
            }
            int val = queue.removeFirst();
            System.out.println(Thread.currentThread().getName() + "取出：" + val + " 队列大小：" + queue);
            notifyAll();
            return val;
        }

        //生产者线程
        static class Producer implements Runnable {
            private final MessageQueue queue;
            private final Random random = new Random();

            public Producer(MessageQueue queue) {
                this.queue = queue;
            }

            @Override
            public void run() {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        int num = random.nextInt(100) + 1;
                        queue.put(num);
                        Thread.sleep(random.nextInt(501));
                    }
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + "停止生产");
                }
            }
        }

        //消费者线程
        static class Consumer implements Runnable {
            private final MessageQueue queue;
            private final Random random = new Random();

            public Consumer(MessageQueue queue) {
                this.queue = queue;
            }

            @Override
            public void run() {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        int num = queue.take();
                        System.out.println(Thread.currentThread().getName() + "消费：" + num);
                        Thread.sleep(random.nextInt(1001));
                    }
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + "停止消费");
                }
            }
        }

        public static void main(String[] args) throws InterruptedException {
            MessageQueue queue = new MessageQueue();

            //两个生产者线程
            Thread p1 = new Thread(new Producer(queue), "生产者-1");
            Thread p2 = new Thread(new Producer(queue), "生产者-2");

            //三个消费者线程
            Thread c1 = new Thread(new Consumer(queue), "消费者-1");
            Thread c2 = new Thread(new Consumer(queue), "消费者-2");
            Thread c3 = new Thread(new Consumer(queue), "消费者-3");

            //启动线程
            p1.start();
            p2.start();
            c1.start();
            c2.start();
            c3.start();

            //运行十秒
            System.out.println("运行十秒");
            Thread.sleep(10000);

            //中断线程
            p1.interrupt();
            p2.interrupt();
            c1.interrupt();
            c2.interrupt();
            c3.interrupt();

            //等待结束
            p1.join();
            p2.join();
            c1.join();
            c2.join();
            c3.join();

            System.out.println("运行结束");
        }
    }
}

