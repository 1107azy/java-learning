import java.util.Random;
import java.util.Scanner;

abstract class Game{
    //定义游戏内容的抽象方法
    public abstract void play(Player player);

    //游戏开始按钮
    public void start(Player player){
        System.out.println("游戏开始");
        play(player);
    }
}

//接口
interface calculateScore{
    int calculateScore(int guess);
    String getGameName();
}

class GuessNumber extends Game implements calculateScore{
    @Override
    public void play(Player player) {
        //基本定量定义
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        int guess = 0;
        int rightNumber = random.nextInt(100);
        String gameName = getGameName();
        System.out.println(gameName);

        //开始猜
        while (true) {
            System.out.println("请猜测0~100的一个数");
            int guessNumber = scanner.nextInt();
            guess++;
            if (guessNumber < rightNumber) {
                System.out.println("小了");
            } else if (guessNumber > rightNumber) {
                System.out.println("大了");
            } else {
                System.out.println("恭喜你猜对了,猜了"+ guess + "次");
                int score = calculateScore(guess);
                player.addScore(score);
                break;
            }
        }
    }
    //接口应用规则重写
    @Override
    public int calculateScore(int guess){
        return Math.max(0,101-guess);
    }
    @Override
    public String getGameName(){
        return "猜数字游戏";
    }
}

//创建玩家类
class Player{
    private String name;
    private int score;
    public  Player(String name){
        this.name = name;
        this.score = 0;
    }

    public void addScore(int s){
        score = score + s;
        System.out.println("获得分数" + score);
    }

    public void showScore(){
        System.out.println(name + "当前积分为" + score);
    }

}
public class Main {
    public static void main(String[] args) {
        Player player = new Player("小明");
        Game game = new GuessNumber();

        game.start(player);
        System.out.println("游戏结束\n");
        player.showScore();
    }
}