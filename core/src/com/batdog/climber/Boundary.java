package com.batdog.climber;

import com.badlogic.gdx.math.Vector2;

public class Boundary {
    World world;
    Vector2 point;
    Vector2 normal;

    Boundary (World world, Vector2 point, Vector2 normal) {
        this.world = world;
        this.point = point;
        this.normal = normal;
    }

//    Vector2 getParallel() {
//        return normal.cpy().rotate90(-1);
//    }
}
