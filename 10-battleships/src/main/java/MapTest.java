import MapGenerator.BattleshipGenerator;

public class MapTest {

    public static void printRandomMap(String[] args) {
        var x = BattleshipGenerator.defaultInstance().generateMap();

        for (int i = 0; i < 100; i++) {
            if (i % 10 == 0 && i != 0) {
                System.out.println();
            }
            System.out.print(x.charAt(i));
        }
    }
}
