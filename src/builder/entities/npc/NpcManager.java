package builder.entities.npc;

import builder.GameState;
import builder.Tickable;
import builder.entities.Interactable;
import builder.ui.RenderableGroup;

import engine.EngineState;
import engine.renderer.Renderable;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of NPCs.
 */
public class NpcManager implements Interactable, Tickable, RenderableGroup {

    private final ArrayList<Npc> npcs = new ArrayList<>();

    /**
     * Instantiates a new Npc manager.
     */
    public NpcManager() {}

    /**
     * Gets npcs.
     *
     * @return the npcs
     */
    public ArrayList<Npc> getNpcs() {
        return npcs;
    }

    /**
     * Cleanup.
     */
    public void cleanup() {
        for (int i = this.getNpcs().size() - 1; i >= 0; i -= 1) {
            if (this.npcs.get(i).isMarkedForRemoval()) {
                this.npcs.remove(i);
            }
        }
    }

    /**
     * Add an NPC to this manager for tracking and management.
     *
     * @param npc npc to add to the manager for it to well manage/track.
     */
    public void addNpc(Npc npc) {
        this.getNpcs().add(npc);
    }

    @Override
    public void tick(EngineState state, GameState game) {
        this.cleanup();
        for (Npc npc : getNpcs()) {
            npc.tick(state, game);
        }
    }

    @Override
    public void interact(EngineState state, GameState game) {
        for (Interactable interactable : this.getInteractables()) {
            interactable.interact(state, game);
        }
    }

    /**
     * get interactable.
     *
     * @return an ArrayList of interactable
     */
    private ArrayList<Interactable> getInteractables() {
        final ArrayList<Interactable> interactables = new ArrayList<>();
        for (Npc npc : getNpcs()) {
            if (npc != null) {
                interactables.add(npc);
            }
        }
        return interactables;
    }

    @Override
    public List<Renderable> render() {
        return new ArrayList<>(this.getNpcs());
    }
}
