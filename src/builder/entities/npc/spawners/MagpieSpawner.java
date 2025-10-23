package builder.entities.npc.spawners;

import builder.GameState;

/**
 * The type Magpie spawner.
 */
public class MagpieSpawner extends SetSpawner {

    /**
     * Instantiates a new Magpie spawner.
     *
     * @param x the x
     * @param y the y
     */
    public MagpieSpawner(int x, int y) {
        super(x, y, 360);
    }

    /**
     * Instantiates a new Magpie spawner.
     *
     * @param x        the x
     * @param y        the y
     * @param duration the duration
     */
    public MagpieSpawner(int x, int y, int duration) {
        super(x, y, duration);
    }

    @Override
    protected void spawn(GameState game) {
        game.getEnemies().getBirds().add(game.getEnemies().mkM(game.getPlayer()));
    }

}
