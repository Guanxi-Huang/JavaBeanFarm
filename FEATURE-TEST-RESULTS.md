# JavaBeanFarm Feature Test Results

Verification date: 2026-10-03, Australia/Brisbane.
The verification budget was three rounds: existing tests, additional feature tests, and the combined suite.

## Result

The current project compiles.
The combined JUnit suite ran 519 tests: 511 passed and 8 failed.
The original 480 tests still pass.
The 39 additional feature checks include 31 passes and 8 failures.
No new feature check was skipped or disabled.
The failures were retained as assertions against the required behaviour.
Production code and the supplied system tests were not changed during this testing task.

## Coverage Summary

| Feature | Result | Evidence |
| --- | --- | --- |
| WASD movement, movement priority, and water collision | Pass | New checks verify exact coordinates and blocked movement. |
| Number keys 1-5 and inventory highlight | Pass | New checks verify selected slots, tool classes, and border sprites. |
| Left-click, held left button, and interaction under the player | Pass | New checks verify placement, cost, and no duplicate cabbage planting. |
| Right-click does not use a tool | Pass | No cabbage is planted and coins are unchanged. |
| Grass conversion and dirt tilling | Pass | New checks verify replacement, tilled state, and unchanged resources. |
| Planting and invalid planting conditions | Pass | New checks verify the two-coin cost, tilled soil, and insufficient coins. |
| Mining | Pass | New checks verify the five-tick interval, two-coin yield, ten-coin limit, depleted sprite, and tool restriction. |
| Crop growth, harvesting, and replanting | Pass | New checks verify all growth sprites, timing, rewards, removal, and reuse of soil. |
| Hive and scarecrow placement | Pass | New checks verify tile restrictions, costs, insufficient food, and prevention of repeated placement. |
| Map and details loading | Pass | Existing WorldBuilder, OverlayBuilder, and LoadDetails tests pass. |
| Coin and food display | Pass in simulation | Existing ResourceSimulationTest and LoadDetailsTest inspect rendered digits. |
| Scarecrow range and bird types | Pass | New checks verify nearby pigeons and magpies are repelled, while eagles and distant magpies are unaffected. |
| Loaded hive detection and one-shot firing | Pass | New checks verify range and that an unloaded hive cannot fire again. |
| Hive reload timing | Fail | The hive reloads early without the player and does not reload within 80 ticks while the player stands on it. |
| Guard bee target selection | Fail | The bee follows the first bird instead of the nearest one. |
| Guard bee return when no birds remain | Fail | The bee keeps moving away from its spawn. |
| Guard bee contact with a bird | Pass | Both entities are marked for removal. |
| Eagle spawning | Pass | New checks verify interval, position, and one registration per bird. |
| Magpie and pigeon spawning | Fail | Both spawners register one bird twice. |
| No pigeon spawning without cabbages | Pass | The enemy list remains empty. |
| Bird theft, return, and normal removal | Pass | New checks verify cabbage removal, food and coin deductions, return state, and removal near spawn. |
| Eagle and magpie refunds after removal during return | Fail | Stolen food and coins are not restored. |
| Game startup | Pass | A fresh game window remained responsive for 15 seconds, with zero bytes in stderr. |
| Native keyboard/mouse delivery and visual appearance | Not verified | Inputs were simulated through the real game classes; no manual desktop playthrough or pixel-level inspection was performed. |
| Brutus | Not applicable to the default game | It is an Assignment 1 demonstration character and is not created by the current default Assignment 2 game. |

## Confirmed Failures

| Test | Required result | Observed result |
| --- | --- | --- |
| eagleRemovedDuringReturnRefundsStolenFood | Food returns to 30 after removing the returning eagle. | Food remains 27. |
| guardBeeTargetsNearestBirdInsteadOfFirstBird | The bee turns right towards the nearest bird, direction 0. | It turns left towards the first bird, direction 180. |
| magpieSpawnerRegistersEachBirdOnce | One enemy-list entry per spawn. | Two entries refer to the spawned bird. |
| standingOnHiveReloadsItWithinEightyTicks | The hive can fire again after 80 ticks. | The hive cannot fire. |
| pigeonSpawnerRegistersEachBirdOnceWhenCabbageExists | One enemy-list entry per spawn. | Two entries refer to the spawned bird. |
| hiveDoesNotReloadEarlyWithoutPlayer | The hive remains unloaded before 240 ticks. | The hive is already loaded. |
| guardBeeReturnsTowardsSpawnWhenBirdsDisappear | The bee moves back towards its spawn at x=100. | It continues moving away. |
| magpieRemovedDuringReturnRefundsStolenCoin | Coins return to 20 after removing the returning magpie. | Coins remain 19. |

## Additional Feature Checks

| Test | Result |
| --- | --- |
| `wMovesUpOnePixel` | PASS |
| `sMovesDownOnePixel` | PASS |
| `aMovesLeftOnePixel` | PASS |
| `dMovesRightOnePixel` | PASS |
| `simultaneousMovementUsesDocumentedPriorityWithoutDiagonalMovement` | PASS |
| `waterBlocksMovementAcrossTileBoundary` | PASS |
| `numberKeysSelectAllFiveToolsAndHighlightSelectedSlot` | PASS |
| `bucketPlantsUnderPlayerInsteadOfMouseAndChargesTwoCoins` | PASS |
| `rightClickDoesNotUseTool` | PASS |
| `holdingLeftDoesNotPlantDuplicateCabbages` | PASS |
| `plantingRequiresTilledSoilAndEnoughCoins` | PASS |
| `hoeTurnsGrassIntoDirt` | PASS |
| `hoeTillsDirtWithoutResourceCost` | PASS |
| `jackhammerMinesOnlyEveryFiveTicksAndStopsAfterTenCoins` | PASS |
| `otherToolsCannotMineOre` | PASS |
| `cabbageAdvancesThroughEachGrowthSpriteEveryHundredTicks` | PASS |
| `walkingOntoMatureCabbageHarvestsAndAllowsReplanting` | PASS |
| `immatureCabbageCannotBeHarvested` | PASS |
| `hiveHammerPlacesOneHiveAndChargesBothResources` | PASS |
| `hiveCannotBePlacedWithoutEnoughFoodOrOnDirt` | PASS |
| `polePlacesOneScarecrowOnTilledDirtForTwoCoins` | PASS |
| `scarecrowRepelsOnlyPigeonsAndMagpiesWithinFourTiles` | PASS |
| `loadedHiveFiresOnlyOneBeeWithinDetectionRange` | PASS |
| `hiveReloadsAfterTwoHundredFortyTicksWithoutPlayer` | PASS |
| `hiveDoesNotReloadEarlyWithoutPlayer` | FAIL |
| `standingOnHiveReloadsItWithinEightyTicks` | FAIL |
| `guardBeeTargetsNearestBirdInsteadOfFirstBird` | FAIL |
| `guardBeeReturnsTowardsSpawnWhenBirdsDisappear` | FAIL |
| `guardBeeContactRemovesBothBeeAndBird` | PASS |
| `eagleSpawnerWaitsForIntervalAndRegistersEachBirdOnce` | PASS |
| `magpieSpawnerRegistersEachBirdOnce` | FAIL |
| `pigeonSpawnerRegistersEachBirdOnceWhenCabbageExists` | FAIL |
| `pigeonSpawnerDoesNotSpawnWithoutCabbages` | PASS |
| `pigeonStealsCabbageAndReturnsToSpawn` | PASS |
| `pigeonWithoutCabbagesReturnsInsteadOfAttacking` | PASS |
| `eagleStealsThreeFoodThenReturnsAndDisappears` | PASS |
| `magpieStealsOneCoinThenReturnsAndDisappears` | PASS |
| `eagleRemovedDuringReturnRefundsStolenFood` | FAIL |
| `magpieRemovedDuringReturnRefundsStolenCoin` | FAIL |

## Reproduce

Run from the project folder:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File scripts/java.ps1 test
```

Run only the new feature checks after building:

```powershell
java -cp "out/vscode;lib/*" org.junit.runner.JUnitCore builder.AllFeaturesTest
```

The command exits with failure while these eight required behaviours remain incorrect.
Some older tests only verify non-null values or successful execution, so their passing result is not evidence of complete behavioural coverage.
This report does not claim exhaustive coverage of every possible input or timing combination.

## Files

- New checks: `test/builder/AllFeaturesTest.java`
- Original suite result: `out/all-features-baseline.txt`
- New checks result: `out/all-features-targeted.txt`
- Combined result: `out/all-features-final.txt`
- Runtime result: `out/all-features-game-result.txt`
- Runtime error output: `out/all-features-game-stderr.txt`
