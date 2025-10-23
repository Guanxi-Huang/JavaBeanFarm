package builder.entities.npc.spawners;

import builder.GameState;
import engine.EngineState;
import engine.game.HasPosition;
import engine.timing.RepeatingTimer;
import engine.timing.TickTimer;

/**
 * The type Set spawner.
 */
public abstract class SetSpawner implements Spawner {
    private int x;
    private int y;
    private TickTimer timer;

    /**
     * Instantiates a new Set spawner.
     *
     * @param x the x
     * @param y the y
     */
    public SetSpawner(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Instantiates a new Set spawner.
     *
     * @param x        the x
     * @param y        the y
     * @param duration the duration
     */
    public SetSpawner(int x, int y, int duration) {
        this.x = x;
        this.y = y;
        this.timer = new RepeatingTimer(duration);
    }

    @Override
    public TickTimer getTimer() {
        return this.timer;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void tick(EngineState state, GameState game) {
        this.timer.tick();
        if (this.getTimer().isFinished()) {
            game.getEnemies().setSpawnX(this.getX());
            game.getEnemies().setSpawnY(this.getY());
            spawn(game);
        }
    }

    protected abstract void spawn(GameState game);

    public HasPosition spawnPoint() {
        return new SetSpawner(x, y) {
            @Override
            protected void spawn(GameState game){}
        };
    }
}

