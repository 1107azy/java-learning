// 定义一个类，类名叫 HeapOOM
public class HeapOOM {

    // 定义一个空的内部类，用来创建对象占内存
    static class OOMObject {
    }

    // 程序入口 main 方法
    public static void main(String[] args) {
        try {
            // 创建一个 List 集合，用来保存对象引用，防止被 GC 回收
            java.util.List<OOMObject> list = new java.util.ArrayList<>();

            // 死循环，一直不停创建对象
            while (true) {
                // 每次循环都 new 一个新对象，加到 list 里
                list.add(new OOMObject());
            }

            // 捕获堆内存溢出错误
        } catch (OutOfMemoryError e) {
            // 打印提示信息
            System.out.println("发生堆内存溢出：" + e);
        }
    }
}
