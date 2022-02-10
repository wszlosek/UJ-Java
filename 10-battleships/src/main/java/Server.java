import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private final ServerSocket serverSocket;
    private String map;

    private Server(InetAddress address, int port) throws IOException {
        serverSocket = new ServerSocket(port, 10000, address);
        System.out.println("Running Battleships at address: " + address + ", port: " + port);
    }

    public static void main(String[] args, Start s) throws Exception {
        InetAddress addr = SrvUtil.findAddress();
        Server server = new Server(addr, s.port());
        server.map = args[0];
        new Thread(server, "Server").start();
    }

    @Override
    public void run() {
        try {
            Socket socket = serverSocket.accept();
            System.out.println("Got request from " + socket.getRemoteSocketAddress());
            Game session = new Game(socket, Mode.SERVER, this.map);
            new Thread(session, "Server").start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
