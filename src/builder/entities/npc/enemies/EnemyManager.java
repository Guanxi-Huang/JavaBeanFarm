package builder.entities.npc.enemies;

import builder.GameState;
import builder.Tickable;
import builder.entities.Interactable;
import builder.entities.npc.spawners.Spawner;
import builder.player.Player;
import builder.ui.RenderableGroup;

import engine.EngineState;
import engine.game.HasPosition;
import engine.renderer.Dimensions;
import engine.renderer.Renderable;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Enemy manager.
 */
public class EnemyManager implements Tickable, Interactable, RenderableGroup {

    private ArrayList<Spawner> spawners;
    private ArrayList<Enemy> birds;
    private int spawnX;
    private int spawnY;

    /**
     * Instantiates a new Enemy manager.
     *
     * @param dimensions the dimensions
     */
    public EnemyManager(Dimensions dimensions) {
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
     * Gets spawners.
     *
     * @return the spawners
     */
    public ArrayList<Spawner> getSpawners() {
        return this.spawners;
    }

    /**
     * Sets spawners.
     */
    public void setSpawners() {
        this.spawners = new ArrayList<>();
    }

    /**
     * Gets birds.
     *
     * @return the birds
     */
    public ArrayList<Enemy> getBirds() {
        return this.birds;
    }

    /**
     * Sets birds.
     */
    public void setBirds() {
        this.birds = new ArrayList<>();
    }

    /**
     * Cleanup.
     */
    public void cleanup() {
        for (int i = this.birds.size() - 1; i >= 0; i -= 1) {
            if (this.birds.get(i).isMarkedForRemoval()) {
                this.birds.remove(i);
            }
        }
    }

    /**
     * add spawners.
     *
     * @param spawner the current bird spawner
     */
    public void add(Spawner spawner) {
        this.spawners.add(spawner);
    }

    /**
     * Mk m magpie.
     *
     * @param player the player
     * @return the magpie
     */
    public Magpie mkM(Player player) {
        final Magpie magpie = new Magpie(this.spawnX, this.spawnY, player);
        this.birds.add(magpie);
        return magpie;
    }

    /**
     * Mk p pigeon.
     *
     * @param hasPosition the has position
     * @return the pigeon
     */
    public Pigeon mkP(HasPosition hasPosition) {
        final Pigeon pigeon = new Pigeon(this.spawnX, this.spawnY, hasPosition);
        this.birds.add(pigeon);
        return pigeon;
    }

    /**
     * Mk e eagle.
     *
     * @require the player can not be null
     * @param player the player
     * @return the eagle
     */
    public Eagle mkE(Player player) {
        return new Eagle(this.spawnX, this.spawnY, player);
    }

    @Override
    public void tick(EngineState state, GameState game) {
        this.cleanup();
        for (Spawner spawner : this.spawners) {
            spawner.tick(state, game);
        }
        for (Enemy bird : birds) {
            bird.tick(state, game);
        }
    }

    /**
     * Get all {@link Magpie}s positions from the enemy manager.
     *
     * @return all {@link Magpie}s positions from the enemy manager.
     */
    public ArrayList<Magpie> getMagpies() {
        final ArrayList<Magpie> magpies = new ArrayList<>();
        for (Enemy bird : birds) {
            if (bird instanceof Magpie temp) {
                magpies.add(temp);
            }
        }
        return magpies;
    }

    /**
     * Gets a ll.
     *
     * @return the a ll
     */
    public ArrayList<Enemy> getAll() {
        return this.birds;
    }

    /**
     * interact
     *
     * @param state The state of the engine, including the mouse, keyboard information and
     *     dimension. Useful for processing keyboard presses or mouse movement.
     * @param game The state of the game, including the player and world. Can be used to query or
     *     update the game state.
     */
    @Override
    public void interact(EngineState state, GameState game) {
    }

    @Override
    public List<Renderable> render() {
        return new ArrayList<>(this.birds);
    }
}
