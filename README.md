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

- `com.crown.maps` - 2D (tiled) maps (3D maps)

- `com.crown.skills` - `Creature` skills API

- `com.crown.time` - Virtual clock implementation & Timelines (time-travelling API - very unstable)

- `com.crown.common.utils` - `Random` class extensions, RPG-specific

## Usage

### Maven dependency (Jitpack)

Latest version is unstable, so use this commit instead:

```
<dependency>
    <groupId>com.github.F1uctus</groupId>
    <artifactId>crown-framework</artifactId>
    <version>021993f86c30a3315da45362de64fb35f713d475</version>
</dependency>
```
