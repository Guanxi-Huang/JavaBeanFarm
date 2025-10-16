package builder.entities.npc.spawners;

import builder.GameState;

public class MagpieSpawner extends SetSpawner {

    public MagpieSpawner(int x, int y) {
        super(x, y, 360);
    }

    public MagpieSpawner(int x, int y, int duration) {
        super(x, y, duration);
    }

    @Override
    protected void spawn(GameState game) {
        game.getEnemies().Birds.add(game.getEnemies().mkM(game.getPlayer()));
    }

}
