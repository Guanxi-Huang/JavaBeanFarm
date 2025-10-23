package builder.entities.npc;

import builder.GameState;
import builder.entities.npc.enemies.Enemy;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

/**
 * A highly trained Guard Bee... don't think about that too much. This is our projectile class,
 * basically a bullet.
 */
public class GuardBee extends Npc {

    private final int spawnX;
    private final int spawnY;
    private static final int SPEED = 2;
    private static final SpriteGroup art = SpriteGallery.bee;
    final FixedTimer lifespan = new FixedTimer(300);
    private final HasPosition trackedTarget;



    /**
     * Instantiates a new GuardBee.
     *
     * @require x >= 0 && y >= 0;
     * @param x horizontal spawning position
     * @param y vertical spawning position
     * @param trackedTarget target with a position we want this to track
     */
    public GuardBee(int x, int y, HasPosition trackedTarget) {
        super(x, y);
        this.setSprite(art.getSprite("default"));
        this.trackedTarget = trackedTarget;

        this.spawnX = x;
        this.spawnY = y;
        this.setSpeed(GuardBee.SPEED);

        setMoveDirection(trackedTarget);
    }

    /**
     * Gets spawn x.
     *
     * @return the spawn x
     */
    public int getSpawnX() {
        return this.spawnX;
    }

    /**
     * Gets spawn y.
     *
     * @return the spawn y
     */
    public int getSpawnY() {
        return this.spawnY;
    }

    /**
     * Gets tracked target.
     *
     * @return the tracked target
     */
    public HasPosition getTrackedTarget() {
        return this.trackedTarget;
    }

    /**
     * Update art based on direction.
     */
    public void updateArtBasedOnDirection() {
        boolean goingUp = (this.getDirection() >= 230 && this.getDirection() < 310);
        boolean goingDown = (this.getDirection() >= 40 && this.getDirection() < 140);
        boolean goingRight = (this.getDirection() >= 310 && this.getDirection() < 40);
        if (goingDown) {
            this.setSprite(art.getSprite("down"));
        } else if (goingUp) {
            this.setSprite(art.getSprite("up"));
        } else if (goingRight) {
            this.setSprite(art.getSprite("right"));
        } else {
            this.setSprite(art.getSprite("left"));
        }
    }

    @Override
    public void tick(EngineState state, GameState game) {
        super.tick(state);
        this.move();

        if (this.trackedTarget == null) {
            double deltaX = this.spawnX - this.getX();
            double deltaY = this.spawnY - this.getY();
            this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
            return;
        }
        for (Enemy enemy : game.getEnemies().getBirds()) {
            if (this.distanceFrom(enemy) < 300) {
                // if a magpie is close enough to a bee it will lock onto it
                setMoveDirection(enemy);
                break;
            }
        }
        for (Enemy enemy : game.getEnemies().getAll()) {
            if (this.distanceFrom(enemy) < state.getDimensions().tileSize()) {
                enemy.markForRemoval();
                this.markForRemoval();
            }
        }

        this.updateArtBasedOnDirection();
        lifespan.tick();
        if (lifespan.isFinished()) {
            this.markForRemoval();
        }
    }

    public void setMoveDirection(HasPosition target) {
        double deltaX = target.getX() - this.getX();
        double deltaY = target.getY() - this.getY();
        this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
    }
}
