package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Npc;

import engine.EngineState;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

public abstract class Enemy extends Npc {

    private int spawnX;
    private int spawnY;
    private FixedTimer lifespan;
    private boolean attacking;
    private HasPosition trackedTarget;

    public Enemy(int x, int y) {
        super(x, y);
        this.spawnX = x;
        this.spawnY = y;
        this.attacking = true;
    }

    public int getSpawnX() {
        return spawnX;
    }

    public void setSpawnX(int spawnX) {
        this.spawnX = spawnX;
    }

    public int getSpawnY() {
        return spawnY;
    }

    public FixedTimer getLifespan() {
        return lifespan;
    }


    public void setLifespan(FixedTimer timer) {
        this.lifespan = timer;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public HasPosition getTrackedTarget() {
        return trackedTarget;
    }

    public void setTrackedTarget(HasPosition trackedTarget) {
        this.trackedTarget = trackedTarget;
    }

    @Override
    public void tick(EngineState state, GameState game) {
        super.tick(state, game);
    }

    @Override
    public void interact(EngineState state, GameState game) {}
}