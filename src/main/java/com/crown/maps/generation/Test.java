package com.crown.maps.generation;

import com.crown.common.utils.Random;
import de.articdive.jnoise.JNoise;
import org.jetbrains.annotations.NotNull;
import org.mini2Dx.gdx.math.Vector2;

public class Test {
    private static final int numOctaves = 7;
    private static final float persistence = .5f;
    private static final float lacunarity = 2;
    private static final float initialScale = 2;

    private static final @NotNull JNoise noise = JNoise.newBuilder().openSimplex().build();

    /**
     * Generates a height (0.0-1.0) map with specified size.
     */
    public static float[] generateHeightMap(int mapSize) {
        var map = new float[mapSize * mapSize];

        var offsets = new Vector2[numOctaves];
        for (int i = 0; i < numOctaves; i++) {
            offsets[i] = new Vector2(Random.getInt(-1000, 1000), Random.getInt(-1000, 1000));
        }

        float minValue = Float.MAX_VALUE;
        float maxValue = Float.MIN_VALUE;

        for (int y = 0; y < mapSize; y++) {
            for (int x = 0; x < mapSize; x++) {
                float noiseValue = 0;
                float scale = initialScale;
                float weight = 1;
                for (int i = 0; i < numOctaves; i++) {
                    Vector2 p = offsets[i].add(new Vector2(x / (float) mapSize, y / (float) mapSize).scl(scale));
                    noiseValue += noise.getNoise(p.x, p.y) * weight;
                    weight *= persistence;
                    scale *= lacunarity;
                }
                map[y * mapSize + x] = noiseValue;
                minValue = Math.min(noiseValue, minValue);
                maxValue = Math.max(noiseValue, maxValue);
            }
        }

        // Normalize
        if (maxValue != minValue) {
            for (int i = 0; i < map.length; i++) {
                map[i] = (map[i] - minValue) / (maxValue - minValue);
            }
        }

        return map;
    }
}
