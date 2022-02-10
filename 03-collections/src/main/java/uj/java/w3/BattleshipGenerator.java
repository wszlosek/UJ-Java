package uj.java.w3;

public interface BattleshipGenerator {

    String generateMap();

    static BattleshipGenerator defaultInstance() {
        return new BattleshipMapCreator();
    }
}
