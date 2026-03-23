import java.text.Normalizer;
import java.util.Random;
import java.util.Scanner;

class Player{
    protected String name;
    protected int score;
    public Player(String name){
        this.name=name;
        this.score = 0;
    }
    public void playgame(){
        score = score + 10;
        System.out.println("当前得分为"+score);
    }
    public void ShowScore(){
        System.out.println("玩家"+ name + "得分为" + score);
    }
}
class VIPPlayer extends Player{

    public VIPPlayer(String name,int score){
        super(name);
        this.score = score ;
    }
@Override
    public void ShowScore(){
        super.ShowScore();
        System.out.println("玩家" + name + "得分为" + score);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Player normalPlayer = new Player("小红");
        VIPPlayer vipPlayer = new VIPPlayer("小明",50);

        System.out.println("小红开始游戏");
        GuessNumberGame game1 = new GuessNumberGame();
        game1.startGame();
        boolean guessJudge = false;
        while(!guessJudge){
            System.out.println("请输入你猜的数字");
            int num = scanner.nextInt();
            guessJudge = game1.guess(normalPlayer,num);
        }
        normalPlayer.ShowScore();

        System.out.println("小明开始游戏");
        GuessNumberGame game2 = new GuessNumberGame();
        game2.startGame();
        guessJudge = false ;
        while(!guessJudge){
            System.out.println("请输入你猜的数字");
            int num = scanner.nextInt();
            guessJudge = game2.guess(vipPlayer,num);
        }
        vipPlayer.ShowScore();

        System.out.println("\n 多态玩家展示");
        Player[] allPlayer = {normalPlayer , vipPlayer};
        for (Player p : allPlayer){
            p.ShowScore();
            System.out.println("---");
        }
        scanner.close();
    }
}