package MapGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleshipMapCreator implements BattleshipGenerator {

    private final int height = 10;
    private final int width = 10;
    private final String[][] map = new String[width][height];
    private final String waterSymb = ".";
    private final String shipSymb = "#";
    private final String currentShipSymb = "$";

    @Override
    public String generateMap() {
        putWaterOnMap();
        final int singleShips = 4;
        final int doubleShips = 3;
        final int tripleShips = 2;
        final int quadrupleShips = 1;

        for (int i = 0; i < singleShips; i++)
            putNMastedShipOnMap(1);
        for (int i = 0; i < doubleShips; i++)
            putNMastedShipOnMap(2);
        for (int i = 0; i < tripleShips; i++)
            putNMastedShipOnMap(3);
        for (int i = 0; i < quadrupleShips; i++)
            putNMastedShipOnMap(4);

        return resultToString();
    }

    private String resultToString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                result.append(map[i][j]);
            }
        }
        return result.toString();
    }

    private void putWaterOnMap() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = waterSymb;
            }
        }
    }

    private boolean coordinatesInsideMap(int height, int width) {
        return 0 <= height && height < this.height
                && 0 <= width && width < this.width;
    }

    private Step randomGo(int randomHeight, int randomWidth) {
        Random random = new Random();
        int randomWay = random.nextInt(4);

        switch (randomWay) {
            case 0 -> randomHeight -= 1;
            case 1 -> randomHeight += 1;
            case 2 -> randomWidth -= 1;
            case 3 -> randomWidth += 1;
        }

        if (!coordinatesInsideMap(randomHeight, randomWidth)) {
            switch (randomWay) {
                case 0 -> randomHeight += 1;
                case 1 -> randomHeight -= 1;
                case 2 -> randomWidth += 1;
                case 3 -> randomWidth -= 1;
            }
        }

        return new Step(randomHeight, randomWidth);
    }

    private void putNMastedShipOnMap(int n) {
        int randomHeight;
        int randomWidth;

        do {
            Random random = new Random();
            clearCurrentShip();

            do {
                randomHeight = random.nextInt(height);
                randomWidth = random.nextInt(width);
            } while (map[randomHeight][randomWidth].equals(shipSymb));

            map[randomHeight][randomWidth] = currentShipSymb;

            for (int i = 1; i < n; i++) {
                do {
                    Step randomStep = randomGo(randomHeight, randomWidth);
                    randomHeight = randomStep.height();
                    randomWidth = randomStep.width();

                } while (map[randomHeight][randomWidth].equals(currentShipSymb)
                        || map[randomHeight][randomWidth].equals(shipSymb));

                map[randomHeight][randomWidth] = currentShipSymb;
            }
        } while (!mapIsCorrect());

        saveShip();
    }

    private void saveShip() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (map[i][j].equals(currentShipSymb))
                    map[i][j] = shipSymb;
            }
        }
    }

    private void clearCurrentShip() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (map[i][j].equals(currentShipSymb))
                    map[i][j] = waterSymb;
            }
        }
    }

    private boolean obstaclesAroundPoint(int i, int j) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (coordinatesInsideMap(i+x, j+y)
                        && map[i+x][j+y].equals(shipSymb)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean mapIsCorrect() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (map[i][j].equals(currentShipSymb) && obstaclesAroundPoint(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

}