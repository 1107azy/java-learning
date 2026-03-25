import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

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
        System.out.println(name + "当前分数为"+score);
    }
}

abstract class Game{
    public abstract void play(Player player);

    public void start(Player player){
        System.out.println("开始游戏");
        play(player);
    }
}

interface calculateScore{
    int calculateScore(int s);
    String getGamaName();
}

class GuessGame extends Game implements calculateScore {
    @Override
    public void play(Player player) {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        int rightNumber = random.nextInt(100);
        int guess = 0;
        String name = getGamaName();


        while (true) {
            System.out.println("请从1~100之间猜一个数");
                try {
                    int guessNumber = scanner.nextInt();
                    guess++;
                    if (guessNumber < rightNumber) {
                        System.out.println("小了");
                    } else if (guessNumber > rightNumber) {
                        System.out.println("大了");
                    } else {
                        System.out.println("恭喜你猜对了,猜了" + guess + "次");
                        int score = calculateScore(guess);
                        player.addScore(score);
                        break;
                    }
                }catch (InputMismatchException e){
                    System.out.println("输入的是非法数字");
                    scanner.next();
            }
        }
    }

    @Override
    public int calculateScore(int guess) {
        return Math.max(0, 100 - guess);
    }
    @Override
    public String getGamaName() {
        return "猜数字游戏";
    }
}

//创建自定义异常类
class InvaildInputExcetion extends Exception{
    public InvaildInputExcetion(String message){
        super(message);
    }
}

public class Main {
    public static void checkName(String name) throws InvaildInputExcetion {
        if (name == null || name.trim().isEmpty()) {
            throw new InvaildInputExcetion("姓名不能为空");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入你的名字");
        String name = scanner.nextLine();

        try {
            //检验名字
            checkName(name);

            //检验通过后继续游戏
            Player player = new Player(name);
            Game game = new GuessGame();
            game.start(player);
            player.showScore();

        }catch (InvaildInputExcetion e){
            System.out.println(e.getMessage());
            scanner.next();
        }
    }
}