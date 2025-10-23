package builder.entities.npc.spawners;

import builder.GameState;

/**
 * The type Eagle spawner.
 */
public class EagleSpawner extends SetSpawner {

    /**
     * Instantiates a new Eagle spawner.
     *
     * @param x the x
     * @param y the y
     */
    public EagleSpawner(int x, int y) {
        super(x, y, 1000);
    }

    /**
     * Instantiates a new Eagle spawner.
     *
     * @param x        the x
     * @param y        the y
     * @param duration the duration
     */
    public EagleSpawner(int x, int y, int duration) {
        super(x, y, duration);
    }

    @Override
    protected void spawn(GameState game) {
        game.getEnemies().setSpawnX(getX());
        game.getEnemies().setSpawnY(getY());
        game.getEnemies().getBirds().add(game.getEnemies().mkE(game.getPlayer()));
    }
}
