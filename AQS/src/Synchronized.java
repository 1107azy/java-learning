public class Synchronized {
    public static class SyncCounter {
        private int count = 0;

        // 同步方法保证线程安全
        public synchronized void increment() {
            count++;
        }

        public int getCount() {
            return count;
        }

        public static void main(String[] args) throws InterruptedException {
            SyncCounter counter = new SyncCounter();
            long start = System.currentTimeMillis();

            // 10个线程，每个累加10万次
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
            System.out.println("synchronized 结果：" + counter.getCount());
            System.out.println("耗时：" + (System.currentTimeMillis() - start) + "ms");
        }
    }
}
