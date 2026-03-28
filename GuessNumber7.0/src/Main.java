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
        System.out.println("恭喜获得积分"+ s);
    }

    public void showScore(){
        System.out.println(name + "当前积分" + score);
    }

    //hashmap必须进行重写player类
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name,player.name);
    }
    @Override
    public int hashCode(){
        return Objects.hash(name);
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

    //保存单局得分，存入hashmap中
    private int SingleScore;
    public int getSingleScore() {
        return SingleScore;
    }

    @Override
    public void play(Player player) {
        this.SingleScore = 0 ;
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
                    SingleScore = calculateScore(guess);
                    player.addScore(SingleScore);
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

    //添加新的选项，是否进行下一轮游戏
    public static boolean isContinue(Scanner scanner){
        while (true){
            System.out.println("是否进行下一轮游戏（y/n)");
            String choose = scanner.next().trim().toLowerCase();
            if ("y".equals(choose)) return true;
            if ("n".equals(choose)) return false;
            System.out.println("只能输入y或n");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Player> list = new ArrayList<>();
        HashMap<Player,ArrayList<Integer>> recordMap = new HashMap<>();
        //自定义玩家数量
        int count = getCount();
        //玩家信息输入
        for(int i = 0 ; i < count; i++){
            try{
                System.out.println("请输入玩家名字");
                String name = scanner.nextLine();
                checkName(name);
                Player player = new Player(name);
                list.add(player);

                //初始化得分
                recordMap.put(player,new ArrayList<>());
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
            int fistScore =((GuessNumber)game).getSingleScore();
            recordMap.get(p).add(fistScore);
        }

        //新游戏逻辑
        while(isContinue(scanner)){
            System.out.println("新的一局开始了");
            for (Player p : list){
                System.out.println("当前玩家为"+p.getName());
                game.start(p);
                p.showScore();
                int singleScore =((GuessNumber)game).getSingleScore();
                recordMap.get(p).add(singleScore);
            }
        }

        //排序
        Collections.sort(list, new Comparator<Player>() {
            @Override
            public int compare(Player player1,Player player2){
                List<Integer> score1List = recordMap.get(player1);
                List<Integer> score2List = recordMap.get(player2);

                int sum1=0;
                int sum2=0;
                for (int s : score1List)sum1 = sum1 + s;
                for (int s : score2List)sum2 = sum2 + s;
                if (sum1 != sum2) {
                    return sum2 - sum1;
                }else {
                    double avg1 = 1.0 * sum1 / score1List.size();
                    double avg2 = 1.0 * sum2 / score2List.size();
                    return Double.compare(avg2,avg1);
                }

            }
        });

        //输出排行榜
        System.out.println("最终排行为");
        for (int i = 0 ; i < list.size() ; i++){
            Player p = list.get(i);
            List<Integer> allScore = recordMap.get(p);
            int totalRound =allScore.size();
            int totalScore= 0;
            for (int s:allScore){
                totalScore = totalScore + s;
            }
            double totalavg = 1.0*totalScore / totalRound;

            System.out.println("第"+(i+1)+"名为："+ p.getName() + "    总局数为："+ totalRound + "    总分数为："+ totalScore + "   平均分数为：" + totalavg);
            System.out.println("-----------------------------------------");
        }
        scanner.close();
    }
}