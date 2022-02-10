package MapGenerator;

import java.util.List;

public interface BattleshipGenerator {

    String generateMap();

    static BattleshipGenerator defaultInstance() {
        return new BattleshipMapCreator();
    }
}

