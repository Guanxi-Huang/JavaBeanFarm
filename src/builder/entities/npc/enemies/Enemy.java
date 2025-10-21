package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Npc;
import builder.entities.npc.spawners.SetSpawner;
import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

public abstract class Enemy extends Npc {

    private int spawnX;
    private int spawnY;
    private FixedTimer lifespan;
    private boolean attacking;
    private HasPosition trackedTarget;
    private SetSpawner spawner;

    public Enemy(int x, int y) {
        super(x, y);
        this.spawnX = x;
        this.spawnY = y;
        this.attacking = true;
        this.spawner =
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

    public void setSpawnY(int spawnY) {
        this.spawnY = spawnY;
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

    public abstract SpriteGroup getSpriteGroup();


    public HasPosition getTrackedTarget() {
        return trackedTarget;
    }

    public void setTrackedTarget(HasPosition trackedTarget) {
        this.trackedTarget = trackedTarget;
    }

    public SetSpawner getSpawner() {
        return spawner;
    }

    public void setSpawner(SetSpawner spawner) {
        this.spawner = spawner;
    }

    public void setTargetDirection(double x, double y) {
        double deltaX = x - this.getX();
        double deltaY = y - this.getY();
        this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
    }

    public void updateSprite(int trackedTargetY) {
        if (trackedTargetY > this.getY()) {
            this.setSprite(getSpriteGroup().getSprite("down"));
        } else {
            this.setSprite(getSpriteGroup().getSprite("up"));
        }
    }


    public boolean isClosed(HasPosition target, EngineState engine) {
        return distanceFrom(target) < engine.getDimensions().tileSize();
    }

    public abstract void updateAttack (EngineState engine, GameState game);


    @Override
    public void tick(EngineState engine, GameState game) {
        super.tick(engine, game);
        if (this.getLifespan() != null) {
            this.getLifespan().tick();
            if (this.getLifespan().isFinished()) {
                markForRemoval();
            }
        }
        if (isAttacking()) {
            setTargetDirection(getTrackedTarget().getX(), getTrackedTarget().getY());
            updateSprite(getTrackedTarget().getY());
            updateAttack(engine, game);
        } else {
            setTargetDirection(getSpawnX(), getSpawnY());
            updateSprite(getSpawnY());
            if (isClosed(spawner.spawnPoint(), engine)) {
                markForRemoval();
            }
        }
        this.move();
    }

    @Override
    public void interact(EngineState state, GameState game) {}
}