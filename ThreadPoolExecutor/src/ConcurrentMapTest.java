import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentMapTest {
    public static void main(String[] args) throws InterruptedException {
        // 测试1：ConcurrentHashMap —— 安全，不报错
        Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        System.out.println("=== ConcurrentHashMap 迭代（安全）===");
        testMap(concurrentMap);

        Thread.sleep(1000);
        System.out.println("\n=====================================");

        // 测试2：HashMap —— 迭代时修改，抛出 ConcurrentModificationException
        Map<String, Integer> hashMap = new HashMap<>();
        System.out.println("=== HashMap 迭代（报错）===");
        testMap(hashMap);
    }

    private static void testMap(Map<String, Integer> map) throws InterruptedException {
        // 线程1：不断写入数据
        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                map.put("key" + i, i);
                try { Thread.sleep(50); } catch (Exception ignored) {}
            }
        }).start();

        // 线程2：遍历数据（HashMap 这里会报错）
        new Thread(() -> {
            try { Thread.sleep(100); } catch (Exception ignored) {}
            for (String key : map.keySet()) {
                System.out.println("遍历：" + key + " = " + map.get(key));
                try { Thread.sleep(50); } catch (Exception ignored) {}
            }
        }).start();
    }
}
