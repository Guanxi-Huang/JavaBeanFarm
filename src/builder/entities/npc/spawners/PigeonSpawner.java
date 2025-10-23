package builder.entities.npc.spawners;

import builder.GameState;
import builder.entities.resources.Cabbage;
import builder.entities.tiles.Tile;

import engine.EngineState;
import engine.game.Entity;
import engine.game.HasPosition;

import java.util.List;

public class PigeonSpawner extends SetSpawner {

    public PigeonSpawner(int x, int y) {
        super(x, y, 100);
    }

    public PigeonSpawner(int x, int y, int duration) {
        super(x, y, duration);
    }

    @Override
    public void tick(EngineState state, GameState game) {
        getTimer().tick();

        List<Tile> tiles = game.getWorld().tileSelector(
                                tile -> {
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
                }
            }

            if (this.getTimer().isFinished()) {
                game.getEnemies().setSpawnX(this.getX());
                game.getEnemies().setSpawnY(this.getY());
                game.getEnemies().getBirds().add(game.getEnemies().mkP(closest));
            }
        }
    }

    @Override
    protected void spawn(GameState game) {
    }

    /**
     * Return how far away this npc is from the given position
     *
     * @param position the position we are measuring to from this npcs position!
     * @return integer representation for how far apart they are
     */
    public int distanceFrom(HasPosition position) {
        int deltaX = position.getX() - this.getX();
        int deltaY = position.getY() - this.getY();
        return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}

