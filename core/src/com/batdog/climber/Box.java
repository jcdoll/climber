package com.batdog.climber;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
import java.util.List;

// Encapsulates box locations and swept collision detection
// Calculations assume that extents.x and extents.y are both positive
//
// References
// http://hamaluik.com/posts/simple-aabb-collision-using-the-minkowski-difference/
// http://hamaluik.com/posts/swept-aabb-collision-detection-using-the-minkowski-difference/
public class Box {
    World world;
    Vector2 position = new Vector2(); // from origin to lower left corner
    Vector2 extents = new Vector2();
    Vector2 velocity = new Vector2();
    Vector2 force = new Vector2();
    boolean affectedByGravity;
    float mass;
    Texture texture;

    // x and y are distances to the lower left corner of the box
    // w and h are the total extents of the box
    Box(World world) {
        this.world = world;
    }

    Box(float x, float y, float w, float h) {
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        extents = new Vector2(w, h);
    }

    void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }

    void setVelocity(float x, float y) {
        velocity.x = x;
        velocity.y = y;
    }

    void setExtents(float x, float y) {
        extents.x = x;
        extents.y = y;
    }

    void setMass(float m) {
        mass = m;
    }

    void setTexture(Texture t) {
        texture = t;
    }

    void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, extents.x, extents.y);
    }

    // Distance from origin to lower left corner
    float getLeft() {
        return position.x;
    }

    // Distance from origin to lower left corner
    float getBottom() {
        return position.y;
    }

    float getRight() {
        return position.x + extents.x;
    }

    float getTop() {
        return position.y + extents.y;
    }

    float getWidth() {
        return extents.x;
    }

    float getHeight() {
        return extents.y;
    }
}
