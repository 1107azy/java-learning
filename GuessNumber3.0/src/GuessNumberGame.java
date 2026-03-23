import java.util.Random;
import java.util.Scanner;

public class GuessNumberGame{
    private int rightNumber;
    private Scanner scanner = new Scanner(System.in);
    public void startGame(){
        Random random = new Random();
        rightNumber = random.nextInt(100);
        System.out.println("猜数字游戏");
    }
    public boolean guess(Player player,int guess){
        if (guess < rightNumber){
            System.out.println("小了");
            return false;
        } else if (guess > rightNumber) {
            System.out.println("大了");
            return false;
        }
        else{
            System.out.println("恭喜"+player.name+"答对了 \n");
            player.playgame();
            return true;
        }
    }
}
