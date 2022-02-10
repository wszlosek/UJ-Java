import MapGenerator.Coordinates;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Map {

    public char[][] map = new char[10][10];
    List<List<Coordinates>> ships = new ArrayList<>();

    public void printMap() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }

    public void generateMapFromFile(String path) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        StringBuilder board = new StringBuilder();
        try {
            for (int i = 0; i < 10; i++) {
                assert reader != null;
                board.append(reader.readLine());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int k = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                map[i][j] = board.charAt(k);
                k += 1;
            }
        }
    }

    public void generateUnknownMap() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                map[i][j] = '?';
            }
        }
    }

    public void changeChars(char oldChar, char newChar) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (map[i][j] == oldChar) {
                    map[i][j] = newChar;
                }
            }
        }
    }

    public Coordinates shotToField(String coords) {
        return new Coordinates(coords.charAt(0) - 'A', Integer.parseInt(coords.substring(1)) - 1);
    }

    private boolean pointInShipsList(Coordinates c) {
        for (var v : ships) {
            for (var v1 : v) {
                if (v1.x() == c.x() && v1.y() == c.y()) {
                    return true;
                }
            }
        }
        return false;
    }

    public int coordInShipsList(Coordinates c) {
        for (int i = 0; i < 10; i++) {
            for (var v1 : ships.get(i)) {
                if (v1.x() == c.x() && v1.y() == c.y()) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int countShips() {
        int result = 0;
        for (int i = 0; i < 10; i++) {
            result += ships.get(i).size();
        }
        return result;
    }

    private void searchShipsInMap(int x, int y, int k) {
        if (map[x][y] != '#') {
            return;
        }

        if (pointInShipsList(new Coordinates(x, y))) {
            return;
        }

        ships.get(k).add(new Coordinates(x, y));

        if (x - 1 >= 0 && y + 1 < 10) {
            searchShipsInMap(x - 1, y + 1, k);
        }
        if (x - 1 >= 0 && y - 1 >= 0) {
            searchShipsInMap(x - 1, y - 1, k);
        }
        if (x + 1 < 10 && y - 1 >= 0) {
            searchShipsInMap(x + 1, y - 1, k);
        }
        if (x + 1 < 10 && y + 1 < 10) {
            searchShipsInMap(x + 1, y + 1, k);
        }
        if (x - 1 >= 0) {
            searchShipsInMap(x - 1, y, k);
        }
        if (x + 1 < 10) {
            searchShipsInMap(x + 1, y, k);
        }
        if (y - 1 >= 0) {
            searchShipsInMap(x, y - 1, k);
        }
        if (y + 1 < 10) {
            searchShipsInMap(x, y + 1, k);
        }
    }

    public void parser() {
        for (int i = 0; i < 10; i++) {
            this.ships.add(new ArrayList<>());
        }
        int k = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (map[i][j] == '#' && !pointInShipsList(new Coordinates(i, j))) {
                    searchShipsInMap(i, j, k);
                    k += 1;
                }
            }
        }
    }

    public void discoverFields(int x, int y) {
        if (map[x][y] != '#') {
            return;
        }

        if (x - 1 >= 0 && y + 1 < 10) {
            if (map[x - 1][y + 1] == '?') {
                map[x - 1][y + 1] = '.';
            } else if (map[x - 1][y + 1] == '#') {
                map[x - 1][y + 1] = 'p';
            }
            discoverFields(x - 1, y + 1);
        }
        if (x - 1 >= 0 && y - 1 >= 0) {
            if (map[x - 1][y - 1] == '?') {
                map[x - 1][y - 1] = '.';
            } else if (map[x - 1][y - 1] == '#') {
                map[x - 1][y - 1] = 'p';
            }
            discoverFields(x - 1, y - 1);
        }
        if (x + 1 < 10 && y - 1 >= 0) {
            if (map[x + 1][y - 1] == '?') {
                map[x + 1][y - 1] = '.';
            } else if (map[x + 1][y - 1] == '#') {
                map[x + 1][y - 1] = 'p';
            }
            discoverFields(x + 1, y - 1);
        }
        if (x + 1 < 10 && y + 1 < 10) {
            if (map[x + 1][y + 1] == '?') {
                map[x + 1][y + 1] = '.';
            } else if (map[x + 1][y + 1] == '#') {
                map[x + 1][y + 1] = 'p';
            }
            discoverFields(x + 1, y + 1);
        }
        if (x - 1 >= 0) {
            if (map[x - 1][y] == '?') {
                map[x - 1][y] = '.';
            } else if (map[x - 1][y] == '#') {
                map[x - 1][y] = 'p';
            }
            discoverFields(x - 1, y);
        }
        if (x + 1 < 10) {
            if (map[x + 1][y] == '?') {
                map[x + 1][y] = '.';
            } else if (map[x + 1][y] == '#') {
                map[x + 1][y] = 'p';
            }
            discoverFields(x + 1, y);
        }
        if (y - 1 >= 0) {
            if (map[x][y - 1] == '?') {
                map[x][y - 1] = '.';
            } else if (map[x][y - 1] == '#') {
                map[x][y - 1] = 'p';
            }
            discoverFields(x, y - 1);
        }
        if (y + 1 < 10) {
            if (map[x][y + 1] == '?') {
                map[x][y + 1] = '.';
            } else if (map[x][y + 1] == '#') {
                map[x][y + 1] = 'p';
            }
            discoverFields(x, y + 1);
        }

    }

    public void discoverFieldsBySunkenShips() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (map[i][j] == '!') {
                    map[i][j] = '#';
                    discoverFields(i, j);
                }
            }
        }
        changeChars('p', '#');
    }
}
