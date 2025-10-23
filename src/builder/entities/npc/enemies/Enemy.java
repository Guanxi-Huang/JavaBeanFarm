package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Npc;
import builder.entities.npc.spawners.SetSpawner;
import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

/**
 * The type Enemy.
 */
public abstract class Enemy extends Npc {

    private int spawnX;
    private int spawnY;
    private FixedTimer lifespan;
    private boolean attacking;
    private HasPosition trackedTarget;
    private SetSpawner spawner;

    /**
     * Instantiates a new Enemy.
     *
     * @param x the x
     * @param y the y
     */
    public Enemy(int x, int y) {
        super(x, y);
        this.spawnX = x;
        this.spawnY = y;
        this.attacking = true;
        this.spawner = new SetSpawner(x, y) {
            @Override protected void spawn(GameState game) {}
        };
    }

    /**
     * Gets spawn x.
     *
     * @return the spawn x
     */
    public int getSpawnX() {
        return spawnX;
    }

    /**
     * Sets spawn x.
     *
     * @param spawnX the spawn x
     */
    public void setSpawnX(int spawnX) {
        this.spawnX = spawnX;
    }

    /**
     * Gets spawn y.
     *
     * @return the spawn y
     */
    public int getSpawnY() {
        return spawnY;
    }

    /**
     * Sets spawn y.
     *
     * @param spawnY the spawn y
     */
    public void setSpawnY(int spawnY) {
        this.spawnY = spawnY;
    }

    /**
     * Gets lifespan.
     *
     * @return the lifespan
     */
    public FixedTimer getLifespan() {
        return lifespan;
    }


    /**
     * Sets lifespan.
     *
     * @param timer the timer
     */
    public void setLifespan(FixedTimer timer) {
        this.lifespan = timer;
    }

    /**
     * Is attacking boolean.
     *
     * @return the boolean
     */
    public boolean isAttacking() {
        return attacking;
    }

    /**
     * Sets attacking.
     *
     * @param attacking the attacking
     */
    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    /**
     * Gets sprite group.
     *
     * @return the sprite group
     */
    public abstract SpriteGroup getSpriteGroup();


    /**
     * Gets tracked target.
     *
     * @return the tracked target
     */
    public HasPosition getTrackedTarget() {
        return trackedTarget;
    }

    /**
     * Sets tracked target.
     *
     * @param trackedTarget the tracked target
     */
    public void setTrackedTarget(HasPosition trackedTarget) {
        this.trackedTarget = trackedTarget;
    }

    /**
     * Gets spawner.
     *
     * @return the spawner
     */
    public SetSpawner getSpawner() {
        return spawner;
    }

    /**
     * Sets spawner.
     *
     * @param spawner the spawner
     */
    public void setSpawner(SetSpawner spawner) {
        this.spawner = spawner;
    }

    /**
     * Sets target direction.
     *
     * @param x the x
     * @param y the y
     */
    public void setTargetDirection(double x, double y) {
        double deltaX = x - this.getX();
        double deltaY = y - this.getY();
        this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
    }

    /**
     * Update sprite.
     *
     * @param trackedTargetY the tracked target y
     */
    public void updateSprite(int trackedTargetY) {
        if (getSpriteGroup() == null) {
            return;
        }
        if (trackedTargetY > this.getY()) {
            this.setSprite(getSpriteGroup().getSprite("down"));
        } else {
            this.setSprite(getSpriteGroup().getSprite("up"));
        }
    }


    /**
     * Is closed boolean.
     *
     * @param target the target
     * @param engine the engine
     * @return the boolean
     */
    public boolean isClosed(HasPosition target, EngineState engine) {
        if (engine == null || target == null) {
            return false;
        }
        final var dims = engine.getDimensions();
        if (dims == null) {
            return false;
        }

        return distanceFrom(target) < engine.getDimensions().tileSize();
    }

    /**
     * Update attack.
     *
     * @param engine the engine
     * @param game   the game
     */
    public abstract void updateAttack(EngineState engine, GameState game);


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


}