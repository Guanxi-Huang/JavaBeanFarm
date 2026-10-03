# JavaBeanFarm Controls and Features

This guide combines the features described in Assignment 1 and Assignment 2 with the controls in the current project.
Features required by the assignments are listed separately from known implementation limits.
A tick means one game update, not one second.

## Controls

Make sure the game window has keyboard focus before using these controls.

| Key or action | Function | Conditions and result |
| --- | --- | --- |
| `W` | Move up | Water blocks movement. |
| `S` | Move down | Water blocks movement. |
| `A` | Move left | Water blocks movement. |
| `D` | Move right | Water blocks movement. |
| `1` | Select the Bucket | Left-click on empty, tilled dirt to plant a cabbage for 2 coins. |
| `2` | Select the Hoe | Left-click on empty grass to turn it into dirt, then use the hoe on dirt to till it. |
| `3` | Select the Jackhammer | Stand on an ore tile and hold the left mouse button to mine coins. |
| `4` | Select the HiveHammer | Left-click on empty grass to place a beehive for 2 coins and 2 food. |
| `5` | Select the Pole | Left-click on empty, tilled dirt to place a scarecrow for 2 coins. |
| Left mouse button | Use the selected tool | The tool affects the tile under the player, not the tile under the mouse pointer. |
| Walk onto a mature cabbage | Harvest automatically | Gain 2 food and 3 coins, then remove the cabbage. No click is needed. |

Hold the left mouse button to keep using a tool while standing still or moving.
If several movement keys are held, only one direction is used, with priority `W`, `S`, `A`, then `D`.
Diagonal movement is not supported.
The red border in the inventory bar shows the selected tool.
The resource display shows the current coin and food totals.
The assignment descriptions and current game code do not define controls for pausing, saving, loading, or removing placed towers.

## Basic Farm Features

| Feature | Behaviour |
| --- | --- |
| Mining | Each ore starts with 10 coins. The player can collect up to 2 coins per mining action, once every 5 ticks. |
| Preparing soil | Use the hoe to change empty grass into dirt, then till the dirt. |
| Planting | A cabbage needs empty, tilled dirt and at least 2 coins. Planting costs 2 coins. |
| Crop growth | A cabbage grows automatically through five states: initial, budding, growing, grown, and collectable. Its code documentation specifies a transition every 100 ticks. |
| Harvesting | Walk onto a collectable cabbage to gain 2 food and 3 coins. The cleared tile can be planted again. |
| Map loading | A `.map` file defines terrain. A `.details` file defines the player position, starting resources, initial cabbages, and bird spawner positions and intervals. |
| Brutus | Assignment 1 includes a randomly moving demonstration character in stage 0. The current Assignment 2 game does not create it by default. |

## Birds and Towers Required by Assignment 2

These are the behaviours required by the assignment.
See the implementation limits below before assuming every behaviour is fully supported by the current code.

| Object | Required behaviour |
| --- | --- |
| Pigeon | Fly towards the nearest cabbage, steal it, then return to the spawn point and disappear. If no cabbage exists, return to spawn. |
| Eagle | Fly towards the player, steal 3 food, then return to spawn and disappear. If removed before reaching spawn, return the stolen food. |
| Magpie | Fly towards the player and try to steal 1 coin, then return to spawn and disappear. If removed before reaching spawn, return the stolen coin. |
| Bird spawner | Spawn birds at the position and interval set in the `.details` file. Spawn pigeons only when cabbages exist. |
| Beehive | When loaded, spawn one guard bee if a bird enters its 350-pixel detection range. |
| Beehive reload | Reload every 240 ticks. Standing on the hive should increase the reload rate by three, giving an 80-tick reload when the player stays on it. |
| Guard bee | Follow the nearest bird. On contact, remove both the bee and the bird. Return to spawn when no birds remain. |
| Scarecrow | Make pigeons and magpies within 4 tiles stop attacking and return to spawn. Eagles are not affected. |

Bird movement, spawning, crop growth, hive attacks, and scarecrow effects happen automatically.
They do not need extra keyboard controls.

## Current Implementation Limits

The current beehive code does not correctly implement the three-times reload rate while the player stands on it.
Its timer is also advanced in more than one update path.

Guard bees currently choose the first bird found within range, which may not be the nearest bird.
Their return-to-spawn behaviour when no birds remain is also incomplete.

The eagle and magpie resource-return logic does not cover every case where a bird is removed before reaching spawn.

Magpie and pigeon spawners also register the same spawned bird twice in the enemy list.

The latest feature verification ran 519 tests: 511 passed and 8 failed.
The original 480 tests still pass, but 8 of the 39 new feature checks expose incorrect required behaviour.
See [FEATURE-TEST-RESULTS.md](FEATURE-TEST-RESULTS.md) for the coverage table, failed checks, and reproduction commands.

## Suggested Play Sequence

1. Press `3`, stand on ore, and hold the left mouse button to collect coins.
2. Press `2` and use the hoe to prepare empty ground for planting.
3. Press `1` and left-click on empty, tilled dirt to plant cabbages.
4. Wait for the cabbages to mature, then walk onto them to collect food and coins.
5. Press `4` to place beehives on empty grass, or press `5` to place scarecrows on empty, tilled dirt.

## Sources

- Assignment 1: `F:/UQ Courses/CSSE2002/Assignment1/Assignment 1.pdf`
- Assignment 2: `F:/UQ Courses/CSSE2002/Assignment2-description.pdf`
- Current controls and behaviour: the player, inventory, tile, resource, and NPC classes in this project.
