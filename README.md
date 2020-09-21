<h1 align="center">👑 Crown 👑</h1>

<p align="center">
    <img src="https://github.com/F1uctus/crown/workflows/GitHub%20Package/badge.svg?branch=android"
         alt="GitHub Actions status for 'GitHub Package'">
    <img src="https://github.com/F1uctus/crown/workflows/Build%20&%20Verify/badge.svg?branch=android"
         alt="GitHub Actions status for 'Build & Verify'">
    <a href="https://gitmoji.carloscuesta.me">
        <img src="https://img.shields.io/badge/gitmoji-%20😜%20😍-FFDD67.svg?style=flat-square"
             alt="Gitmoji">
    </a>
</p>

<h3 align="center">A Java framework for RPG/roguelikes.</h3>

## API Structure

| Package | Description |
|-|-|
| `com.crown.creatures`           | Basic `Creature` API
| `com.crown.i18n`                | Internationalization helpers
| `com.crown.maps`                | 3D / 2D(tiled) maps logic
| — `...maps.pathfinding`         | 3D map pathfinding algorithms and heuristics
| `com.crown.skills`              | `Creature` skills API
| `com.crown.time`                | `VirtualClock` implementation
| `com.crown.common.utils`        | `Random` class extensions & other utility stuff

## Usage

### Gradle dependency (GitHub Packages)

```
implementation 'com.github.f1uctus:crown:android-VERSION'
```

To see the most comprehensive usage overview,
see my [demo Android game](https://github.com/f1uctus/escape-from-castilla) implementation.

### Code

Every game must initialize these base structures at the program beginning:

#### VirtualClock

`VirtualClock` is an abstraction over a fixed-delay timer,
to allow your game to perform periodical actions.<br>
In general an instance is created that way:
```java
var gameClock = new VirtualClock(
    10, // period between game ticks, in milliseconds
    gameWindow::repaint // a function to execute on every tick
);
```
Then, clock is started using `startAt(Instant)` or `startAtRnd()`.

#### GameState

`GameState` should be implemented in your code by inheritance from `BaseGameState`.
That object contains global game map, and a players collection.

Initialization example:
```java
var gameState = new GameState(new GlobalMap(
    "Map name",
    500,            // x size
    500,            // y size
    MapLevel.height // z size
))
```

#### Main timeline

`Timeline` is a main storage of actions performed by every creature in the game.
It is a 'heart' of time-travelling logic.

Initialization is static:
```java
Timeline.init(gameClock, gameState);
```

#### Internationalization

Most of Crown's action methods return `ITemplate` object - a localizable message storage,
so you must add at least one resource bundle to store built-in message properties. They include:
| Property name              | Description |
|----------------------------|-------------|
| `message.ok`               | Action successful. |
| `delta.outOfBounds`        | Some property (e.g. `hp`) went out of range while executing action. |
| `move.lowEnergy`           | Obvious. |
| `move.obstacle`            | Obvious. |

To easily interact with your resource bundles there is an `I18n` class with static
methods to wrap your messages to the localizable `ITemplate`s.
To translate `ITemplate` to some language, a `ITemplate.getLocalized(String lang)` method is used.
Of course, you can add your own localizable messages and use them the same way.
For example:
```java
var result = I18n.of("mage.unknownSpell");
result.getLocalized("en") // => I don't know that spell.
```

To initialize `I18n` class this approach is used:
```java
var bundles = new HashMap<String, ResourceBundle>();
// "gameMessages" is a name for your resource bundle file, without a locale.
bundles.put("ru", ResourceBundle.getBundle("gameMessages", new Locale("ru_RU")));
bundles.put("en", ResourceBundle.getBundle("gameMessages", new Locale("en_US")));
// more bundles...
I18n.init(bundles);
```