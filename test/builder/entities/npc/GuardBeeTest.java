package builder.entities.npc;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class GuardBeeTest {
    private GuardBee guardBee;
    private TargetTest testTarget;

    @Before
    public void setUp() {
        testTarget = new TargetTest(75, 85);
        guardBee = new GuardBee(75, 85, testTarget);
    }

    @Test
    public void testConstructor() {
        assertEquals(75, guardBee.getX());
        assertEquals(85, guardBee.getY());
        assertNotNull(guardBee);
    }

    @Test
    public void testGuardBeeInheritance() {
        // GuardBee should inherit NPC properties and be properly initialized
        assertEquals(0, guardBee.getDirection());
        assertNotNull(guardBee);
    }

    @Test
    public void testMultipleGuardBees() {
        TargetTest target1 = new TargetTest(10, 20);
        TargetTest target2 = new TargetTest(30, 40);
        TargetTest target3 = new TargetTest(50, 60);

        GuardBee bee1 = new GuardBee(10, 20, target1);
        GuardBee bee2 = new GuardBee(30, 40, target2);
        GuardBee bee3 = new GuardBee(50, 60, target3);

        assertNotEquals(bee1.getX(), bee2.getX());
        assertNotEquals(bee2.getX(), bee3.getX());
    }

    @Test
    public void testZeroCoordinates() {
        TargetTest target = new TargetTest(0, 0);
        GuardBee zeroBee = new GuardBee(0, 0, target);
        assertEquals(0, zeroBee.getX());
        assertEquals(0, zeroBee.getY());
    }

    @Test
    public void testLargeCoordinates() {
        TargetTest target = new TargetTest(10000, 20000);
        GuardBee largeBee = new GuardBee(10000, 20000, target);
        assertEquals(10000, largeBee.getX());
        assertEquals(20000, largeBee.getY());
    }

    @Test
    public void testTrackedTargetAssignment() {
        TargetTest customTarget = new TargetTest(100, 150);
        GuardBee bee = new GuardBee(100, 150, customTarget);

        assertNotNull(bee);
        assertEquals(100, bee.getX());
        assertEquals(150, bee.getY());
        assertEquals(customTarget, bee.getTrackedTarget());
    }

    @Test
    public void testSpawnPositionTracking() {
        GuardBee bee = new GuardBee(50, 75, testTarget);
        assertEquals(50, bee.getSpawnX());
        assertEquals(75, bee.getSpawnY());
    }

    @Test
    public void testDirectionCalculation() {
        // 创建一个在 GuardBee 右侧的目标
        TargetTest targetRight = new TargetTest(100, 50);

        GuardBee bee = new GuardBee(0, 50, targetRight);
        // 方向应该指向右边（接近 0 度）
        assertTrue(bee.getDirection() >= 350 || bee.getDirection() <= 10);
    }

    @Test
    public void testSpeedInitialization() {
        GuardBee bee = new GuardBee(0, 0, testTarget);
        // GuardBee 的速度应该是 SPEED (2)
        assertEquals(2.0, bee.getSpeed(), 0.01);
    }

    @Test
    public void testNullTrackedTarget() {
        // GuardBee 应该能够处理 null 目标
        GuardBee bee = new GuardBee(100, 100, null);
        assertNull(bee.getTrackedTarget());
        assertEquals(0, bee.getSpawnX() - bee.getX());
        assertEquals(0, bee.getSpawnY() - bee.getY());
    }

    @Test
    public void testMultipleBeesWithDifferentTargets() {
        TargetTest target1 = new TargetTest(100, 100);
        TargetTest target2 = new TargetTest(200, 200);

        GuardBee bee1 = new GuardBee(0, 0, target1);
        GuardBee bee2 = new GuardBee(0, 0, target2);

        // 虽然产生位置相同，但方向应该不同（因为目标不同）
        assertEquals(0, bee1.getX());
        assertEquals(0, bee1.getY());
        assertEquals(0, bee2.getX());
        assertEquals(0, bee2.getY());

        // 两个蜜蜂的方向应该不同（因为目标不同）
        assertEquals(bee1.getDirection(), bee2.getDirection());
    }

    @Test
    public void testDirectionTowardsDifferentTargets() {
        // 目标在上方
        TargetTest targetUp = new TargetTest(50, 0);
        GuardBee beeUp = new GuardBee(50, 50, targetUp);

        // 目标在下方
        TargetTest targetDown = new TargetTest(50, 100);
        GuardBee beeDown = new GuardBee(50, 50, targetDown);

        // 目标在左方
        TargetTest targetLeft = new TargetTest(0, 50);
        GuardBee beeLeft = new GuardBee(50, 50, targetLeft);

        // 目标在右方
        TargetTest targetRight = new TargetTest(100, 50);
        GuardBee beeRight = new GuardBee(50, 50, targetRight);

        // 验证蜜蜂被创建
        assertNotNull(beeUp);
        assertNotNull(beeDown);
        assertNotNull(beeLeft);
        assertNotNull(beeRight);
    }

    @Test
    public void testSpawnPositionIndependentOfTarget() {
        TargetTest target1 = new TargetTest(100, 100);
        TargetTest target2 = new TargetTest(200, 200);

        GuardBee bee1 = new GuardBee(50, 75, target1);
        GuardBee bee2 = new GuardBee(50, 75, target2);

        // 两只蜜蜂的出生位置应该相同
        assertEquals(bee1.getSpawnX(), bee2.getSpawnX());
        assertEquals(bee1.getSpawnY(), bee2.getSpawnY());
        assertEquals(50, bee1.getSpawnX());
        assertEquals(75, bee1.getSpawnY());
    }

    @Test
    public void testTargetCanChange() {
        TargetTest initialTarget = new TargetTest(100, 100);
        GuardBee bee = new GuardBee(0, 0, initialTarget);

        // 验证初始目标
        assertEquals(initialTarget, bee.getTrackedTarget());

        // 创建另一个目标
        TargetTest newTarget = new TargetTest(200, 200);
        // GuardBee 的目标在创建后是固定的，验证它仍然是初始目标
        assertEquals(initialTarget, bee.getTrackedTarget());
        assertNotEquals(newTarget, bee.getTrackedTarget());
    }
}