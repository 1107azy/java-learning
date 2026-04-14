// 定义类 StackSOF
public class StackSOF {

    // 定义一个方法，自己调用自己（递归）
    private static void recursiveCall() {
        // 方法内部又调用自己
        recursiveCall();
    }

    // 程序入口
    public static void main(String[] args) {
        try {
            // 调用这个递归方法
            recursiveCall();

            // 捕获栈溢出错误
        } catch (StackOverflowError e) {
            System.out.println("发生栈溢出：" + e);
        }
    }
}
