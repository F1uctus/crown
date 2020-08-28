package com.crown.maps.vision;

import com.crown.maps.Map;

import java.util.ArrayList;

/**
 * This is a real-3D grid map field of vision logic implemented with
 * "Diamond raycasting" algorithm. This code was ported from very old
 * 1.5.1 version of libtcod written in C. Only God knows how it works now.
 * =============
 * libtcod 1.5.1
 * Copyright (c) 2008,2009,2010 Jice & Mingos
 */
public class DiamondRaycasting {
    static class Ray3 {
        int x, y, z; // position
        int obscurityX, obscurityY, obscurityZ; // obscurity vector
        int errorX, errorY, errorZ; // bresenham error
        Ray3 inputX, inputY, inputZ; // offset of input rays
        boolean added; // already in the fov
        boolean ignore; // non visible. don't bother processing it

        Ray3(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        boolean isObscure() {
            return errorX > 0 && errorX <= obscurityX
                || errorY > 0 && errorY <= obscurityY
                || errorZ > 0 && errorZ <= obscurityZ;
        }
    }

    private static int originX, originY, originZ; // fov origin
    private static Ray3[][][] rayMap; // result rays
    private static Ray3[][][] rayMap2; // temporary rays

    private static Ray3 newRay(Map map, int x, int y, int z) {
        if (!map.inBounds(x + originX, y + originY, z + originZ)) return null;
        var ray = rayMap2[x + originX][y + originY][z + originZ];
        if (ray == null)
            ray = new Ray3(x, y, z);
        return ray;
    }

    private static void processRay(ArrayList<Ray3> perimeter, Ray3 newRay, Ray3 inputRay) {
        if (newRay != null) {
            if (newRay.z == inputRay.z) {
                if (newRay.y == inputRay.y) {
                    newRay.inputX = inputRay;
                } else if (newRay.x == inputRay.x) {
                    newRay.inputY = inputRay;
                }
            } else {
                newRay.inputZ = inputRay;
            }
            // don't know if the above is correct

            int mapX = originX + newRay.x;
            int mapY = originY + newRay.y;
            int mapZ = originZ + newRay.z;
            if (!newRay.added) {
                perimeter.add(newRay);
                newRay.added = true;
                rayMap[mapX][mapY][mapZ] = newRay;
            }
        }
    }

    private static void processXInput(Ray3 newRay, Ray3 xInput) {
        if (xInput.obscurityX == 0 && xInput.obscurityY == 0 && xInput.obscurityZ == 0) return;
        if (xInput.errorX > 0 && newRay.obscurityX == 0) {
            newRay.errorX = xInput.errorX - xInput.obscurityY - xInput.obscurityZ;
            newRay.errorY = xInput.errorY + xInput.obscurityY;
            newRay.errorZ = xInput.errorZ + xInput.obscurityZ;
            newRay.obscurityX = xInput.obscurityX;
            newRay.obscurityY = xInput.obscurityY;
        }
        if (xInput.errorY <= 0 && xInput.obscurityY > 0 && xInput.errorX > 0) {
            newRay.errorY = xInput.errorY + xInput.obscurityY;
            newRay.errorX = xInput.errorX - xInput.obscurityY;
            newRay.obscurityX = xInput.obscurityX;
            newRay.obscurityY = xInput.obscurityY;
        }
        if (xInput.errorZ <= 0 && xInput.obscurityZ > 0 && xInput.errorX > 0) {
            newRay.errorZ = xInput.errorZ + xInput.obscurityZ;
            newRay.errorX = xInput.errorX - xInput.obscurityZ;
            newRay.obscurityX = xInput.obscurityX;
            newRay.obscurityZ = xInput.obscurityZ;
        }
    }

    private static void processYInput(Ray3 newRay, Ray3 yInput) {
        if (yInput.obscurityX == 0 && yInput.obscurityY == 0 && yInput.obscurityZ == 0) return;
        if (yInput.errorY > 0 && newRay.obscurityY == 0) {
            newRay.errorY = yInput.errorY - yInput.obscurityX - yInput.obscurityZ;
            newRay.errorZ = yInput.errorZ + yInput.obscurityZ;
            newRay.errorX = yInput.errorX + yInput.obscurityX;
            newRay.obscurityX = yInput.obscurityX;
            newRay.obscurityY = yInput.obscurityY;
        }
        if (yInput.errorX <= 0 && yInput.obscurityX > 0 && yInput.errorY > 0) {
            newRay.errorY = yInput.errorY - yInput.obscurityX;
            newRay.errorX = yInput.errorX + yInput.obscurityX;
            newRay.obscurityX = yInput.obscurityX;
            newRay.obscurityY = yInput.obscurityY;
        }
        if (yInput.errorZ <= 0 && yInput.obscurityZ > 0 && yInput.errorY > 0) {
            newRay.errorY = yInput.errorY - yInput.obscurityZ;
            newRay.errorZ = yInput.errorZ + yInput.obscurityZ;
            newRay.obscurityZ = yInput.obscurityZ;
            newRay.obscurityY = yInput.obscurityY;
        }
    }

    private static void processZInput(Ray3 newRay, Ray3 zInput) {
        if (zInput.obscurityX == 0 && zInput.obscurityY == 0 && zInput.obscurityZ == 0) return;
        if (zInput.errorZ > 0 && newRay.obscurityZ == 0) {
            newRay.errorZ = zInput.errorZ - zInput.obscurityX - zInput.obscurityY;
            newRay.errorY = zInput.errorY + zInput.obscurityY;
            newRay.errorX = zInput.errorX + zInput.obscurityX;
            newRay.obscurityX = zInput.obscurityX;
            newRay.obscurityY = zInput.obscurityY;
            newRay.obscurityZ = zInput.obscurityZ;
        }
        if (zInput.errorX <= 0 && zInput.obscurityX > 0 && zInput.errorZ > 0) {
            newRay.errorZ = zInput.errorZ - zInput.obscurityX;
            newRay.errorX = zInput.errorX + zInput.obscurityX;
            newRay.obscurityX = zInput.obscurityX;
            newRay.obscurityZ = zInput.obscurityZ;
        }
        if (zInput.errorY <= 0 && zInput.obscurityY > 0 && zInput.errorZ > 0) {
            newRay.errorZ = zInput.errorZ - zInput.obscurityX;
            newRay.errorY = zInput.errorY + zInput.obscurityY;
            newRay.obscurityY = zInput.obscurityY;
            newRay.obscurityZ = zInput.obscurityZ;
        }
    }

    private static void mergeInput(Map map, Ray3 ray) {
        var obj = map.get(
            ray.x + originX,
            ray.y + originY,
            ray.z + originZ
        );
        Ray3 xi = ray.inputX;
        Ray3 yi = ray.inputY;
        Ray3 zi = ray.inputZ;
        if (xi != null) processXInput(ray, xi);
        if (yi != null) processYInput(ray, yi);
        if (zi != null) processZInput(ray, zi);
        if (xi == null) {
            if (yi != null && zi != null) {
                if (yi.isObscure() && zi.isObscure()) ray.ignore = true;
            } else if (zi != null) {
                if (zi.isObscure()) ray.ignore = true;
            } else {
                if (yi.isObscure()) ray.ignore = true;
            }
        } else if (yi == null) {
            if (zi != null && xi != null) {
                if (xi.isObscure() && zi.isObscure()) ray.ignore = true;
            } else if (zi != null) {
                if (zi.isObscure()) ray.ignore = true;
            } else {
                if (xi.isObscure()) ray.ignore = true;
            }
        } else if (zi == null) {
            if (yi != null && xi != null) {
                if (yi.isObscure() && xi.isObscure()) ray.ignore = true;
            } else if (yi != null) {
                if (yi.isObscure()) ray.ignore = true;
            } else {
                if (xi.isObscure()) ray.ignore = true;
            }
        } else if (xi.isObscure() && yi.isObscure() && zi.isObscure()) {
            ray.ignore = true;
        }
        boolean prop;
        if (ray.inputZ == null) {
            prop = !ray.ignore && obj != null && !obj.isTransparent();
        } else {
            prop = !ray.ignore;
//            var prevObj = map.get(ray.inputZ.x + originX, ray.inputZ.y + originY, ray.inputZ.z + originZ);
//            if (ray.z < ray.inputZ.z) {
//                //dz == -
//                prop = !ray.ignore && !(obj.ceilingTransparent && prevObj.floorTransparent);
//            } else {
//                //dz == +
//                prop = !ray.ignore && !(obj.floorTransparent && prevObj.ceilingTransparent);
//            }
        }
        if (prop) {
            ray.errorX = ray.obscurityX = Math.abs(ray.x);
            ray.errorY = ray.obscurityY = Math.abs(ray.y);
            ray.errorZ = ray.obscurityZ = Math.abs(ray.z);
        }
    }

    private static void expandPerimeterFrom(Map m, ArrayList<Ray3> perimeter, Ray3 r) {
        if (r.x >= 0) {
            processRay(perimeter, newRay(m, r.x + 1, r.y, r.z), r);
        }
        if (r.x <= 0) {
            processRay(perimeter, newRay(m, r.x - 1, r.y, r.z), r);
        }
        if (r.y >= 0) {
            processRay(perimeter, newRay(m, r.x, r.y + 1, r.z), r);
        }
        if (r.y <= 0) {
            processRay(perimeter, newRay(m, r.x, r.y - 1, r.z), r);
        }
        if (r.z >= 0) {
            processRay(perimeter, newRay(m, r.x, r.y, r.z + 1), r);
        }
        if (r.z <= 0) {
            processRay(perimeter, newRay(m, r.x, r.y, r.z - 1), r);
        }
    }

    public boolean[][][] computeFov(Map map, int playerX, int playerY, int playerZ, int maxRadius, boolean lightWalls) {
        rayMap = new Ray3[map.xSize][map.ySize][map.zSize];
        rayMap2 = new Ray3[map.xSize][map.ySize][map.zSize];
        originX = playerX;
        originY = playerY;
        originZ = playerZ;
        int r2 = maxRadius * maxRadius;
        var perimeter = new ArrayList<Ray3>();
        expandPerimeterFrom(map, perimeter, newRay(map, 0, 0, 0));
        for (Ray3 ray : perimeter) {
            int distance = 0;
            if (r2 > 0)
                distance = ray.x * ray.x + ray.y * ray.y + ray.z * ray.z;
            if (distance <= r2) {
                mergeInput(map, ray);
                if (!ray.ignore) expandPerimeterFrom(map, perimeter, ray);
            } else {
                ray.ignore = true;
            }
        }

        // set fov data
        var fov = new boolean[map.xSize][map.ySize][map.zSize];
        for (int x = 0; x < map.xSize; x++) {
            for (int y = 0; y < map.ySize; y++) {
                for (int z = 0; z < map.zSize; z++) {
                    var r = rayMap[x][y][z];
                    fov[x][y][z] = r != null && !r.ignore &&
                        (r.errorX <= 0 || r.errorX > r.obscurityX) &&
                        (r.errorY <= 0 || r.errorY > r.obscurityY) &&
                        (r.errorZ <= 0 || r.errorZ > r.obscurityZ);
                }
            }
        }
        fov[originX][originY][originZ] = true;

//        if (lightWalls) {
//            int xmin = 0, ymin = 0, zmin = 0;
//            int xmax = map.xSize, ymax = map.ySize, zmax = map.zSize;
//            if (maxRadius > 0) {
//                xmin = Math.max(0, playerX - maxRadius);
//                ymin = Math.max(0, playerY - maxRadius);
//                zmin = Math.max(0, playerZ - maxRadius);
//                xmax = Math.min(map.xSize, playerX + maxRadius + 1);
//                ymax = Math.min(map.ySize, playerY + maxRadius + 1);
//                zmax = Math.min(map.zSize, playerZ + maxRadius + 1);
//            }
//            // is this stuff right?
//            mapPostProcessing(map, xmin, ymin, zmin, playerX, playerY, playerZ, -1, -1, -1);
//            mapPostProcessing(map, playerX, ymin, zmin, xmax - 1, playerY, playerZ, 1, -1, -1);
//            mapPostProcessing(map, xmin, playerY, zmin, playerX, ymax - 1, playerZ, -1, 1, -1);
//            mapPostProcessing(map, xmin, ymin, playerZ, playerX, playerY, zmin - 1, -1, -1, 1);
//            mapPostProcessing(map, playerX, playerY, zmin, xmax - 1, ymax - 1, playerZ, 1, 1, -1);
//            mapPostProcessing(map, xmin, playerY, playerZ, playerX, ymax - 1, zmax - 1, -1, 1, 1);
//            mapPostProcessing(map, playerX, ymin, playerZ, xmax - 1, playerY, zmax - 1, 1, -1, 1);
//            mapPostProcessing(map, playerX, playerY, playerZ, xmax - 1, ymax - 1, zmax - 1, 1, 1, 1);
//        }
        return fov;
    }

//    void mapPostProcessing(
//        Map map,
//        int x0,
//        int y0,
//        int z0,
//        int x1,
//        int y1,
//        int z1,
//        int dx,
//        int dy,
//        int dz
//    ) {
//        int cx, cy, cz;
//        for (cx = x0; cx <= x1; cx++) {
//            for (cy = y0; cy <= y1; cy++) {
//                for (cz = z0; cz <= z1; cz++) {
//                    int x2 = cx + dx;
//                    int y2 = cy + dy;
//                    int z2 = cz + dz;
//                    var obj = map.get(cx, cy, cz);
//                    //wrong, consider dz here
//                    boolean sightCheck;
//                    boolean walkCheck;
//                    if (dz == 0) {
//                        sightCheck = obj.isTransparent();
//                        walkCheck = obj.isWalkable();
//                    } else if (dz > 0) {
//                        sightCheck = obj.floorTransparent;
//                        walkCheck = obj.rampDown;
//                    } else {
//                        sightCheck = obj.ceilingTransparent;
//                        walkCheck = obj.rampUp;
//                    }
//                    if (offset < map.nbcells && map.cells[offset].fov == 1 && sightCheck && walkCheck) {
//                        boolean walkCheck2;
//                        if (x2 >= x0 && x2 <= x1) {
//                            var obj2 = map.get(x2, cy, cz);
//                            if (dz == 0) {
//                                walkCheck2 = obj2.isWalkable();
//                            } else if (dz > 0) {
//                                walkCheck2 = obj2.rampUp;
//                            } else {
//                                walkCheck2 = obj2.rampDown;
//                            }
//
//                            if (offset2 < map.nbcells && !walkCheck2)
//                                fov[x2][cy][cz] = true;
//                        }
//                        if (y2 >= y0 && y2 <= y1) {
//                            var obj2 = map.get(cx, y2, cz);
//                            if (dz == 0) {
//                                walkCheck2 = obj2.isWalkable();
//                            } else if (dz > 0) {
//                                walkCheck2 = obj2.rampUp;
//                            } else {
//                                walkCheck2 = obj2.rampDown;
//                            }
//
//                            if (offset2 < map.nbcells && !walkCheck2)
//                                map.cells[offset2].fov = 1;
//                        }
//                        if (z2 >= z0 && z2 <= z1) {
//                            var obj2 = map.get(cx, cy, z2);
//                            if (dz == 0) {
//                                walkCheck2 = obj2.isWalkable();
//                            } else if (dz > 0) {
//                                walkCheck2 = obj2.rampUp;
//                            } else {
//                                walkCheck2 = obj2.rampDown;
//                            }
//
//                            if (offset2 < map.nbcells && !walkCheck2)
//                                fov[cx][cy][z2] = true;
//                        }
//                        if (x2 >= x0 && x2 <= x1 && y2 >= y0 && y2 <= y1) {
//                            var obj2 = map.get(x2, y2, cz);
//                            if (dz == 0) {
//                                walkCheck2 = obj2.isWalkable();
//                            } else if (dz > 0) {
//                                walkCheck2 = obj2.rampUp;
//                            } else {
//                                walkCheck2 = obj2.rampDown;
//                            }
//
//                            if (offset2 < map.nbcells && !walkCheck2)
//                                map.cells[offset2].fov = 1;
//                        }
//                        //not sure this is correct.
//                        if (x2 >= x0 && x2 <= x1 && z2 >= z0 && z2 <= z1) {
//                            var obj2 = map.get(x2, cy, z2);
//                            if (dz == 0) {
//                                walkCheck2 = obj2.isWalkable();
//                            } else if (dz > 0) {
//                                walkCheck2 = obj2.rampUp;
//                            } else {
//                                walkCheck2 = obj2.rampDown;
//                            }
//
//                            if (offset2 < map.nbcells && !walkCheck2)
//                                map.cells[offset2].fov = 1;
//                        }
//                        if (y2 >= y0 && y2 <= y1 && z2 >= z0 && z2 <= z1) {
//                            var obj2 = map.get(cx, y2, z2);
//                            if (dz == 0) {
//                                walkCheck2 = obj2.isWalkable();
//                            } else if (dz > 0) {
//                                walkCheck2 = obj2.rampUp;
//                            } else {
//                                walkCheck2 = obj2.rampDown;
//                            }
//
//                            if (offset2 < map.nbcells && !walkCheck2)
//                                map.cells[offset2].fov = 1;
//                        }
//                        if (x2 >= x0 && x2 <= x1 && y2 >= y0 && y2 <= y1 && z2 >= z0 && z2 <= z1) {
//                            var obj2 = map.get(x2, y2, z2);
//                            if (dz == 0) {
//                                walkCheck2 = obj2.isWalkable();
//                            } else if (dz > 0) {
//                                walkCheck2 = obj2.rampUp;
//                            } else {
//                                walkCheck2 = obj2.rampDown;
//                            }
//
//                            if (offset2 < map.nbcells && !walkCheck2)
//                                map.cells[offset2].fov = 1;
//                        }
//                    }
//                }
//            }
//        }
//    }
}
