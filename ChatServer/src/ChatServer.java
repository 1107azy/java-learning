import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
    private static final int PORT = 8888;
    private static Set<Socket> clientSockets = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("=== 聊天室服务端启动，端口：" + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("新客户端连接：" + clientSocket);

                synchronized (clientSockets) {
                    clientSockets.add(clientSocket);
                }
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message, Socket senderSocket) {
        synchronized (clientSockets) {
            for (Socket socket : clientSockets) {
                if (socket != senderSocket) {
                    try {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println(message);
                    } catch (IOException e) {
                        System.out.println("客户端发送失败");
                    }
                }
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String message;
                while ((message = in.readLine()) != null) {
                    if ("exit".equalsIgnoreCase(message)) break;
                    System.out.println("收到消息：" + message);
                    broadcast(message, clientSocket);
                }
            } catch (IOException e) {
                System.out.println("客户端断开连接");
            } finally {
                synchronized (clientSockets) {
                    clientSockets.remove(clientSocket);
                }
                try {
                    clientSocket.close();
                } catch (IOException e) { }
            }
        }
    }
}
