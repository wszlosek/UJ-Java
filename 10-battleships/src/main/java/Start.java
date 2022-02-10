import java.io.IOException;

public class Start {

    private Mode mode;
    private int port;
    private String address;

    public static void main(String[] args) {
        if (args.length != 6 && args.length != 8) {
            return;
        }

        var newGame = new Start();
        newGame.modeValid(args[1]);
        newGame.portValid(args[3]);

        switch (newGame.mode) {
            case SERVER -> {
                try {
                    Server.main(new String[]{args[5]}, newGame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            case CLIENT -> {
                newGame.addressValid(args[5]);
                try {
                    Client.main(new String[]{args[7]}, newGame);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            default -> System.out.println("Parameter must contain 'server' or 'client'!");
        }
    }

    private void modeValid(String mode) {
        this.mode = switch (mode) {
            case "server" -> Mode.SERVER;
            case "client" -> Mode.CLIENT;
            default -> null;
        };
    }

    private void portValid(String port) {
        try {
            this.port = Integer.parseInt(port);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Port must be a number");
            System.exit(0);
        }
    }

    public void addressValid(String address) {
        this.address = address;
    }

    public Mode mode() {
        return mode;
    }

    public int port() {
        return port;
    }

    public String address() {
        return address;
    }
}
