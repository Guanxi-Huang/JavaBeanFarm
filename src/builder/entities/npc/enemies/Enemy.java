package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Npc;

import engine.EngineState;
import engine.timing.FixedTimer;

public abstract class Enemy extends Npc {

    private FixedTimer lifespan;

    public Enemy(int x, int y) {
        super(x, y);
    }

    public FixedTimer getLifespan() {
        return lifespan;
    }


    public void setLifespan(FixedTimer timer) {
        this.lifespan = timer;
    }

    @Override
    public void tick(EngineState state, GameState game) {
        super.tick(state, game);
    }

    @Override
    public void interact(EngineState state, GameState game) {}
}