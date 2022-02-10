import MapGenerator.Coordinates;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Game implements Runnable {

    private BufferedWriter out;
    private BufferedReader in;
    private final Map myMap = new Map();
    private final Map enemyMap = new Map();
    private final Mode mode;
    private int countErr = 0;
    private String lastMsg = "";
    private String dove = null;

    public Game(Socket s, Mode mode, String mapPath) {

        setTimeout(s, 1000);
        try {
            out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8));
            in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        generateMaps(mapPath);
        this.mode = mode;
    }

    private void setTimeout(Socket s, int i) {

        try {
            s.setSoTimeout(i);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        introductionProcedure();
        if (this.mode == Mode.CLIENT) {
            sendingStart();
        }
        while (true) {
            shootAndTakeShots();
        }
    }

    private void shootAndTakeShots() {

        try {
            try {
                dove = in.readLine();
            } catch (SocketTimeoutException e) {
                tryAgainSendMessage();
                if (countErr > 3) {
                    communicationError();
                } else {
                    shootAndTakeShots();
                }
            }

            System.out.println("Jako " + this.mode + " przyjąłem wiadomość: \"" + dove + "\"");

            if (dove.equals("ostatni zatopiony")) {
                myWinningProcedure();
            }

            var statement = new Statement(dove.substring(0, dove.indexOf(';')), dove.substring(dove.indexOf(';') + 1));
            while (!statement.status().equals("trafiony zatopiony") && !statement.status().equals("trafiony")
                    && !statement.status().equals("pudło") && !statement.status().equals("ostatni zatopiony") && !statement.status().equals("start")) {
                if (countErr > 3) {
                    communicationError();
                }
                tryAgainReceivingMessage();
                statement.changeParameters(dove.substring(0, dove.indexOf(';')), dove.substring(dove.indexOf(';') + 1));
            }

            Coordinates coordinatesFromEnemyShot = myMap.shotToField(statement.field());
            switch (statement.status()) {
                case "trafiony zatopiony", "ostatni zatopiony" -> enemyMap.changeChars('$', '!');
                case "trafiony" -> enemyMap.changeChars('$', '#');
                default -> enemyMap.changeChars('$', '.');
            }

            String toSend;
            if (myMap.map[coordinatesFromEnemyShot.x()][coordinatesFromEnemyShot.y()] == '@') {
                toSend = "trafiony";
            } else {
                int shipPosition = myMap.coordInShipsList(coordinatesFromEnemyShot);
                if (shipPosition != -1) {
                    myMap.ships.get(shipPosition).remove(coordinatesFromEnemyShot);

                    if (myMap.ships.get(shipPosition).size() == 0 && myMap.countShips() > 0) {
                        toSend = "trafiony zatopiony";
                    } else if (myMap.ships.get(shipPosition).size() == 0 && myMap.countShips() == 0) {
                        toSend = "ostatni zatopiony";
                    } else {
                        toSend = "trafiony";
                    }
                    myMap.map[coordinatesFromEnemyShot.x()][coordinatesFromEnemyShot.y()] = '@';
                } else {
                    toSend = "pudło";
                    myMap.map[coordinatesFromEnemyShot.x()][coordinatesFromEnemyShot.y()] = '~';
                }
            }

            if (!toSend.contains("ostatni")) {
                var shot = randomShot();
                toSend += ";" + shot;
                var c = enemyMap.shotToField(shot);
                if (enemyMap.map[c.x()][c.y()] != '!') {
                    enemyMap.map[c.x()][c.y()] = '$';
                }
            }

            lastMsg = toSend;
            sendMessage(toSend);
            System.out.println("Jako " + this.mode + " wysyłam wiadomość \"" + toSend + "\"");

            if (toSend.contains("ostatni")) {
                myLosingProcedure();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        restartDoveBufferAndErrors();
    }

    private void restartDoveBufferAndErrors() {
        dove = null;
        countErr = 0;
    }

    private String randomShot() {
        Random r = new Random();
        String letters = "ABCDEFGHIJ";

        char randomCharPart = letters.charAt(r.nextInt(letters.length()));
        String randomNumberPart = String.valueOf(r.nextInt(10) + 1);
        return randomCharPart + randomNumberPart;
    }

    private void introductionProcedure() {
        myMap.printMap();
        System.out.println("Jestem sobie " + this.mode);
    }

    private void myWinningProcedure() {
        System.out.println("Wygrana");
        enemyMap.changeChars('$', '#');
        enemyMap.changeChars('?', '.');

        endingProcedure();
        System.exit(0);
    }

    private void sendMessage(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void endingProcedure() {
        enemyMap.discoverFieldsBySunkenShips();
        myMap.discoverFieldsBySunkenShips();

        enemyMap.printMap();
        System.out.println();
        myMap.printMap();
    }

    private void communicationError() {
        System.out.println("Błąd komunikacji");
        System.exit(0);
    }

    private void myLosingProcedure() {
        System.out.println("Przegrana");
        endingProcedure();
        System.exit(0);
    }

    private void generateMaps(String mapPath) {
        myMap.generateMapFromFile(mapPath);
        myMap.parser();
        enemyMap.generateUnknownMap();
    }

    private void sendingStart() {
        String shot = randomShot();
        String toSend = "start;" + shot;

        lastMsg = toSend;
        System.out.println(this.mode + ": " + toSend);

        var c = enemyMap.shotToField(shot);
        enemyMap.map[c.x()][c.y()] = '$';
        sendMessage(toSend);
    }

    private void tryAgainReceivingMessage() throws IOException {
        tryAgainSendMessage();
        dove = in.readLine();
    }

    private void tryAgainSendMessage() {
        countErr += 1;
        sendMessage(lastMsg);
    }
}
