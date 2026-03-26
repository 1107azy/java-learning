import java.util.*;

class Player{
    private String name;
    private int score;
    public Player(String name){
        this.name = name ;
        this.score=0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int s){
        score = score + s;
        System.out.println("恭喜获得积分"+ score);
    }

    public void showScore(){
        System.out.println(name + "当前积分" + score);
    }
}

interface calculateScore{
    int calculateScore(int s);
    String getGameName();

}
abstract class Game{
    public abstract void play(Player player);

    public void start(Player player){
        System.out.println("开始游戏");
        play(player);
    }
}

class GuessNumber extends Game implements calculateScore{

    @Override
    public void play(Player player) {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        int rightNumber = random.nextInt(100);
        int guess = 0 ;
        System.out.println(getGameName());

        while(true){
            System.out.println("请从1~100猜一个数字");
            try {
                int guessNumber = scanner.nextInt();
                guess ++ ;
                if (guessNumber < rightNumber){
                    System.out.println("小了");
                } else if (guessNumber > rightNumber) {
                    System.out.println("大了");
                }else {
                    System.out.println("恭喜"+player.getName()+"猜对了");
                    int score = calculateScore(guess);
                    player.addScore(score);
                    break;
                }
            }catch (InputMismatchException e){
                System.out.println("请重新输入");
                scanner.next();
            }
        }
    }

    @Override
    public int calculateScore(int guess) {
        return Math.max(0,100-guess);
    }

    @Override
    public String getGameName() {
        return "猜数字游戏";
    }
}

class InvalidException extends Exception{
    public InvalidException(String message){
        super(message);
    }
}
public class Main {
    public static void checkName(String name) throws InvalidException{
        if(name == null || name.trim().isEmpty()){
           throw new InvalidException("请输入名字");
        }
    }
    public static int getCount(){
        Scanner scanner = new Scanner(System.in);
        while (true){
            try{
                System.out.println("请输入玩家数量");
                int n = scanner.nextInt();
                if(n>=1){
                    return  n;
                }else {
                    System.out.println("至少输入一人");
                }
            }catch (Exception e) {
                System.out.println("请输入合法数字");
                scanner.next();
            }
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Player> list = new ArrayList<>();
        //自定义玩家数量
        int count = getCount();
        //玩家信息输入
        for(int i = 0 ; i < count; i++){
            try{
                System.out.println("请输入玩家名字");
                String name = scanner.nextLine();
                checkName(name);
                list.add(new Player(name));
            }catch (InvalidException e){
                System.out.println(e.getMessage());
                i--;
            }
        }
        //开始游戏
        Game game = new GuessNumber();
        System.out.println("游戏开始");
        for (Player p : list){
            System.out.println("当前玩家为"+p.getName());
            game.start(p);
            p.showScore();
        }
        //排序
        Collections.sort(list, new Comparator<Player>() {
            @Override
            public int compare(Player player1, Player player2) {
                if (player1.getScore()!=player2.getScore()){
                    return player2.getScore() - player1.getScore();
                }else {
                    return player1.getName().compareTo(player2.getName());
                }
            }
        });
        //输出排行榜
        System.out.println("最终排行为");
        for (int i = 0 ; i < list.size() ; i++){
            Player p = list.get(i);
            System.out.println("第"+(i+1)+"名："+p.getName()+"分数："+p.getScore());
        }
}
}