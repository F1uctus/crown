<h1 align="center">Crown | early WIP</h1>

<p align="center">
    <a href="https://gitmoji.carloscuesta.me">
        <img src="https://img.shields.io/badge/gitmoji-%20😜%20😍-FFDD67.svg?style=flat-square"
             alt="Gitmoji">
    </a>
</p>

<h3 align="center">A Java micro-framework for fast &amp; easy RPG creation.</h3>

## API Structure

- `com.crown.creatures` - Basic `Creature` API

- `com.crown.i18n` - Internationalization helpers

- `com.crown.items` - Pickable inventory items

- `com.crown.maps` - 3D / 2D(tiled) maps logic

- `com.crown.skills` - `Creature` skills API

- `com.crown.time` - `VirtualClock` implementation + `Timeline` (time-travelling API - a bit unstable)

- `com.crown.common.utils` - `Random` class extensions, RPG-specific

## Usage

### Maven dependency (Jitpack)

```
<dependency>
    <groupId>com.github.F1uctus</groupId>
    <artifactId>crown-framework</artifactId>
    <version>-SNAPSHOT</version>
</dependency>
```

## Time-travelling API implementation overview

I tried to keep diagram as simple as possible, but feel free to report any inconvenience.
It is a bit deprecated now. New diagram is WIP.

![Crown time travelling API overview](/images/crown-timelines-overview.png)
