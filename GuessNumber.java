import java.util.Random;
import java.util.Scanner;

//TIP 要<b>运行</b>代码，请按 <shortcut actionId="Run"/> 或
// 点击装订区域中的 <icon src="AllIcons.Actions.Execute"/> 图标。
public class GuessNumber {
    public static void main(String[] args) {

        Random random = new Random();
        int rightnumber = random.nextInt(100);
        int count = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("猜数字游戏");
        System.out.println("请猜从1-100数字");
        while(true){
            System.out.println("作出你的猜测");
            String input = scanner.next();
            int guess;
            try{
                guess = Integer.parseInt(input);
                count ++;
                if(guess> rightnumber){
                    System.out.println("大了");
                } else if (guess < rightnumber) {
                    System.out.println("小了");
                }
                else {
                    System.out.println("你猜对了");
                    System.out.println("你猜了" + count+"次");
                    break;
                }
            }
            catch (NumberFormatException e){
                System.out.println("请输入合法数字");
            }
        }
        }
    }