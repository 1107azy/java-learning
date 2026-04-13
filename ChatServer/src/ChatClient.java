import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8888;

    public static void main(String[] args) {
        System.out.println("=== 聊天室客户端 ===");
        System.out.println("输入 exit 退出");

        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            new Thread(new ReceiveThread(socket)).start();

            String input;
            while (true) {
                input = scanner.nextLine();
                out.println(input);
                if ("exit".equalsIgnoreCase(input)) break;
            }
        } catch (IOException e) {
            System.out.println("与服务端断开连接");
        }
    }

    private static class ReceiveThread implements Runnable {
        private Socket socket;

        public ReceiveThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("服务端已关闭");
            }
        }
    }
}
