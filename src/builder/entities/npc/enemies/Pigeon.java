package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.resources.Cabbage;
import builder.entities.tiles.Tile;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.Entity;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

import java.util.List;

public class Pigeon extends Enemy{

    private static final SpriteGroup art = SpriteGallery.pigeon;

    public Pigeon(int x, int y) {
        super(x, y);
        setSprite(art.getSprite("down"));
        setLifespan(new FixedTimer(3000));
    }

    public Pigeon(int x, int y, HasPosition trackedTarget) {
        super(x, y);
        this.setSpeed(1);
        setTrackedTarget(trackedTarget);
        setSprite(art.getSprite("down"));
        setLifespan(new FixedTimer(3000));
    }

    @Override
    public SpriteGroup getSpriteGroup() {
        return art;
    }

    @Override
    public void tick(EngineState engine, GameState game) {
        super.tick(engine, game);

        this.getLifespan().tick();
        if (this.getLifespan().isFinished()) {
            this.markForRemoval();
        }

        if (!isAttacking() &&
            this.distanceFrom(getSpawnX(), getSpawnY()) < engine.getDimensions().tileSize()) { // get close to spawn
            this.markForRemoval();
        }

        if (isAttacking()) {
            if (getTrackedTarget() == null) {
                setTargetDirection((double) engine.getDimensions().windowSize() / 2,
                        (double) engine.getDimensions().windowSize() / 2);
                updateSprite(getTrackedTarget().getY());
            } else {
                setTargetDirection(getTrackedTarget().getX(), getTrackedTarget().getY());
            }
        } else {
            setTargetDirection(getSpawnX(), getSpawnY());
            updateSprite(getSpawnY());
        }
        this.move();

        List<Tile> tiles = game.getWorld().tileSelector(tile -> {
                                    for (Entity entity : tile.getStackedEntities()) {
                                        if (entity instanceof Cabbage) {
                                            return true;
                                        }
                                    }
                                    return false;
                                });
        if (!tiles.isEmpty()) {
            int distance = this.distanceFrom(tiles.getFirst());
            Tile closest = tiles.getFirst();
            for (Tile tile : tiles) {
                if (this.distanceFrom(tile) < distance) {
                    closest = tile;
                } else {
                    // do nothing
                }
            }
            setTrackedTarget(closest);

            if (isAttacking() && this.distanceFrom(getTrackedTarget()) < engine.getDimensions().tileSize()) {
                for (Entity entity : closest.getStackedEntities()) {
                    if (entity instanceof Cabbage cabbage) {
                        cabbage.markForRemoval();
                        setAttacking(false);
                    } else {
                        // do nothing
                    }
                }
            }
        } else { // no cabbages to get
            setAttacking(false);
        }
    }
}
