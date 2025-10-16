package builder.entities.npc.spawners;

import builder.GameState;

public class EagleSpawner extends SetSpawner {

    public EagleSpawner(int x, int y) {
        super(x, y, 1000);
    }

    public EagleSpawner(int x, int y, int duration) {
        super(x, y, duration);
    }

    @Override
    protected void spawn(GameState game) {
        game.getEnemies().Birds.add(game.getEnemies().mkE(game.getPlayer()));
    }
}
