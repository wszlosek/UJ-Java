import java.io.IOException;
import java.net.Socket;

public class Client {

    private Mode mode;
    private int port;
    private String address;
    private Socket socket;

    public static void main(String[] map, Start s) throws IOException {
        var c = new Client();
        c.importData(s);
        c.socket = new Socket(c.address, c.port);
        Game session = new Game(c.socket, c.mode, map[0]);
        new Thread(session, "Client").start();
    }

    private void importData(Start s) {
        mode = s.mode();
        port = s.port();
        address = s.address();
    }

    public void importDataFromCode() {
        address = "192.168.0.10";
        port = 6666;
        mode = Mode.CLIENT;
    }
}
