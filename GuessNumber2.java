import java.util.Random;
import java.util.Scanner;

//TIP 要<b>运行</b>代码，请按 <shortcut actionId="Run"/> 或
// 点击装订区域中的 <icon src="AllIcons.Actions.Execute"/> 图标。
class Player{
    private String name;
    private int guessCount;
    public Player(String name){
        this.name=name;
        this.guessCount = 0;
    }
    //引入方法
    public void addCount(){
        guessCount++;
    }

    public String getName() {
        return name;
    }

    public int getGuessCount() {
        return guessCount;
    }
}

public class GuessNumber2 {
    public static void main(String[] args) {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        Player player = new Player("LI");
        int rightNumber = random.nextInt(100);
        System.out.println("猜数字小游戏");
        while (true) {
            System.out.println("输入数字");
            int num = scanner.nextInt();    //输入一个数
            player.addCount();
            if (num < rightNumber) {
                System.out.println("小了");
            } else if (num > rightNumber) {
                System.out.println("大了");
            } else {
                System.out.println("答对了");
                System.out.println(player.getName() + "猜了" + player.getGuessCount()+"次");
                break;
            }
        }
        scanner.close();
    }
}
