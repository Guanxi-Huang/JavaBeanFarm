package builder;

import builder.entities.npc.BeeHive;
import builder.entities.npc.GuardBee;
import builder.entities.npc.NpcManager;
import builder.entities.npc.Scarecrow;
import builder.entities.npc.enemies.Eagle;
import builder.entities.npc.enemies.EnemyManager;
import builder.entities.npc.enemies.Magpie;
import builder.entities.npc.enemies.Pigeon;
import builder.entities.npc.spawners.EagleSpawner;
import builder.entities.npc.spawners.MagpieSpawner;
import builder.entities.npc.spawners.PigeonSpawner;
import builder.entities.resources.Cabbage;
import builder.entities.tiles.Dirt;
import builder.entities.tiles.Grass;
import builder.entities.tiles.OreVein;
import builder.entities.tiles.Water;
import builder.inventory.TinyInventory;
import builder.inventory.items.Bucket;
import builder.inventory.items.HiveHammer;
import builder.inventory.items.Hoe;
import builder.inventory.items.Item;
import builder.inventory.items.Jackhammer;
import builder.inventory.items.Pole;
import builder.inventory.ui.InventoryOverlay;
import builder.player.PlayerManager;
import builder.ui.SpriteGallery;
import builder.world.BeanWorld;
import builder.world.WorldBuilder;
import engine.renderer.Dimensions;
import engine.renderer.TileGrid;
import org.junit.Before;
import org.junit.Test;
import scenarios.mocks.MockEngineState;
import scenarios.mocks.MockKeys;
import scenarios.mocks.MockMouse;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AllFeaturesTest {
    private final Dimensions dimensions = new TileGrid(25, 800);
    private BeanWorld world;
    private PlayerManager player;
    private TinyInventory inventory;
    private EnemyManager enemies;
    private NpcManager npcs;
    private GameState game;
    private MockEngineState idle;
    private InventoryOverlay overlay;

    @Before
    public void setUp() {
        world = WorldBuilder.empty();
        player = new PlayerManager(400, 400);
        inventory = new TinyInventory(5, 20, 30);
        enemies = new EnemyManager(dimensions);
        npcs = new NpcManager();
        game = new JavaBeanGameState(world, player.getPlayer(), inventory, npcs, enemies);
        idle = new MockEngineState(dimensions);
        overlay = new InventoryOverlay(dimensions, 5);
        Item[] tools = {new Bucket(), new Hoe(), new Jackhammer(),
                new HiveHammer(), new Pole()};
        for (int index = 0; index < tools.length; index++) {
            inventory.setItem(index, tools[index]);
        }
    }

    private void select(char key) {
        overlay.tick(idle.press(key), game);
    }

    private void move(char key, int x, int y) {
        player.tick(idle.press(key), game);
        assertEquals(x, player.getPlayer().getX());
        assertEquals(y, player.getPlayer().getY());
    }

    private Dirt planted(int x, int y) {
        Dirt dirt = new Dirt(x, y);
        dirt.till();
        dirt.plant(inventory);
        world.place(dirt);
        return dirt;
    }

    @Test
    public void wMovesUpOnePixel() {
        move('w', 400, 399);
    }

    @Test
    public void sMovesDownOnePixel() {
        move('s', 400, 401);
    }

    @Test
    public void aMovesLeftOnePixel() {
        move('a', 399, 400);
    }

    @Test
    public void dMovesRightOnePixel() {
        move('d', 401, 400);
    }

    @Test
    public void simultaneousMovementUsesDocumentedPriorityWithoutDiagonalMovement() {
        MockEngineState state = new MockEngineState(dimensions,
                new MockMouse(700, 700, false, false, false),
                new MockKeys(List.of('d', 'a', 's', 'w')));
        player.tick(state, game);
        assertEquals(400, player.getPlayer().getX());
        assertEquals(399, player.getPlayer().getY());
    }

    @Test
    public void waterBlocksMovementAcrossTileBoundary() {
        player = new PlayerManager(415, 400);
        game = new JavaBeanGameState(world, player.getPlayer(), inventory, npcs, enemies);
        world.place(new Water(416, 384));
        player.tick(idle.press('d'), game);
        assertEquals(415, player.getPlayer().getX());
        assertEquals(400, player.getPlayer().getY());
    }

    @Test
    public void numberKeysSelectAllFiveToolsAndHighlightSelectedSlot() {
        Class<?>[] types = {Bucket.class, Hoe.class, Jackhammer.class,
                HiveHammer.class, Pole.class};
        for (int index = 0; index < types.length; index++) {
            select((char) ('1' + index));
            assertEquals(index, inventory.getActiveSlot());
            assertEquals(types[index], inventory.getHolding().getClass());
            for (int slot = 0; slot < 5; slot++) {
                assertSame(SpriteGallery.inventory.getSprite(slot == index
                                ? "activeborder" : "border"),
                        overlay.render().get(slot).getSprite());
            }
        }
    }

    @Test
    public void bucketPlantsUnderPlayerInsteadOfMouseAndChargesTwoCoins() {
        Dirt dirt = new Dirt(384, 384);
        dirt.till();
        world.place(dirt);
        select('1');
        MockEngineState state = new MockEngineState(dimensions,
                new MockMouse(700, 700, true, false, false), new MockKeys(List.of()));
        player.tick(state, game);
        assertEquals(18, inventory.getCoins());
        assertEquals(1, dirt.getStackedEntities().size());
        assertTrue(dirt.getStackedEntities().getFirst() instanceof Cabbage);
        assertEquals(384, dirt.getStackedEntities().getFirst().getX());
    }

    @Test
    public void rightClickDoesNotUseTool() {
        Dirt dirt = new Dirt(384, 384);
        dirt.till();
        world.place(dirt);
        select('1');
        player.tick(new MockEngineState(dimensions,
                new MockMouse(400, 400, false, true, false), new MockKeys(List.of())), game);
        assertTrue(dirt.getStackedEntities().isEmpty());
        assertEquals(20, inventory.getCoins());
    }

    @Test
    public void holdingLeftDoesNotPlantDuplicateCabbages() {
        Dirt dirt = new Dirt(384, 384);
        dirt.till();
        world.place(dirt);
        select('1');
        for (int tick = 0; tick < 10; tick++) {
            player.tick(idle.leftClick().withFrame(tick), game);
        }
        assertEquals(1, dirt.getStackedEntities().size());
        assertEquals(18, inventory.getCoins());
    }

    @Test
    public void plantingRequiresTilledSoilAndEnoughCoins() {
        Dirt dirt = new Dirt(384, 384);
        world.place(dirt);
        select('1');
        player.tick(idle.leftClick(), game);
        assertTrue(dirt.getStackedEntities().isEmpty());
        dirt.till();
        inventory.addCoins(-19);
        player.tick(idle.leftClick(), game);
        assertTrue(dirt.getStackedEntities().isEmpty());
        assertEquals(1, inventory.getCoins());
    }

    @Test
    public void hoeTurnsGrassIntoDirt() {
        Grass grass = new Grass(384, 384);
        world.place(grass);
        select('2');
        player.tick(idle.leftClick(), game);
        assertTrue(grass.isMarkedForRemoval());
        assertTrue(world.allTiles().stream().anyMatch(tile -> tile instanceof Dirt
                && tile.getX() == 384 && tile.getY() == 384));
        assertEquals(20, inventory.getCoins());
    }

    @Test
    public void hoeTillsDirtWithoutResourceCost() {
        Dirt dirt = new Dirt(384, 384);
        world.place(dirt);
        select('2');
        player.tick(idle.leftClick(), game);
        assertTrue(dirt.isTilled());
        assertEquals(20, inventory.getCoins());
        assertEquals(30, inventory.getFood());
    }

    @Test
    public void jackhammerMinesOnlyEveryFiveTicksAndStopsAfterTenCoins() {
        OreVein ore = new OreVein(384, 384);
        world.place(ore);
        select('3');
        player.tick(idle.leftClick().withFrame(1), game);
        assertEquals(20, inventory.getCoins());
        player.tick(idle.leftClick().withFrame(5), game);
        assertEquals(22, inventory.getCoins());
        for (int tick = 6; tick <= 50; tick++) {
            player.tick(idle.leftClick().withFrame(tick), game);
        }
        assertEquals(30, inventory.getCoins());
        ore.tick(idle);
        assertSame(SpriteGallery.rock.getSprite("depleted"), ore.getOre().getSprite());
    }

    @Test
    public void otherToolsCannotMineOre() {
        world.place(new OreVein(384, 384));
        select('1');
        player.tick(idle.leftClick().withFrame(5), game);
        assertEquals(20, inventory.getCoins());
    }

    @Test
    public void cabbageAdvancesThroughEachGrowthSpriteEveryHundredTicks() {
        Cabbage cabbage = new Cabbage(384, 384);
        String[] stages = {"default", "budding", "growing", "grown", "collectable"};
        assertSame(SpriteGallery.cabbage.getSprite(stages[0]), cabbage.getSprite());
        for (int stage = 1; stage < stages.length; stage++) {
            for (int tick = 0; tick < 99; tick++) {
                cabbage.tick(idle);
            }
            assertSame(SpriteGallery.cabbage.getSprite(stages[stage - 1]), cabbage.getSprite());
            cabbage.tick(idle);
            assertSame(SpriteGallery.cabbage.getSprite(stages[stage]), cabbage.getSprite());
        }
    }

    @Test
    public void walkingOntoMatureCabbageHarvestsAndAllowsReplanting() {
        Dirt dirt = planted(384, 384);
        for (int tick = 0; tick < 400; tick++) {
            world.tick(idle.withFrame(tick), game);
        }
        player.tick(idle, game);
        assertEquals(21, inventory.getCoins());
        assertEquals(32, inventory.getFood());
        assertTrue(dirt.getStackedEntities().getFirst().isMarkedForRemoval());
        world.tick(idle, game);
        assertTrue(dirt.getStackedEntities().isEmpty());
        select('1');
        player.tick(idle.leftClick(), game);
        assertEquals(1, dirt.getStackedEntities().size());
        assertEquals(19, inventory.getCoins());
    }

    @Test
    public void immatureCabbageCannotBeHarvested() {
        Dirt dirt = planted(384, 384);
        player.tick(idle, game);
        assertFalse(dirt.getStackedEntities().getFirst().isMarkedForRemoval());
        assertEquals(18, inventory.getCoins());
        assertEquals(30, inventory.getFood());
    }

    @Test
    public void hiveHammerPlacesOneHiveAndChargesBothResources() {
        Grass grass = new Grass(384, 384);
        world.place(grass);
        select('4');
        player.tick(idle.leftClick(), game);
        player.tick(idle.leftClick(), game);
        assertEquals(1, npcs.getNpcs().size());
        assertTrue(npcs.getNpcs().getFirst() instanceof BeeHive);
        assertEquals(18, inventory.getCoins());
        assertEquals(28, inventory.getFood());
        assertEquals(1, grass.getStackedEntities().size());
    }

    @Test
    public void hiveCannotBePlacedWithoutEnoughFoodOrOnDirt() {
        Grass grass = new Grass(384, 384);
        world.place(grass);
        inventory.addFood(-29);
        select('4');
        player.tick(idle.leftClick(), game);
        assertTrue(npcs.getNpcs().isEmpty());
        assertEquals(20, inventory.getCoins());
        Dirt dirt = new Dirt(448, 384);
        inventory.addFood(10);
        dirt.use(idle.leftClick(), game);
        assertTrue(dirt.getStackedEntities().isEmpty());
        assertEquals(11, inventory.getFood());
    }

    @Test
    public void polePlacesOneScarecrowOnTilledDirtForTwoCoins() {
        Dirt dirt = new Dirt(384, 384);
        world.place(dirt);
        select('5');
        player.tick(idle.leftClick(), game);
        assertTrue(npcs.getNpcs().isEmpty());
        dirt.till();
        player.tick(idle.leftClick(), game);
        player.tick(idle.leftClick(), game);
        assertEquals(1, npcs.getNpcs().size());
        assertTrue(npcs.getNpcs().getFirst() instanceof Scarecrow);
        assertEquals(18, inventory.getCoins());
    }

    @Test
    public void scarecrowRepelsOnlyPigeonsAndMagpiesWithinFourTiles() {
        Scarecrow scarecrow = new Scarecrow(400, 400);
        Magpie magpie = new Magpie(450, 400, game.getPlayer());
        Pigeon pigeon = new Pigeon(400, 450, game.getPlayer());
        Eagle eagle = new Eagle(450, 450, game.getPlayer());
        Magpie outside = new Magpie(529, 400, game.getPlayer());
        enemies.getBirds().addAll(List.of(magpie, pigeon, eagle, outside));
        scarecrow.interact(idle, game);
        assertFalse(magpie.isAttacking());
        assertFalse(pigeon.isAttacking());
        assertTrue(eagle.isAttacking());
        assertTrue(outside.isAttacking());
    }

    @Test
    public void loadedHiveFiresOnlyOneBeeWithinDetectionRange() {
        BeeHive hive = new BeeHive(400, 400);
        Eagle outside = new Eagle(751, 400, game.getPlayer());
        enemies.getBirds().add(outside);
        assertNull(hive.checkAndSpawnBee(enemies.getBirds()));
        enemies.getBirds().add(new Eagle(749, 400, game.getPlayer()));
        assertTrue(hive.checkAndSpawnBee(enemies.getBirds()) instanceof GuardBee);
        assertNull(hive.checkAndSpawnBee(enemies.getBirds()));
    }

    private void verifyHiveReload(int requiredTicks, boolean standingOnHive) {
        BeeHive hive = new BeeHive(384, 384);
        if (!standingOnHive) {
            player = new PlayerManager(700, 700);
            game = new JavaBeanGameState(world, player.getPlayer(), inventory, npcs, enemies);
        }
        enemies.getBirds().add(new Eagle(450, 400, game.getPlayer()));
        assertNotNull(hive.checkAndSpawnBee(enemies.getBirds()));
        enemies.getBirds().clear();
        for (int tick = 1; tick <= requiredTicks; tick++) {
            hive.tick(idle.withFrame(tick), game);
            hive.interact(idle.withFrame(tick), game);
        }
        enemies.getBirds().add(new Eagle(450, 400, game.getPlayer()));
        assertNotNull(hive.checkAndSpawnBee(enemies.getBirds()));
    }

    @Test
    public void hiveReloadsAfterTwoHundredFortyTicksWithoutPlayer() {
        verifyHiveReload(240, false);
    }

    @Test
    public void hiveDoesNotReloadEarlyWithoutPlayer() {
        BeeHive hive = new BeeHive(384, 384);
        player = new PlayerManager(700, 700);
        game = new JavaBeanGameState(world, player.getPlayer(), inventory, npcs, enemies);
        enemies.getBirds().add(new Eagle(450, 400, game.getPlayer()));
        assertNotNull(hive.checkAndSpawnBee(enemies.getBirds()));
        enemies.getBirds().clear();
        for (int tick = 1; tick < 240; tick++) {
            hive.tick(idle.withFrame(tick), game);
            hive.interact(idle.withFrame(tick), game);
        }
        enemies.getBirds().add(new Eagle(450, 400, game.getPlayer()));
        assertNull(hive.checkAndSpawnBee(enemies.getBirds()));
    }

    @Test
    public void standingOnHiveReloadsItWithinEightyTicks() {
        verifyHiveReload(80, true);
    }

    @Test
    public void guardBeeTargetsNearestBirdInsteadOfFirstBird() {
        Eagle farther = new Eagle(200, 400, game.getPlayer());
        Eagle nearer = new Eagle(500, 400, game.getPlayer());
        enemies.getBirds().addAll(List.of(farther, nearer));
        GuardBee bee = new GuardBee(400, 400, farther);
        bee.tick(idle, game);
        assertEquals(0, bee.getDirection());
    }

    @Test
    public void guardBeeReturnsTowardsSpawnWhenBirdsDisappear() {
        Eagle target = new Eagle(700, 400, game.getPlayer());
        GuardBee bee = new GuardBee(100, 400, target);
        bee.setX(400);
        bee.tick(idle, game);
        bee.tick(idle, game);
        assertTrue("Bee should move back towards x=100", bee.getX() < 400);
    }

    @Test
    public void guardBeeContactRemovesBothBeeAndBird() {
        Eagle target = new Eagle(410, 400, game.getPlayer());
        enemies.getBirds().add(target);
        GuardBee bee = new GuardBee(400, 400, target);
        bee.tick(idle, game);
        assertTrue(target.isMarkedForRemoval());
        assertTrue(bee.isMarkedForRemoval());
    }

    @Test
    public void eagleSpawnerWaitsForIntervalAndRegistersEachBirdOnce() {
        EagleSpawner spawner = new EagleSpawner(600, 100, 3);
        spawner.tick(idle, game);
        spawner.tick(idle, game);
        assertTrue(enemies.getBirds().isEmpty());
        spawner.tick(idle, game);
        assertEquals(1, enemies.getBirds().size());
        assertEquals(600, enemies.getBirds().getFirst().getX());
        assertEquals(100, enemies.getBirds().getFirst().getY());
    }

    @Test
    public void magpieSpawnerRegistersEachBirdOnce() {
        new MagpieSpawner(600, 100, 1).tick(idle, game);
        assertEquals(1, enemies.getBirds().size());
    }

    @Test
    public void pigeonSpawnerRegistersEachBirdOnceWhenCabbageExists() {
        planted(384, 384);
        new PigeonSpawner(600, 100, 1).tick(idle, game);
        assertEquals(1, enemies.getBirds().size());
    }

    @Test
    public void pigeonSpawnerDoesNotSpawnWithoutCabbages() {
        new PigeonSpawner(600, 100, 1).tick(idle, game);
        assertTrue(enemies.getBirds().isEmpty());
    }

    @Test
    public void pigeonStealsCabbageAndReturnsToSpawn() {
        Dirt dirt = planted(384, 384);
        Pigeon pigeon = new Pigeon(100, 100, dirt);
        pigeon.setX(384);
        pigeon.setY(384);
        pigeon.updateAttack(idle, game);
        assertTrue(dirt.getStackedEntities().getFirst().isMarkedForRemoval());
        assertFalse(pigeon.isAttacking());
        for (int tick = 0; tick < 400 && !pigeon.isMarkedForRemoval(); tick++) {
            pigeon.tick(idle.withFrame(tick), game);
        }
        assertTrue(pigeon.isMarkedForRemoval());
        assertTrue(pigeon.distanceFrom(100, 100) < dimensions.tileSize() + 4);
    }

    @Test
    public void pigeonWithoutCabbagesReturnsInsteadOfAttacking() {
        Pigeon pigeon = new Pigeon(100, 100, new Dirt(600, 600));
        pigeon.setX(400);
        pigeon.setY(400);
        pigeon.tick(idle, game);
        assertFalse(pigeon.isAttacking());
        pigeon.tick(idle, game);
        assertTrue(pigeon.getDirection() < 0 || pigeon.getDirection() > 180);
    }

    @Test
    public void eagleStealsThreeFoodThenReturnsAndDisappears() {
        Eagle eagle = new Eagle(100, 100, game.getPlayer());
        eagle.setX(400);
        eagle.setY(400);
        eagle.updateAttack(idle, game);
        assertEquals(27, inventory.getFood());
        assertFalse(eagle.isAttacking());
        for (int tick = 0; tick < 400 && !eagle.isMarkedForRemoval(); tick++) {
            eagle.tick(idle.withFrame(tick), game);
        }
        assertTrue(eagle.isMarkedForRemoval());
        assertTrue(eagle.distanceFrom(100, 100) < dimensions.tileSize() + 8);
        assertEquals(27, inventory.getFood());
    }

    @Test
    public void magpieStealsOneCoinThenReturnsAndDisappears() {
        Magpie magpie = new Magpie(100, 100, game.getPlayer());
        magpie.setX(400);
        magpie.setY(400);
        magpie.updateAttack(idle, game);
        assertEquals(19, inventory.getCoins());
        assertFalse(magpie.isAttacking());
        for (int tick = 0; tick < 400 && !magpie.isMarkedForRemoval(); tick++) {
            magpie.tick(idle.withFrame(tick), game);
        }
        assertTrue(magpie.isMarkedForRemoval());
        assertEquals(19, inventory.getCoins());
    }

    @Test
    public void eagleRemovedDuringReturnRefundsStolenFood() {
        Eagle eagle = new Eagle(100, 100, game.getPlayer());
        eagle.setX(400);
        eagle.setY(400);
        eagle.updateAttack(idle, game);
        assertEquals(27, inventory.getFood());
        eagle.setX(250);
        eagle.setY(250);
        enemies.getBirds().add(eagle);
        eagle.markForRemoval();
        enemies.tick(idle, game);
        assertTrue(enemies.getBirds().isEmpty());
        assertEquals(30, inventory.getFood());
    }

    @Test
    public void magpieRemovedDuringReturnRefundsStolenCoin() {
        Magpie magpie = new Magpie(100, 100, game.getPlayer());
        magpie.setX(400);
        magpie.setY(400);
        magpie.updateAttack(idle, game);
        assertEquals(19, inventory.getCoins());
        magpie.setX(250);
        magpie.setY(250);
        enemies.getBirds().add(magpie);
        magpie.markForRemoval();
        enemies.tick(idle, game);
        assertTrue(enemies.getBirds().isEmpty());
        assertEquals(20, inventory.getCoins());
    }
}
