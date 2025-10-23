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
    private int positionX;
    private int positionY;
    private TickTimer timer;

    /**
     * Instantiates a new Set spawner.
     *
     * @param x the x
     * @param y the y
     */
    public SetSpawner(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }

    /**
     * Instantiates a new Set spawner.
     *
     * @param x        the x
     * @param y        the y
     * @param duration the duration
     */
    public SetSpawner(int x, int y, int duration) {
        this.positionX = x;
        this.positionY = y;
        this.timer = new RepeatingTimer(duration);
    }

    @Override
    public TickTimer getTimer() {
        return this.timer;
    }

    @Override
    public int getX() {
        return this.positionX;
    }

    @Override
    public void setX(int x) {
        this.positionX = x;
    }

    @Override
    public int getY() {
        return this.positionY;
    }

    @Override
    public void setY(int y) {
        this.positionY = y;
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

    /**
     * Spawn point has position.
     *
     * @return the has position
     */
    public HasPosition spawnPoint() {
        return new SetSpawner(positionX, positionY) {
            @Override
            protected void spawn(GameState game){}
        };
    }
}

