import java.io.*;
import java.util.*;

class Player {
    private String name;
    private int score;

    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int s) {
        score = score + s;
        synchronized (System.out) {
            System.out.println("本次获得 " + s + " 分，当前总分：" + score);
        }
    }

    public void showScore() {
        synchronized (System.out) {
            System.out.println(name + "当前积分" + score);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

interface calculateScore {
    int calculateScore(int attempts);
    String getGameName();
}

// 抽象游戏类，定义模板
abstract class Game {
    public abstract int play(Player player);  // 改为返回本局得分
}

// 自动猜数字游戏（无手动输入）
class AutoGuessNumber extends Game implements calculateScore {
    private final int targetNumber;
    private final String gameName = "猜数字游戏";

    public AutoGuessNumber() {
        Random random = new Random();
        this.targetNumber = random.nextInt(100) + 1; // 1~100
    }

    @Override
    public int play(Player player) {
        int attempts = 0;
        while (true) {
            int guess = (int) (Math.random() * 100) + 1; // 自动随机猜测
            attempts++;
            if (guess == targetNumber) {
                int score = calculateScore(attempts);
                player.addScore(score);
                synchronized (System.out) {
                    System.out.println(player.getName() + " 猜中了！目标数字：" + targetNumber + "，用了 " + attempts + " 次，获得 " + score + " 分");
                }
                return score;
            } else {
                // 可选：输出提示（为了减少输出混乱，注释掉）
                // synchronized (System.out) {
                //     System.out.println(player.getName() + " 猜了 " + guess + "，不对");
                // }
            }
        }
    }

    @Override
    public int calculateScore(int attempts) {
        return Math.max(0, 100 - attempts);
    }

    @Override
    public String getGameName() {
        return gameName;
    }
}

class InvalidException extends Exception {
    public InvalidException(String message) {
        super(message);
    }
}

// 多线程任务：每个玩家独立运行一局游戏
class PlayGameTask implements Runnable {
    private final Player player;
    private final HashMap<Player, ArrayList<Integer>> recordMap;

    public PlayGameTask(Player player, HashMap<Player, ArrayList<Integer>> recordMap) {
        this.player = player;
        this.recordMap = recordMap;
    }

    @Override
    public void run() {
        // 每个线程创建自己独立的游戏实例
        AutoGuessNumber game = new AutoGuessNumber();
        synchronized (System.out) {
            System.out.println("\n" + player.getName() + " 开始游戏");
        }
        int score = game.play(player);
        player.showScore();
        synchronized (recordMap) {
            recordMap.get(player).add(score);
        }
    }
}

public class Main {
    public static void checkName(String name) throws InvalidException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidException("请输入名字");
        }
    }

    public static int getCount() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("请输入玩家数量");
                int n = scanner.nextInt();
                if (n >= 1) {
                    scanner.nextLine(); // 吸收换行
                    return n;
                } else {
                    System.out.println("至少输入一人");
                }
            } catch (Exception e) {
                System.out.println("请输入合法数字");
                scanner.next();
            }
        }
    }

    public static boolean isContinue(Scanner scanner) {
        while (true) {
            System.out.print("是否进行下一轮游戏（y/n）：");
            String choose = scanner.next().trim().toLowerCase();
            if ("y".equals(choose)) return true;
            if ("n".equals(choose)) return false;
            System.out.println("只能输入y或n");
        }
    }

    private static final String SCORE_FILE = "player_scores.txt";

    public static void saveScoresFile(HashMap<Player, ArrayList<Integer>> recordMap) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCORE_FILE))) {
            for (Map.Entry<Player, ArrayList<Integer>> entry : recordMap.entrySet()) {
                String name = entry.getKey().getName();
                List<Integer> scores = entry.getValue();
                StringBuilder sb = new StringBuilder();
                sb.append(name).append("|");
                for (int i = 0; i < scores.size(); i++) {
                    sb.append(scores.get(i));
                    if (i < scores.size() - 1) {
                        sb.append("，");
                    }
                }
                bw.write(sb.toString());
                bw.newLine();
            }
            System.out.println("成绩已保存文件 " + SCORE_FILE);
        } catch (IOException e) {
            System.out.println("保存失败");
            e.printStackTrace();
        }
    }

    public static HashMap<Player, ArrayList<Integer>> loadScoresFromFile() {
        HashMap<Player, ArrayList<Integer>> recordMap = new HashMap<>();
        File file = new File(SCORE_FILE);
        if (!file.exists()) return recordMap;

        try (BufferedReader br = new BufferedReader(new FileReader(SCORE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split1 = line.split("\\|");
                if (split1.length < 2) continue;
                String name = split1[0];
                String[] scoreStrs = split1[1].split("，");
                Player player = new Player(name);
                ArrayList<Integer> scoreList = new ArrayList<>();
                for (String s : scoreStrs) {
                    try {
                        scoreList.add(Integer.parseInt(s.trim()));
                    } catch (Exception ignored) {
                    }
                }
                recordMap.put(player, scoreList);
            }
            System.out.println("已读取历史成绩");
        } catch (IOException e) {
            System.out.println("读取失败");
        }
        return recordMap;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Player> list = new ArrayList<>();
        HashMap<Player, ArrayList<Integer>> recordMap = loadScoresFromFile();

        if (!recordMap.isEmpty()) {
            for (Player p : recordMap.keySet()) {
                list.add(p);
                int totalScore = 0;
                for (int s : recordMap.get(p)) {
                    totalScore += s;
                }
                p.setScore(totalScore);
            }
            System.out.println("已加载历史玩家，共 " + list.size() + " 人");
        } else {
            int count = getCount();
            for (int i = 0; i < count; i++) {
                try {
                    System.out.println("请输入玩家名字");
                    String name = scanner.nextLine();
                    checkName(name);
                    Player player = new Player(name);
                    list.add(player);
                    recordMap.put(player, new ArrayList<>());
                } catch (InvalidException e) {
                    System.out.println(e.getMessage());
                    i--;
                }
            }
        }

        // 多线程进行第一轮游戏
        System.out.println("游戏开始（多线程自动猜数字）");
        List<Thread> threads = new ArrayList<>();
        for (Player p : list) {
            PlayGameTask task = new PlayGameTask(p, recordMap);
            Thread thread = new Thread(task, p.getName() + "线程");
            threads.add(thread);
            thread.start();
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 后续轮次
        while (isContinue(scanner)) {
            System.out.println("\n========== 新的一局（多线程） ==========");
            threads.clear();
            for (Player p : list) {
                PlayGameTask task = new PlayGameTask(p, recordMap);
                Thread thread = new Thread(task, p.getName() + "线程");
                threads.add(thread);
                thread.start();
            }
            for (Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // 排序（按总分降序，总分相同按平均分降序）
        Collections.sort(list, new Comparator<Player>() {
            @Override
            public int compare(Player player1, Player player2) {
                List<Integer> score1List = recordMap.get(player1);
                List<Integer> score2List = recordMap.get(player2);
                int sum1 = 0, sum2 = 0;
                for (int s : score1List) sum1 += s;
                for (int s : score2List) sum2 += s;
                if (sum1 != sum2) {
                    return sum2 - sum1;
                } else {
                    double avg1 = 1.0 * sum1 / score1List.size();
                    double avg2 = 1.0 * sum2 / score2List.size();
                    return Double.compare(avg2, avg1);
                }
            }
        });

        // 输出最终排行榜
        System.out.println("\n最终排行为");
        for (int i = 0; i < list.size(); i++) {
            Player p = list.get(i);
            List<Integer> allScore = recordMap.get(p);
            int totalRound = allScore.size();
            int totalScore = 0;
            for (int s : allScore) totalScore += s;
            double totalavg = 1.0 * totalScore / totalRound;
            System.out.println("第" + (i + 1) + "名为：" + p.getName() + "    总局数为：" + totalRound
                    + "    总分数为：" + totalScore + "   平均分数为：" + totalavg);
            System.out.println("-----------------------------------------");
        }

        saveScoresFile(recordMap);
        scanner.close();
    }
}