import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private static final int PORT = 8888;
    private static Map<String, ClientInfo> clientMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("=== 聊天室服务端启动，端口：" + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message, String senderNick, String room) {
        Collection<ClientInfo> clients = clientMap.values();
        for (ClientInfo info : clients) {
            if (room.equals(info.getRoom()) && !info.getNick().equals(senderNick)) {
                try {
                    info.getOut().println(message);
                } catch (Exception e) {
                    // 忽略发送失败的客户端
                }
            }
        }
    }

    private static class ClientInfo {
        private Socket socket;
        private PrintWriter out;
        private String nick;
        private String room;

        public ClientInfo(Socket socket, PrintWriter out, String nick, String room) {
            this.socket = socket;
            this.out = out;
            this.nick = nick;
            this.room = room;
        }

        public Socket getSocket() { return socket; }
        public PrintWriter getOut() { return out; }
        public String getNick() { return nick; }
        public String getRoom() { return room; }
        public void setRoom(String room) { this.room = room; }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private String nick;
        private String room = "大厅";

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader in = null;
            PrintWriter out = null;

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // 昵称注册
                while (true) {
                    out.println("SYSTEM: 请输入昵称：");
                    nick = in.readLine();
                    if (nick == null) return;

                    String key = nick.toLowerCase();
                    if (clientMap.containsKey(key)) {
                        out.println("ERROR: 昵称已存在，请换一个");
                    } else {
                        clientMap.put(key, new ClientInfo(socket, out, nick, room));
                        out.println("SYSTEM: 欢迎 " + nick + "，当前在【大厅】");
                        broadcast("SYSTEM: " + nick + " 进入聊天室", nick, room);
                        System.out.println(nick + " 已连接");
                        break;
                    }
                }

                // 消息循环
                String msg;
                while ((msg = in.readLine()) != null) {
                    if ("exit".equalsIgnoreCase(msg)) break;

                    if (msg.startsWith("/")) {
                        handleCommand(msg, out);
                        continue;
                    }

                    String sendMsg = nick + "：" + msg;
                    System.out.println("[" + room + "] " + sendMsg);
                    broadcast(sendMsg, nick, room);
                }

            } catch (Exception e) {
                // 客户端断开很正常，不爆红
            } finally {
                if (nick != null) {
                    clientMap.remove(nick.toLowerCase());
                    broadcast("SYSTEM: " + nick + " 退出聊天室", nick, room);
                    System.out.println(nick + " 已断开");
                }
                try {
                    socket.close();
                } catch (IOException ignored) {}
            }
        }

        private void handleCommand(String cmd, PrintWriter out) {
            if (cmd.equalsIgnoreCase("/list")) {
                StringBuilder sb = new StringBuilder("SYSTEM: 在线用户：");
                for (ClientInfo info : clientMap.values()) {
                    sb.append(info.getNick()).append(" ");
                }
                out.println(sb);
            } else if (cmd.startsWith("/join ")) {
                String newRoom = cmd.substring(6).trim();
                if (newRoom.isEmpty()) {
                    out.println("SYSTEM: 用法：/join 房间名");
                    return;
                }
                String oldRoom = this.room;
                this.room = newRoom;
                clientMap.get(nick.toLowerCase()).setRoom(newRoom);
                out.println("SYSTEM: 已进入房间：" + newRoom);
                broadcast("SYSTEM: " + nick + " 离开 " + oldRoom, nick, oldRoom);
                broadcast("SYSTEM: " + nick + " 加入 " + newRoom, nick, newRoom);
            } else {
                out.println("SYSTEM: 未知命令，可用 /list /join");
            }
        }
    }
}