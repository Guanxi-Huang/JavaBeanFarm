Document your refactoring choices here. Delete this file if you choose to use a PDF or txt file format instead.

## Refactoring Journey

### Phase 1: Code Analysis & Problem Identification

#### Initial Observations 

When first examining initial version, I noticed several codes are similar:

1. **Eagle.java** (107 lines) - Too long for what should be a simple enemy
2. **Magpie.java** & **Pigeon.java** - Nearly identical structure to Eagle
3. **Expirable interface** - Implemented identically in all enemy classes
4. **distanceFrom() duplication** in Npc.java - Same calculation in two methods
5. **Repetitive code in constructors** - Each enemy duplicated initialization logic

**Personal Experience**: I realized that when I wanted to add a 4th enemy type (hypothetically), I would:
1. Copy Eagle.java and rename it
2. Copy the same lifecycle code
3. Copy the same movement logic
4. Risk introducing bugs in any of these duplicated sections
5. Have 4 places to fix if I found a bug in the attack logic

This is the classic **code smell** that signals we're violating the DRY (Don't Repeat Yourself) principle.

### Phase 2: Strategy Development

#### Approach Taken

I decided to address three main areas:

**Area 1: Data Duplication**
- Problem: `distanceFrom()` calculated the same thing twice
- Solution: Use method delegation (one method calls the other)
- Benefit: If we ever need to change distance algorithm, one edit fixes it everywhere

**Area 2: Behavior Duplication**
- Problem: All enemies manage lifespan, track targets, update direction identically
- Solution: Create abstract Enemy parent class with common code
- Benefit: New enemies only implement unique behavior

**Area 3: Documentation Gap**
- Problem: No Javadoc meant IDE couldn't help with auto-complete
- Solution: Add comprehensive Javadoc to every class and method
- Benefit: Self-documenting code, better IDE support

#### Design Pattern Selection

**Why Template Method Pattern?**

Personal reasoning:
- Eagle, Magpie, Pigeon all have similar tick() logic:
    1. Check if alive
    2. If attacking: chase target, steal resources
    3. If returning: go back to spawn
    4. Move

- But the "steal resources" part is unique to each enemy

- Solution: Parent class defines steps 1-4, child class implements "steal resources"

This is exactly what Template Method Pattern is for!

### Phase 3: Implementation & Refactoring

#### Refactoring 1: Npc.java - Method Delegation

**Before (V1)**:
```java
public int distanceFrom(HasPosition position) {
    int deltaX = position.getX() - this.getX();
    int deltaY = position.getY() - this.getY();
    return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
}

public int distanceFrom(int xCoordinate, int yCoordinate) {
    int deltaX = xCoordinate - this.getX();
    int deltaY = yCoordinate - this.getY();
    return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
}
```

**Process of Thinking**:
1. Recognized duplicate sqrt calculation
2. Asked: "Which version is the source of truth?"
3. Decided: int version (more primitive, no conversion needed)
4. Made HasPosition version delegate to it
5. Added Javadoc to explain the relationship

**After (V2)**:
```java
public int distanceFrom(HasPosition position) {
    return distanceFrom(position.getX(), position.getY());
}

public int distanceFrom(int x, int y) {
    int deltaX = x - this.getX();
    int deltaY = y - this.getY();
    return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
}
```

**Personal Insight**: This taught me that when I see similar code, I should ask "Can one version call the other?" before assuming they need to be separate.

#### Refactoring 2: Eagle.java - Simplify Through Inheritance

**The Problem** (V1 - 107 lines):

Eagle class had three main issues:

1. **Interface Implementation Duplication**
   ```java
   public class Eagle extends Enemy implements Expirable {
       private FixedTimer lifespan = new FixedTimer(5000);
       
       @Override
       public FixedTimer getLifespan() {
           return lifespan;
       }
       
       @Override
       public void setLifespan(FixedTimer timer) {
           this.lifespan = timer;
       }
   }
   ```
   When I checked Magpie and Pigeon, they had **identical** code. Why repeat it?

2. **Complex Constructor**
   ```java
   public Eagle(int x, int y, HasPosition trackedTarget) {
       super(x, y);
       this.spawnX = x;        // Duplicate assignment
       this.spawnY = y;        // Duplicate assignment
       int direction = 20;
       this.setDirection(direction);
       this.setSpeed(2);
       this.trackedTarget = trackedTarget;
       
       this.setSprite(art.getSprite("default"));
       
       if (attacking) {
           double deltaX = trackedTarget.getX() - this.getX();
           double deltaY = trackedTarget.getY() - this.getY();
           this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
       } else {
           // EXACT SAME CODE AS ABOVE
           double deltaX = trackedTarget.getX() - this.getX();
           double deltaY = trackedTarget.getY() - this.getY();
           this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
       }
   }
   ```
   Red flag: If-else with identical code in both branches!

3. **50-Line tick() Method**
   ```java
   @Override
   public void tick(EngineState engine, GameState game) {
       super.tick(engine);
       this.lifespan.tick();
       if (this.lifespan.isFinished()) {
           this.markForRemoval();
       }
       // Check distance to player
       if ((this.distanceFrom(game.getPlayer().getX(), game.getPlayer().getY())
                       < engine.getDimensions().tileSize())
               && this.attacking) {
           this.attacking = false;
           if (this.food == 0) {
               game.getInventory().addFood(-3);
               this.food = 3;
           }
           this.setSpeed(4);
       }
       // Check if back at spawn
       if ((this.distanceFrom(this.spawnX, this.spawnY) < engine.getDimensions().tileSize())
               && !this.attacking) {
           this.markForRemoval();
       }
       this.move();
       
       // Complex direction and sprite update logic...
       if (attacking) {
           double deltaX = trackedTarget.getX() - this.getX();
           double deltaY = trackedTarget.getY() - this.getY();
           this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
           // ... more code ...
       }
   }
   ```

**Issues I Identified**:

1. **The Expirable Interface Problem**
    - I checked Magpie.java - had identical getLifespan() and setLifespan()
    - I checked Pigeon.java - same thing
    - Personal thought: "Why are we using an interface for something that's identical across all implementations?"
    - Answer: The interface is unnecessary; these should just be inherited

2. **The tick() Method Problem**
    - Examined the 50-line tick() method
    - Noticed: 70% of logic is identical to Magpie.java and Pigeon.java
    - Only the attack behavior differs
    - Personal realization: "This is exactly what Template Method Pattern solves!"

3. **The Constructor Complexity**
    - Noted: if-else branches with identical code in both branches
    - Confusion: Why would both branches do the same thing?
    - Logic flaw found and fixed in V2

**Refactoring Process**:

**Step 1: Made Enemy abstract** (prevents accidental instantiation)
```java
// V1
public class Enemy extends Npc { }

// V2  
public abstract class Enemy extends Npc { }
```
**Why**: Signals to other developers that Enemy is a base class, not meant to be used directly.

**Step 2: Moved common fields to Enemy**
```java
// Moved these from each subclass to Enemy:
private int spawnX;
private int spawnY;
private FixedTimer lifespan;
private boolean attacking;
private HasPosition trackedTarget;
```
**Why**: All enemies share these - avoid duplication.

**Step 3: Extracted helper methods**
```java
public void setTargetDirection(double x, double y) {
    double deltaX = x - this.getX();
    double deltaY = y - this.getY();
    this.setDirection((int) Math.toDegrees(Math.atan2(deltaY, deltaX)));
}

public void updateSprite(int trackedTargetY) {
    if (getSpriteGroup() == null) return;
    if (trackedTargetY > this.getY()) {
        this.setSprite(getSpriteGroup().getSprite("down"));
    } else {
        this.setSprite(getSpriteGroup().getSprite("up"));
    }
}

public boolean isClosed(HasPosition target, EngineState engine) {
    if (engine == null || target == null) return false;
    final var dims = engine.getDimensions();
    if (dims == null) return false;
    return distanceFrom(target) < engine.getDimensions().tileSize();
}
```
**Why**: These calculations happen in tick() for every enemy. Extract them once, reuse everywhere.

**Step 4: Implemented Template Method pattern**
```java
@Override
public void tick(EngineState engine, GameState game) {
    super.tick(engine, game);
    
    // Manage lifecycle - same for all enemies
    if (this.getLifespan() != null) {
        this.getLifespan().tick();
        if (this.getLifespan().isFinished()) {
            markForRemoval();
        }
    }
    
    // Different behavior based on attacking state
    if (isAttacking()) {
        setTargetDirection(getTrackedTarget().getX(), getTrackedTarget().getY());
        updateSprite(getTrackedTarget().getY());
        updateAttack(engine, game);  // <- Child implements this
    } else {
        setTargetDirection(getSpawnX(), getSpawnY());
        updateSprite(getSpawnY());
        if (isClosed(spawner.spawnPoint(), engine)) {
            markForRemoval();
        }
    }
    
    this.move();
}

// Abstract method - each enemy implements its own attack
public abstract void updateAttack(EngineState engine, GameState game);
```
**Why**: Creates a reusable skeleton that all enemies follow.

**Step 5: Simplified Eagle.java** (down to 87 lines)
```java
public class Eagle extends Enemy {
    private int food;
    
    public Eagle(int x, int y, HasPosition trackedTarget) {
        super(x, y);
        this.food = 0;
        this.setSpeed(2);
        setTrackedTarget(trackedTarget);
        setLifespan(new FixedTimer(5000));
        
        int initializeDirection = 20;
        if (trackedTarget != null) {
            setTargetDirection(trackedTarget.getX(), trackedTarget.getY());
        } else {
            initializeDirection = 90;
        }
        this.setDirection(initializeDirection);
    }
    
    @Override
    public void updateAttack(EngineState engine, GameState game) {
        Player player = game.getPlayer();
        if (player != null && isClosed(player, engine) && isAttacking()) {
            setAttacking(false);
            if (game.getInventory().getFood() > 0) {
                if (this.food == 0) {
                    game.getInventory().addFood(-3);
                    this.food = 3;
                }
                this.setSpeed(4);
            }
        }
        
        if (this.isMarkedForRemoval() && getTrackedTarget() != null
                && isClosed(getTrackedTarget(), engine)) {
            game.getInventory().addFood(this.food);
        }
    }
    
    public int getFood() { return food; }
    public void setFood(int food) { this.food = food; }
    
    @Override
    public SpriteGroup getSpriteGroup() {
        return art;
    }
}
```

**Result**:
- Original 107 lines → 87 lines (-18%)
- 50 lines of common code removed
- Only 37 lines of Eagle-specific behavior remain
- Much clearer what makes Eagle unique

**Personal Insight**: This showed me that good refactoring doesn't just make code shorter - it makes the *essential* logic visible. Now when I read Eagle.java, I immediately see: "Eagles steal food and run away." That's it. The rest is inherited common behavior.

#### Refactoring 3: Documentation - Javadoc Everywhere

**Before (V1)**:
```java
public Npc(int x, int y) {
    super(x, y);
}

public double getSpeed() {
    return speed;
}

public void move() {
    final int deltaX = (int) Math.round(Math.cos(Math.toRadians(this.direction)) * this.speed);
    // ...
}
```

**After (V2)**:
```java
/**
 * Instantiates a new Npc.
 *
 * @require x >= 0 && y >= 0;
 * @param x the x
 * @param y the y
 */
public Npc(int x, int y) {
    super(x, y);
}

/**
 * Gets speed.
 *
 * @return the speed
 */
public double getSpeed() {
    return speed;
}

/**
 * Adjust the X and Y of {@link Npc}
 */
public void move() {
    final int deltaX = (int) Math.round(Math.cos(Math.toRadians(this.direction)) * this.speed);
    // ...
}
```

**Why This Matters** (Personal Experience):

When I was debugging Magpie behavior, I needed to understand what `setDirection()` does. Without Javadoc, I had to:
1. Click through to the method definition
2. Read the code
3. Figure out what the angle convention is (degrees vs radians?)
4. Check other method calls to see how it's used

With Javadoc, I would just hover my mouse and see it instantly.

**Process Followed**:
1. Added class-level Javadoc
2. Added constructor Javadoc with @require tags for preconditions
3. Added getter/setter Javadoc
4. Added method Javadoc with @param and @return
5. Used meaningful descriptions, not just repeating the name

**Personal Preference**:
I always add @require tags because preconditions catch bugs early. If I see `@require x >= 0 && y >= 0`, 
I know to check boundary conditions when using that constructor.

---

## Detailed Refactoring Analysis

### Refactoring 1: Main.java - Code Formatting

**What**: Split long constructor call across multiple lines
**Lines Changed**: 31
**Before**: 1 line (140+ characters)
**After**: 2 lines (≤100 characters each)

**Why**:
- Easier to read on smaller screens
- Git diffs are clearer (shows exactly which FileReader changed)
- Follows Google Java Style Guide (100 char limit)

**Personal Preference**: I prefer this style because:
- When I'm reviewing code on my phone or tablet, long lines get cut off
- Broken into logical parameter groups makes them easier to track
- When I need to git blame a specific parameter, the history is clearer

---

### Refactoring 2: Npc.java - DRY Principle

**What**: Method delegation to eliminate code duplication
**Lines Changed**: 64-67, 76-80
**Duplication Eliminated**: 4 lines of identical code
**Benefit**: Single source of truth

**Why**:
- If we ever need to change distance calculation (e.g., Manhattan distance instead of Euclidean), we change it once
- Bugs in calculation fix everywhere automatically
- Easier to test - verify the algorithm once

**Personal Experience**:
I've worked on codebases where the same calculation exists in 5 places. When someone found a bug, we had to fix it in all 5 places and hope we didn't miss one. This refactoring prevents that pain.

---

### Refactoring 3: Eagle.java - Inheritance & Pattern Application

**What**: Remove Expirable interface, apply Template Method pattern, simplify from 107→87 lines

**Why Process**:

1. **Observation**: Magpie and Pigeon have identical getLifespan()/setLifespan()
2. **Question**: Is Expirable interface needed, or is it just duplicating code?
3. **Analysis**: All three implementations are identical
4. **Decision**: Move to parent class as regular fields/methods
5. **Benefit**: No need for interface when behavior is identical

**Why Abstract Parent**:
- Prevents `new Enemy()` mistakes
- Clear contract: subclasses must implement updateAttack() and getSpriteGroup()
- Enforced by compiler

**Personal Insight**:
When I first saw the Expirable interface, I thought "this is good design - it's an interface!" But then I realized the interface only had getters/setters with no custom behavior. The interface wasn't adding value - it was just adding complexity. This taught me that good design isn't about using every tool available - it's about using the *right* tool for the problem.

---

### Refactoring 4: Enemy.java - Abstract Parent Pattern

**What**: Created abstract parent class with Template Method implementation

**Why This Was Needed**:

Looking at the tick() methods in V1:

**Eagle.java tick()**:
```
Check lifespan → Remove if expired → Check distance to player → 
  Update sprite → Move → Repeat
```

**Magpie.java tick()**:
```
Check lifespan → Remove if expired → Check distance to player → 
  Update sprite → Move → Repeat
```

**Pigeon.java tick()**:
```
Check lifespan → Remove if expired → Check distance to player → 
  Update sprite → Move → Repeat
```

The pattern is identical! Only the "attack player" part differs.

**The Refactoring**:

Extracted the common pattern into Enemy.java:
```java
@Override
public void tick(EngineState engine, GameState game) {
    // COMMON PART 1: Manage lifespan
    if (this.getLifespan() != null) {
        this.getLifespan().tick();
        if (this.getLifespan().isFinished()) {
            markForRemoval();
        }
    }
    
    // COMMON PART 2: Manage direction and sprite based on state
    if (isAttacking()) {
        setTargetDirection(getTrackedTarget().getX(), getTrackedTarget().getY());
        updateSprite(getTrackedTarget().getY());
        updateAttack(engine, game);  // VARIABLE PART - each subclass different
    } else {
        setTargetDirection(getSpawnX(), getSpawnY());
        updateSprite(getSpawnY());
        if (isClosed(spawner.spawnPoint(), engine)) {
            markForRemoval();
        }
    }
    
    // COMMON PART 3: Move
    this.move();
}
```

Now each subclass only implements:
```java
@Override
public void updateAttack(EngineState engine, GameState game) {
    // Eagle-specific: steal food
    // Magpie-specific: peck behavior
    // Pigeon-specific: coo behavior
}
```


### What I Learned

#### 1. Duplication Is Expensive
**Cost**: Not just code size - it's maintenance burden, bug risk, and cognitive load
**Before**: Didn't fully appreciate this
**After**: Always ask "Is this the third copy of this code?"

#### 2. Inheritance Needs Planning
**Challenge**: I initially considered keeping Expirable interface
**Realization**: When all implementations are identical, you don't need an interface
**Lesson**: Interfaces are for varying behavior; inheritance is for common code

#### 3. Template Method Solves Real Problems
**Observation**: All enemies follow same tick pattern
**Solution**: Template Method pattern made this explicit
**Benefit**: Adding a 4th enemy type is now trivial

#### 4. Documentation Is Not Optional
**Pain Point**: Spending 5 seconds reading code to understand a method
**Solution**: 5 seconds writing Javadoc saves everyone 5 seconds later
**ROI**: Huge

#### 5. Small Refactorings Add Up
**Observation**: distanceFrom() duplication seemed minor
**Impact**: 1 place to fix, 1 place to understand, 1 place to test
**Realization**: Small refactorings compound into large improvements

### Challenges Encountered

#### Challenge 1: Moving Code to Parent Class
**Issue**: What if some enemies don't need certain features?
**Solution**: If they don't use it, they don't use it. The parent still provides it. (Liskov Substitution - a subclass doesn't have to use all inherited features)

#### Challenge 2: Making Enemy Abstract
**Concern**: What if I need to create a generic Enemy for testing?
**Solution**: Never need to - always create specific subclass or mock it

#### Challenge 3: Null Safety
**Issue**: What if trackedTarget is null?
**Solution**: Added null checks in helper methods (isClosed, updateSprite)

#### Challenge 4: Raising new bugs
**Issue**: After refactoring, sometimes new bugs are raised. And if i can not fix them completely, 
    it shows my thoughts of refactoring maybe not correct, which took me a lot of time.
**Solution**: Navigate the codes, and find past version to compare.


## Additional Explanation:
- My poor time assignment caused tight time for builder.world refactor.
