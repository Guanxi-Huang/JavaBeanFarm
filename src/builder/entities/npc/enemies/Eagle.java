package builder.entities.npc.enemies;

import builder.GameState;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

public class Eagle extends Enemy {

    private static final SpriteGroup art = SpriteGallery.eagle;
    private int food = 0;

    public Eagle(int x, int y, HasPosition trackedTarget) {
        super(x, y);

        // derive direction based on where the eagle is and the initial target is
        int direction = 20;
        this.setDirection(direction);
        this.setSpeed(2);
        setTrackedTarget(trackedTarget);
        setLifespan(new FixedTimer(5000));
        setTargetDirection(getTrackedTarget().getX(), getTrackedTarget().getY());
    }

    @Override
    public SpriteGroup getSpriteGroup() {
        return art;
    }

    @Override
    public void tick(EngineState engine, GameState game) {
        super.tick(engine);
        getLifespan().tick();
        if (getLifespan().isFinished()) {
            this.markForRemoval();
        }
        if ((this.distanceFrom(game.getPlayer().getX(), game.getPlayer().getY())
                        < engine.getDimensions().tileSize())
                && isAttacking()) {
            setAttacking(false);
            //      if (game.getInventory().getFood() > 0) {
            if (this.food == 0) {
                game.getInventory().addFood(-3);
                this.food = 3;
            }
            this.setSpeed(4); // the eagle BOOKS it once it has the food
            //      }
        }
        if ((this.distanceFrom(getSpawnX(), getSpawnY()) < engine.getDimensions().tileSize())
                && !isAttacking()) {
            this.markForRemoval();
        }
        this.move();

        updateAttacking();

        if (this.isMarkedForRemoval()
                && this.distanceFrom(getSpawnX(), getSpawnY())
                        > engine.getDimensions().tileSize()) {
            game.getInventory().addFood(this.food);
        }
    }
}
