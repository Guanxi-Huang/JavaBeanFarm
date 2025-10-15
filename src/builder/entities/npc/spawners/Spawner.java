package builder.entities.npc.spawners;

import builder.GameState;
import builder.Tickable;

import engine.EngineState;
import engine.game.HasPosition;
import engine.timing.TickTimer;

/**
 * A spawner is responsible for spawning specific types of {@link builder.entities.npc.Npc}s or
 * {@link builder.entities.npc.enemies.Enemy}s
 */
public interface Spawner extends HasPosition, Tickable {

    TickTimer getTimer();

    @Override
    void tick(EngineState state, GameState game);

    @Override
    int getX();

    @Override
    void setX(int x);

    @Override
    int getY();

    @Override
    void setY(int y);
}
